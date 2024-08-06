package com.team17.backend.Groupchat.service;
import org.springframework.data.jpa.repository.JpaRepository;

import com.team17.backend.Groupchat.model.Groupchat;

import java.util.List;

public interface GroupchatRepository extends JpaRepository<Groupchat, Long> {
    public Groupchat findGroupchatById(Long id);
}