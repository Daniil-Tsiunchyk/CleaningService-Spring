package com.example.CleaningService.repositories;

import com.example.CleaningService.models.CleaningRequest;
import com.example.CleaningService.models.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CleaningRequestRepository
  extends JpaRepository<CleaningRequest, Long> {
  List<CleaningRequest> findByUserAndStatus(User user, String status);

  List<CleaningRequest> findByUserAndStatusNot(User user, String status);
}
