package com.example.CleaningService.models;

import jakarta.persistence.*;
import java.util.List;
import lombok.Data;

@Data
@Entity
@Table(name = "cleaning_requests")
public class CleaningRequest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "service_id")
  private Service service;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "employee_id")
  private Employee employee;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "inventory_id")
  private Inventory inventory;

  @Column(name = "dateTime")
  private String dateTime;

  @OneToMany(mappedBy = "cleaningRequest", cascade = CascadeType.ALL)
  private List<Comment> comments;

  @Column(name = "status")
  private String status;
}
