package com.team17.backend.Message.service;

import com.team17.backend.Message.model.Message;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
    
    public Message findMessageById(long id);
}
