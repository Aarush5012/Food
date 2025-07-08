package com.adityaPandit.zaika.services;

import com.adityaPandit.zaika.entities.User;
import com.adityaPandit.zaika.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER"); // Default role
        userRepo.save(user);
    }

    public boolean existsByUsername(String username) {
        return userRepo.existsByUsername(username);
    }
    @PostConstruct
    public void addDefaultAdmin() {
        if (!userRepo.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123")); // choose strong pass later
            admin.setRole("ADMIN");
            userRepo.save(admin);
        }
    }
    public List<User> getAllUsers() {
        return userRepo.findAll(); // Assuming you have userRepo autowired in the service
    }


}
