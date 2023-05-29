package com.example.CleaningService.Controllers;

import com.example.CleaningService.Models.User;
import com.example.CleaningService.Services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String showProfile(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        } else {
            model.addAttribute("user", user);
            return "profile";
        }
    }

    @PostMapping("/update-profile")
    public String updateProfile(@RequestParam("phoneNumber") String phoneNumber,
                                @RequestParam("avatar") MultipartFile avatar,
                                HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            userService.updateProfile(user, phoneNumber, avatar);
        }
        return "redirect:/profile";
    }

    @GetMapping("/table-users")
    public String getUsers(Model model, @RequestParam(required = false) Long userId) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);

        if (userId != null) {
            User selectedUser = userService.getUserById(userId);
            model.addAttribute("selectedUser", selectedUser);
        }

        return "table-users";
    }


    @GetMapping("/add-admin")
    public String showAddAdminForm(Model model) {
        User user = new User();
        user.setRole("admin");
        model.addAttribute("user", user);
        return "add-admin";
    }

    @PostMapping("/addAdmin")
    public String addAdmin(@ModelAttribute("user") User user) {
        userService.saveUser(user);
        return "redirect:/table-users";
    }

    @PostMapping("/deleteUser")
    public String deleteUser(@ModelAttribute User user, HttpSession session) {
        User currentUser = (User) session.getAttribute("loggedInUser");

        if (currentUser != null && !Objects.equals(currentUser.getId(), user.getId())) {
            userService.deleteUserById(user.getId());
        }

        return "redirect:/table-users";
    }

}

