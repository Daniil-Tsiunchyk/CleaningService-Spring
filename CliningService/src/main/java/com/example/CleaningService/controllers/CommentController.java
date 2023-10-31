package com.example.CleaningService.controllers;

import com.example.CleaningService.models.CleaningRequest;
import com.example.CleaningService.models.Comment;
import com.example.CleaningService.models.User;
import com.example.CleaningService.repositories.CleaningRequestRepository;
import com.example.CleaningService.repositories.CommentRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CommentController {

  private final CommentRepository commentRepository;
  private final CleaningRequestRepository cleaningRequestRepository;

  public CommentController(
    CommentRepository commentRepository,
    CleaningRequestRepository cleaningRequestRepository
  ) {
    this.commentRepository = commentRepository;
    this.cleaningRequestRepository = cleaningRequestRepository;
  }

  @PostMapping("/add-comment")
  public String addComment(
    @RequestParam("requestId") int requestId,
    @RequestParam("content") String content,
    @ModelAttribute("user") User user
  ) {
    CleaningRequest request = cleaningRequestRepository
      .findById((long) requestId)
      .orElse(null);
    if (request == null) {
      return "redirect:/user-requests";
    }

    Comment comment = new Comment();
    comment.setContent(content);
    comment.setDateTime(LocalDateTime.now());
    comment.setUser(user);
    comment.setCleaningRequest(request);
    commentRepository.save(comment);

    return "redirect:/user-archive";
  }
}
