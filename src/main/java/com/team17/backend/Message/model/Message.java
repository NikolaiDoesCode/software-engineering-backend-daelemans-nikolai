package com.team17.backend.Message.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.team17.backend.Groupchat.model.Groupchat;
import com.team17.backend.User.model.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "messages")
public class Message {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public long id;

    @NotBlank(message = "Text is required")
    private String text;

    @NotNull(message = "time is required")
    private LocalDateTime time;

    @JsonBackReference(value = "groupchat-messages")
    @ManyToOne
    @JoinColumn(name = "groupchat_id", nullable = false)
    private Groupchat groupchat;

    @JsonBackReference(value = "user-messages")
    @ManyToOne
    @JoinColumn(name = "user_email", nullable = false)
    private User user;

    public Message() {
    }

    public Message(String text) {
        this.text = text;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getTime() {
        return this.time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Groupchat getGroupchat() {
        return this.groupchat;
    }

    public void setGroupchat(Groupchat groupchat) {
        this.groupchat = groupchat;
    }
}
