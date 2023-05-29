package com.example.CleaningService.Controllers;

import com.example.CleaningService.Models.CleaningRequest;
import com.example.CleaningService.Models.Comment;
import com.example.CleaningService.Models.Service;
import com.example.CleaningService.Models.User;
import com.example.CleaningService.Repositories.CleaningRequestRepository;
import com.example.CleaningService.Repositories.CommentRepository;
import com.example.CleaningService.Repositories.ServiceRepository;
import com.example.CleaningService.Repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@SessionAttributes("user")
public class CleaningController {

    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private CleaningRequestRepository cleaningRequestRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/request-cleaning")
    public String showRequestCleaningForm(@RequestParam(value = "serviceId", required = false) Integer serviceId, Model model) {
        List<Service> services = serviceRepository.findAll();
        model.addAttribute("services", services);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        LocalDateTime now = LocalDateTime.now();
        String nowFormatted = now.format(formatter);
        model.addAttribute("now", nowFormatted);

        LocalDateTime monthAhead = now.plusMonths(1);
        String monthAheadFormatted = monthAhead.format(formatter);
        model.addAttribute("monthAhead", monthAheadFormatted);

        if (serviceId != null) {
            Service selectedService = serviceRepository.findById(serviceId).orElse(null);
            model.addAttribute("selectedService", selectedService);
        }

        return "/request-cleaning";
    }


    @PostMapping("/submitRequest")
    public String submitCleaningRequest(@RequestParam("serviceId") int serviceId,
                                        @RequestParam("dateTime") String dateTime,
                                        HttpSession session) {

        CleaningRequest request = new CleaningRequest();
        Service service = serviceRepository.findById(serviceId).orElse(null);
        if (service == null) {
            return "redirect:/request-cleaning";
        }

        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            return "redirect:/login";
        }
        User freshUser = userRepository.findById((long) sessionUser.getId()).orElse(null);
        if (freshUser == null) {
            return "redirect:/login";
        }

        request.setService(service);
        request.setDateTime(dateTime);
        request.setUser(freshUser);
        request.setStatus("В обработке");
        request.setComments(null);
        request.setEmployee(null);
        request.setInventory(null);
        cleaningRequestRepository.save(request);

        return "redirect:/user-requests";
    }


    @GetMapping("/user-requests")
    public String showPreviousRequests(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        } else {
            List<CleaningRequest> userRequests = cleaningRequestRepository.findByUserAndStatusNot(user, "Завершён");
            model.addAttribute("requests", userRequests);

            Map<CleaningRequest, List<Comment>> commentsForRequests = new HashMap<>();
            for (CleaningRequest request : userRequests) {
                List<Comment> comments = commentRepository.findByCleaningRequest(request);
                commentsForRequests.put(request, comments);
            }
            model.addAttribute("commentsForRequests", commentsForRequests);

            return "user-requests";
        }
    }

    @GetMapping("/user-archive")
    public String showArchivedRequests(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        } else {
            List<CleaningRequest> userRequests = cleaningRequestRepository.findByUserAndStatus(user, "Завершён");
            model.addAttribute("requests", userRequests);

            Map<CleaningRequest, List<Comment>> commentsForRequests = new HashMap<>();
            Map<CleaningRequest, Boolean> userCommented = new HashMap<>();
            for (CleaningRequest request : userRequests) {
                List<Comment> comments = commentRepository.findByCleaningRequest(request);
                commentsForRequests.put(request, comments);

                boolean hasCommented = comments.stream().anyMatch(comment -> comment.getUser().equals(user));
                userCommented.put(request, hasCommented);
            }
            model.addAttribute("commentsForRequests", commentsForRequests);
            model.addAttribute("userCommented", userCommented);

            return "user-archive";
        }
    }


}
