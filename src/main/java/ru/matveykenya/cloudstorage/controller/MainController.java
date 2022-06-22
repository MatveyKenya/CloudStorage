package ru.matveykenya.cloudstorage.controller;

import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.matveykenya.cloudstorage.dto.AuthenticationRequestDto;
import ru.matveykenya.cloudstorage.dto.FileNameDto;
import ru.matveykenya.cloudstorage.jwt.JwtTokenUtil;
import ru.matveykenya.cloudstorage.service.FileService;
import ru.matveykenya.cloudstorage.service.UserService;

import java.io.FileNotFoundException;
import java.security.Principal;

@RestController
public class MainController {

    private final FileService service;

    public MainController(FileService service, UserService userService, AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody AuthenticationRequestDto requestDto) {
        return service.login(requestDto.getLogin(), requestDto.getPassword());
    }

    //список файлов
    @GetMapping("/list")
    public ResponseEntity<Object> getListFiles(@RequestParam int limit, Principal principal) {
        limit = 20; // не понял где во ФРОНТЕ определен лимит там 3 по умолчанию мало
        return service.getListFiles(principal.getName(), limit);
    }

    //добавляем файл
    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> uploadFile(@RequestParam MultipartFile file, Principal principal) {
        return service.uploadFile(principal.getName(), file);
    }

    //Скачиваем файл
    @GetMapping("/file")
    public ResponseEntity<Object> getFile(@RequestParam String filename, Principal principal) {
        return service.getFile(filename, principal.getName());
    }

    //Изменяем имя файла в хранилище
    @PutMapping("/file")
    public ResponseEntity<Object> putFile(@RequestParam String filename, @RequestBody FileNameDto newFilename, Principal principal){
        return service.renameFile(filename, newFilename.getFilename(), principal.getName());
    }

    //Удаляем файл
    @DeleteMapping("/file")
    public ResponseEntity<Object> delFile(@RequestParam String filename, Principal principal) throws FileNotFoundException {
        return service.deleteFile(filename, principal.getName());
    }


}
