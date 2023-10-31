package com.example.CleaningService.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "discounts")
public class Discount {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @Column(name = "percentage")
  private double percentage;
}
