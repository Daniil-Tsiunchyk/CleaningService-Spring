package com.example.CleaningService.Models;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public List<CleaningRequest> getCleaningRequests() {
        return cleaningRequests;
    }

    public void setCleaningRequests(List<CleaningRequest> cleaningRequests) {
        this.cleaningRequests = cleaningRequests;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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