package ru.matveykenya.cloudstorage.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.matveykenya.cloudstorage.entity.FileObject;
import ru.matveykenya.cloudstorage.jwt.JwtTokenUtil;
import ru.matveykenya.cloudstorage.repository.FileRepository;
import ru.matveykenya.cloudstorage.repository.UserRepository;
import ru.matveykenya.cloudstorage.schema.ResponseSchema;

import java.io.*;
import java.util.List;

@Service
public class FileService {
    @Value("${upload.path}")
    private String globalPath;

    private final int LIMIT_MAX = 1000;
    private final FileRepository repository;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    public FileService(FileRepository repository, UserRepository userRepository, AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public ResponseEntity<Object> login(String username, String password){
        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
            String token = jwtTokenUtil.createToken(username);
            return ResponseSchema.login(token);

        } catch (AuthenticationException e) {
            //throw new BadCredentialsException("Invalid username or password");
            return ResponseSchema.error(401, e.getMessage());
        }
    }

    public ResponseEntity<Object> getListFiles(String username, int limit){
        if (limit <= 0 || limit > LIMIT_MAX){
            return ResponseSchema.error(400, "Error input data. 0 < Limit < " + LIMIT_MAX);
        }
        List<FileObject> list = repository.findFilesByUsernameWithLimit(username, limit);
        if (list != null){
            return ResponseEntity.ok(list);
        }
        return ResponseSchema.error(500, "Error getting file list");
    }

    public ResponseEntity<Object> deleteFile(String filename, String username) {
        FileObject fileObject = repository.findByFilenameAndUser(filename, userRepository.findUserByUsername(username));
        if (fileObject != null){
            String path = globalPath + "/" + username + "/" + filename;
            repository.delete(fileObject);
            File file = new File(path);
            if (!file.exists()){
                return ResponseSchema.error(400, "File " + filename + " not found in Cloud Store");
            }
            if (file.delete()){
                return ResponseSchema.ok();
            }
        }
        return ResponseSchema.error(500, "Error delete file: " + filename);
    }

    public ResponseEntity<Object> getFile(String filename, String username) {
        String path = globalPath + "/" + username + "/" + filename;
        System.out.println("get file from path --- " + path);
        File file = new File(path);
        if (!file.exists()){
            return ResponseSchema.error(400, "File " + filename + " not found in Cloud Store");
        }
        try{
            FileInputStream fis =  new FileInputStream(file);
            InputStreamResource resource = new InputStreamResource(fis);

            return ResponseEntity.ok()
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("application/txt")).body(resource);

        } catch (IOException ex){
            //throw new RuntimeException(ex);
            return ResponseSchema.error(500, ex.getMessage());
        }
    }

    public ResponseEntity<Object> uploadFile(String username, MultipartFile file) {
        String fileName = file.getOriginalFilename();
        File convertFile = new File(globalPath + "/" + username + "/" + fileName);
        if (convertFile.exists()){
            return ResponseSchema.error400();
        }
        try (FileOutputStream fos = new FileOutputStream(convertFile)){
            fos.write(file.getBytes());
            FileObject fileObject = FileObject.builder()
                    .filename(fileName)
                    .user(userRepository.findUserByUsername(username))
                    .size(file.getSize())
                    .build();
            repository.save(fileObject);
            return ResponseSchema.ok();
        } catch (IOException ex){
            //throw new RuntimeException(ex);
            return ResponseSchema.error500();
        }
    }

    public ResponseEntity<Object> renameFile(String filename, String newFilename, String userName) {
        File file = new File(globalPath + "/" + userName + "/" + filename);
        File newFile = new File(globalPath + "/" + userName + "/" + newFilename);
        if (file.exists()&&!newFile.exists()){
            if (file.renameTo(newFile)){
                FileObject fileObject = repository.findByFilenameAndUser(filename, userRepository.findUserByUsername(userName));
                fileObject.setFilename(newFilename);
                repository.save(fileObject);
                return ResponseSchema.ok();
            }
            return ResponseSchema.error(500, "Error upload file");
        }
        return ResponseSchema.error400();
    }
}
