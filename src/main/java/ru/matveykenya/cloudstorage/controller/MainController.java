package ru.matveykenya.cloudstorage.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.matveykenya.cloudstorage.dto.AuthenticationRequestDto;
import ru.matveykenya.cloudstorage.entity.FileObject;
import ru.matveykenya.cloudstorage.entity.User;
import ru.matveykenya.cloudstorage.jwt.JwtTokenProvider;
import ru.matveykenya.cloudstorage.service.FileService;
import ru.matveykenya.cloudstorage.service.UserService;

import java.io.FileNotFoundException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MainController {

    private final FileService service;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public MainController(FileService service, UserService userService, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.service = service;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody AuthenticationRequestDto requestDto) {
        try {
            System.out.println("попал в login");
            String username = requestDto.getLogin();
            System.out.println(username + "   " + requestDto.getPassword());

            //todo - на этом моменте застрял
            //на этой строчке застревает через Постман, а запрос через приложение ФРОН вообще сюда не добирается.
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));

            User user = userService.findByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("User with username: " + username + " not found");
            }

            System.out.println("выдача token");
            String token = jwtTokenProvider.createToken(username);

            Map<Object, Object> response = new HashMap<>();
            response.put("username", username);
            response.put("auth-token", token);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    //список файлов
    @GetMapping("/list")
    public List<FileObject> getListFiles(@RequestParam int limit, Principal principal) {
        return service.getListFiles(principal.getName(), limit);
    }

    //добавляем файл
    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadFile(@RequestParam MultipartFile file, Principal principal) {
        return service.uploadFile(principal.getName(), file);
    }

    //Скачиваем файл
    @GetMapping("/file")
    public ResponseEntity<Object> getFile(@RequestParam String filename, Principal principal) {
        return service.getFile(filename, principal.getName());
    }

    //Изменяем имя файла в хранилище
    @PutMapping("/file")
    public String putFile(@RequestParam String filename, @RequestParam String newFilename, Principal principal){
        return service.renameFile(filename, newFilename, principal.getName());
    }

    //Удаляем файл
    @DeleteMapping("/file")
    public String delFile(@RequestParam String filename, Principal principal) throws FileNotFoundException {
        return service.deleteFile(filename, principal.getName());
    }
}
