package ru.matveykenya.cloudstorage.service;

import org.springframework.stereotype.Service;
import ru.matveykenya.cloudstorage.entity.User;
import ru.matveykenya.cloudstorage.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }
}
