package ru.matveykenya.cloudstorage.service;

import org.springframework.stereotype.Service;
import ru.matveykenya.cloudstorage.repository.FileRepository;

import java.util.List;

@Service
public class FileService {

    private final FileRepository repository;
    public FileService(FileRepository repository) {
        this.repository = repository;
    }

    public List<String> getListFiles(int limit){
        return List.of("str1", "str2", "str3");
    }

}
