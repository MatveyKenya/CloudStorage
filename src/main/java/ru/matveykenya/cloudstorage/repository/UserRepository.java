package ru.matveykenya.cloudstorage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.matveykenya.cloudstorage.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>{

    User findUserByUsername(String userName);
}
