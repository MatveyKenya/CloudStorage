package ru.matveykenya.cloudstorage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.matveykenya.cloudstorage.entity.FileObject;
import ru.matveykenya.cloudstorage.entity.User;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FileObject, Long> {

    @Query(value = "select * from files f where username = :username limit :limit", nativeQuery = true)
    List<FileObject> findFilesByUsernameWithLimit(String username, int limit);

    FileObject findByFilenameAndUser(String filename, User user);
}
