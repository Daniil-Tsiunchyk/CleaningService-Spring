package com.example.CleaningService.services;

import com.example.CleaningService.models.User;
import com.example.CleaningService.repositories.UserRepository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public void updateProfile(
    User user,
    String phoneNumber,
    MultipartFile avatar
  ) {
    user.setPhoneNumber(phoneNumber);
    try {
      if (!avatar.isEmpty()) {
        byte[] bytes = avatar.getBytes();
        String UPLOAD_DIR = "uploads/";
        Path path = Paths.get(
          UPLOAD_DIR + File.separator + avatar.getOriginalFilename()
        );
        if (!Files.exists(path.getParent())) {
          try {
            Files.createDirectories(path.getParent());
          } catch (Exception e) {
            throw new RuntimeException(
              "Could not create directory " +
              path.getParent() +
              ": " +
              e.getMessage()
            );
          }
        }
        Files.write(path, bytes);

        user.setAvatarUrl("/" + UPLOAD_DIR + avatar.getOriginalFilename());
      }
      userRepository.save(user);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public User findByEmailAndPassword(String email, String password) {
    User user = userRepository.findByEmail(email);

    if (user != null && password.equals(user.getPassword())) {
      return user;
    }

    return null;
  }

  public User findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  public void save(User user) {
    userRepository.save(user);
  }

  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  public User getUserById(Long id) {
    return userRepository.findById(id).orElse(null);
  }

  public void saveUser(User user) {
    userRepository.save(user);
  }

  public void deleteUserById(Long id) {
    userRepository.deleteById(id);
  }
}
