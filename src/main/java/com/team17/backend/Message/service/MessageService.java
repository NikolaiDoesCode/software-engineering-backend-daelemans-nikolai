package com.team17.backend.Message.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team17.backend.Groupchat.model.Groupchat;
import com.team17.backend.Groupchat.service.GroupchatService;
import com.team17.backend.Groupchat.service.GroupchatServiceException;
import com.team17.backend.Message.model.Message;
import com.team17.backend.User.model.User;
import com.team17.backend.User.service.UserService;
import com.team17.backend.User.service.UserServiceException;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupchatService groupchatService;

    public MessageService() {
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessage(long id) {
        Message message = messageRepository.findMessageById(id);
        return message;
    }

    public Message addMessage(Message message, String userEmail, long groupchatId)
            throws MessageServiceException, UserServiceException, GroupchatServiceException {
        User user = userService.getUser(userEmail);
        Groupchat groupchat = groupchatService.getGroupchatById(groupchatId);

        if (user == null) {
            throw new UserServiceException("User", "User with email %s does not exist".formatted(userEmail));
        }

        if (groupchat == null) {
            throw new GroupchatServiceException("Groupchat",
                    "Groupchat with id %s does not exist".formatted(groupchatId));
        }

        Boolean zitInDeChat = false;

        for (Groupchat userGroupchat : user.getJoinedGroupchats()) {
            if (userGroupchat == groupchat) {
                zitInDeChat = true;
            }
        }

        if (zitInDeChat == false) {
            throw new GroupchatServiceException("Groupchat",
                    "User is not in groupchat with id %s".formatted(groupchatId));
        }

        message.setTime(LocalDateTime.now());
        message.setUser(user);
        message.setGroupchat(groupchat);
        messageRepository.save(message);
        return message;
    }

}
