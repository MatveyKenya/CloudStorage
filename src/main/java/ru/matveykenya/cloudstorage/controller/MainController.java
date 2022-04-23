package ru.matveykenya.cloudstorage.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.matveykenya.cloudstorage.entity.FileObject;
import ru.matveykenya.cloudstorage.service.FileService;
import java.io.FileNotFoundException;
import java.security.Principal;
import java.util.List;

@RestController
public class MainController {

    private final FileService service;
    public MainController(FileService service) {
        this.service = service;
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
    public ResponseEntity<Object> getFile(@RequestParam String filename, Principal principal) throws FileNotFoundException {
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
