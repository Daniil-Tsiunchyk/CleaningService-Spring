package com.example.CleaningService.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "comments")
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @Column(name = "content")
  private String content;

  @Column(name = "dateTime")
  private LocalDateTime dateTime;

  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(cascade = CascadeType.REMOVE)
  @JoinColumn(name = "cleaning_request_id")
  private CleaningRequest cleaningRequest;
}
