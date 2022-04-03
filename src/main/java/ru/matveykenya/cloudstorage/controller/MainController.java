package ru.matveykenya.cloudstorage.controller;

import org.springframework.web.bind.annotation.*;
import ru.matveykenya.cloudstorage.service.FileService;

import java.util.List;

@RestController
public class MainController {

    private final FileService service;
    public MainController(FileService service) {
        this.service = service;
    }

    //список файлов
    @GetMapping("/list")
    public List<String> getListFiles(@RequestParam int limit) {
        return service.getListFiles(limit);
    }

    //добавляем файл
    @PostMapping("/file")
    public String uploadFile(){
        return "upload";
    }

    //Скачиваем файл
    @GetMapping("/file")
    public String getFile(){
        return "get file";
    }

    //Изменяем файл
    @PutMapping("/file")
    public String putFile(){
        return "put file";
    }

    //Удаляем файл
    @DeleteMapping("/file")
    public String delFile(@RequestParam String fileName){
        return "del file";
    }
}
