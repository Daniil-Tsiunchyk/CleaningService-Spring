package com.example.CleaningService.models;

import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  protected Long id;

  @Column(name = "name")
  protected String name;

  @Column(name = "login")
  protected String login;

  @Column(name = "password")
  protected String password;

  @Column(name = "email")
  protected String email;

  @Column(name = "role")
  protected String role;

  @OneToMany(mappedBy = "user")
  private List<CleaningRequest> cleaningRequests;

  @Column(name = "phone_number")
  protected String phoneNumber;

  @Column(name = "avatar_url")
  protected String avatarUrl;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return Objects.equals(id, user.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
