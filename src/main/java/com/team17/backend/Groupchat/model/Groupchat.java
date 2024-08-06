package com.team17.backend.Groupchat.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Profile;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.team17.backend.Message.model.Message;
import com.team17.backend.Rental.model.Rental;
import com.team17.backend.User.model.User;

import jakarta.persistence.*;

@Entity
@Table(name = "groupchats")
public class Groupchat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @ManyToMany(mappedBy = "joined_groupchats", cascade = CascadeType.ALL)
    @JsonBackReference
    public List<User> users;

    @JsonManagedReference(value = "groupchat-messages")
    @OneToMany(mappedBy = "groupchat", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Message> messages;

    public Groupchat(User user) {
        this.users = new ArrayList<>();
        this.users.add(user);
    }

    public Groupchat() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<User> getUsers() {
        return this.users;
    }

    public List<User> addUser(User user) {
        this.users.add(user);
        return this.users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
