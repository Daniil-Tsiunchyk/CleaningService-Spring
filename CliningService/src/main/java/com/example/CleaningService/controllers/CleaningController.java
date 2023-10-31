package com.example.CleaningService.controllers;

import com.example.CleaningService.models.CleaningRequest;
import com.example.CleaningService.models.Comment;
import com.example.CleaningService.models.Service;
import com.example.CleaningService.models.User;
import com.example.CleaningService.repositories.CleaningRequestRepository;
import com.example.CleaningService.repositories.CommentRepository;
import com.example.CleaningService.repositories.ServiceRepository;
import com.example.CleaningService.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@SessionAttributes("user")
public class CleaningController {

  private final ServiceRepository serviceRepository;
  private final CleaningRequestRepository cleaningRequestRepository;
  private final CommentRepository commentRepository;
  private final UserRepository userRepository;

  public CleaningController(
    ServiceRepository serviceRepository,
    CleaningRequestRepository cleaningRequestRepository,
    CommentRepository commentRepository,
    UserRepository userRepository
  ) {
    this.serviceRepository = serviceRepository;
    this.cleaningRequestRepository = cleaningRequestRepository;
    this.commentRepository = commentRepository;
    this.userRepository = userRepository;
  }

  @GetMapping("/request-cleaning")
  public String showRequestCleaningForm(
    @RequestParam(value = "serviceId", required = false) Integer serviceId,
    Model model
  ) {
    List<Service> services = serviceRepository.findAll();
    model.addAttribute("services", services);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
      "yyyy-MM-dd'T'HH:mm"
    );

    LocalDateTime now = LocalDateTime.now();
    String nowFormatted = now.format(formatter);
    model.addAttribute("now", nowFormatted);

    LocalDateTime monthAhead = now.plusMonths(1);
    String monthAheadFormatted = monthAhead.format(formatter);
    model.addAttribute("monthAhead", monthAheadFormatted);

    if (serviceId != null) {
      Service selectedService = serviceRepository
        .findById(serviceId)
        .orElse(null);
      model.addAttribute("selectedService", selectedService);
    }

    return "/request-cleaning";
  }

  @PostMapping("/submitRequest")
  public String submitCleaningRequest(
    @RequestParam("serviceId") int serviceId,
    @RequestParam("dateTime") String dateTime,
    HttpSession session
  ) {
    CleaningRequest request = new CleaningRequest();
    Service service = serviceRepository.findById(serviceId).orElse(null);
    if (service == null) {
      return "redirect:/request-cleaning";
    }

    User sessionUser = (User) session.getAttribute("user");
    if (sessionUser == null) {
      return "redirect:/login";
    }
    User freshUser = userRepository.findById(sessionUser.getId()).orElse(null);
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
      List<CleaningRequest> userRequests = cleaningRequestRepository.findByUserAndStatusNot(
        user,
        "Завершён"
      );
      model.addAttribute("requests", userRequests);

      Map<CleaningRequest, List<Comment>> commentsForRequests = new HashMap<>();
      for (CleaningRequest request : userRequests) {
        List<Comment> comments = commentRepository.findByCleaningRequest(
          request
        );
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
      List<CleaningRequest> userRequests = cleaningRequestRepository.findByUserAndStatus(
        user,
        "Завершён"
      );
      model.addAttribute("requests", userRequests);

      Map<CleaningRequest, List<Comment>> commentsForRequests = new HashMap<>();
      Map<CleaningRequest, Boolean> userCommented = new HashMap<>();
      for (CleaningRequest request : userRequests) {
        List<Comment> comments = commentRepository.findByCleaningRequest(
          request
        );
        commentsForRequests.put(request, comments);

        boolean hasCommented = comments
          .stream()
          .anyMatch(comment -> comment.getUser().equals(user));
        userCommented.put(request, hasCommented);
      }
      model.addAttribute("commentsForRequests", commentsForRequests);
      model.addAttribute("userCommented", userCommented);

      return "user-archive";
    }
  }
}
