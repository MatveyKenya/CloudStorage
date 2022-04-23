package ru.matveykenya.cloudstorage.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.matveykenya.cloudstorage.entity.FileObject;
import ru.matveykenya.cloudstorage.repository.FileRepository;
import ru.matveykenya.cloudstorage.repository.UserRepository;

import java.io.*;
import java.util.List;

@Service
public class FileService {
    @Value("${upload.path}")
    private String globalPath;

    private final FileRepository repository;
    private final UserRepository userRepository;

    public FileService(FileRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public List<FileObject> getListFiles(String username, int limit){
        return repository.findFilesByUsernameWithLimit(username, limit);
    }

    public String deleteFile(String filename, String username) throws FileNotFoundException {
        FileObject fileObject = repository.findByFilenameAndUser(filename, userRepository.findUserByUsername(username));
        if (fileObject != null){
            String path = globalPath + "/" + username + "/" + filename;
            repository.delete(fileObject);
            File file = new File(path);
            if (file.delete()){
                return filename;
            }
        }
        throw new FileNotFoundException("file " + filename + " is not found");
    }

    public ResponseEntity<Object> getFile(String filename, String username) {
        String fileName = globalPath + "/" + username + "/" + filename;
        File file = new File(fileName);
        try{
            FileInputStream fis =  new FileInputStream(file);
            InputStreamResource resource = new InputStreamResource(fis);

            return ResponseEntity.ok()
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("application/txt")).body(resource);

        } catch (IOException ex){
            throw new RuntimeException(ex);
        }
    }

    public String uploadFile(String username, MultipartFile file) {
        String fileName = file.getOriginalFilename();
        File convertFile = new File(globalPath + "/" + username + "/" + fileName);
        if (convertFile.exists()){
            return "File " + fileName + " is already exists!";
        }
        try (FileOutputStream fos = new FileOutputStream(convertFile)){
            fos.write(file.getBytes());
            FileObject fileObject = FileObject.builder()
                    .filename(fileName)
                    .user(userRepository.findUserByUsername(username))
                    .build();
            repository.save(fileObject);
            return "File " + fileName + " is upload successfully";
        } catch (IOException ex){
            throw new RuntimeException(ex);
        }
    }

    public String renameFile(String filename, String newFilename, String userName) {
        File file = new File(globalPath + "/" + userName + "/" + filename);
        File newFile = new File(globalPath + "/" + userName + "/" + newFilename);
        if (file.exists()&&!newFile.exists()){
            if (file.renameTo(newFile)){
                FileObject fileObject = repository.findByFilenameAndUser(filename, userRepository.findUserByUsername(userName));
                fileObject.setFilename(newFilename);
                repository.save(fileObject);
                return "File " + filename + " renamed to " + newFilename + " successfully";
            }
        }
        return "Error. The file does not exist or the new name is already taken.";
    }
}
