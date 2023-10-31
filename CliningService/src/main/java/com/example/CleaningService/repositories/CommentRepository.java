package com.example.CleaningService.repositories;

import com.example.CleaningService.models.CleaningRequest;
import com.example.CleaningService.models.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
  List<Comment> findByCleaningRequest(CleaningRequest cleaningRequest);
}
