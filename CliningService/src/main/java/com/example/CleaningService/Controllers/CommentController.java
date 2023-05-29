package com.example.CleaningService.Controllers;

import com.example.CleaningService.Models.CleaningRequest;
import com.example.CleaningService.Models.Comment;
import com.example.CleaningService.Models.User;
import com.example.CleaningService.Repositories.CleaningRequestRepository;
import com.example.CleaningService.Repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller

public class CommentController {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CleaningRequestRepository cleaningRequestRepository;

    @PostMapping("/add-comment")
    public String addComment(@RequestParam("requestId") int requestId,
                             @RequestParam("content") String content,
                             @ModelAttribute("user") User user) {

        CleaningRequest request = cleaningRequestRepository.findById((long) requestId).orElse(null);
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
