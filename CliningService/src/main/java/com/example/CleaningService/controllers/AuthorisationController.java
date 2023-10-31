package com.example.CleaningService.controllers;

import com.example.CleaningService.models.User;
import com.example.CleaningService.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorisationController {

  private final UserService userService;

  public AuthorisationController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/")
  public String mainPage() {
    return "login";
  }

  @GetMapping("/login")
  public String showLogin() {
    return "login";
  }

  @PostMapping("/authorisation")
  public String processLogin(
    @RequestParam("email") String email,
    @RequestParam("password") String password,
    Model model,
    HttpSession session
  ) {
    User user = userService.findByEmailAndPassword(email, password);
    if (user != null) {
      session.setAttribute("user", user);
      if (user.getRole().equals("admin")) {
        return "redirect:/table-requests";
      } else if (user.getRole().equals("user")) {
        return "redirect:/request-cleaning";
      }
    } else {
      model.addAttribute("error", "Неверная электронная почта или пароль");
      return "login";
    }

    return email;
  }

  @GetMapping("/registration")
  public String showRegistration() {
    return "registration";
  }

  @PostMapping("/registration")
  public String processRegistration(
    @RequestParam("name") String name,
    @RequestParam("email") String email,
    @RequestParam("password") String password,
    @RequestParam("confirm_password") String confirmPassword,
    Model model,
    HttpSession session
  ) {
    if (!password.equals(confirmPassword)) {
      model.addAttribute("error", "Пароли не совпадают");
      return "registration";
    } else {
      User existingUser = userService.findByEmail(email);
      if (existingUser != null) {
        model.addAttribute(
          "error",
          "Пользователь с такой электронной почтой уже существует"
        );
        return "registration";
      } else {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole("user");
        userService.save(user);
        session.setAttribute("user", user);
        return "redirect:/request-cleaning";
      }
    }
  }
}
