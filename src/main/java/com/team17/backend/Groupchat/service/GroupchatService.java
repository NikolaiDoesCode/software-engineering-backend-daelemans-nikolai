package com.team17.backend.Groupchat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team17.backend.Groupchat.model.Groupchat;
import com.team17.backend.User.model.User;
import com.team17.backend.User.service.UserRepository;
import com.team17.backend.User.service.UserService;
import com.team17.backend.User.service.UserServiceException;

@Service
public class GroupchatService {

    public GroupchatService() {
    }

    @Autowired
    private GroupchatRepository groupchatRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    public List<Groupchat> getAllGroupchats() {
        return groupchatRepository.findAll();
    }

    public Groupchat getGroupchatById(long id) {
        return groupchatRepository.findGroupchatById(id);
    }

    public List<User> getUsersOfGroupChat(long id) throws GroupchatServiceException {
        Groupchat groupchat = groupchatRepository.findGroupchatById(id);

        if (groupchat == null) {
            throw new GroupchatServiceException("Groupchat", "Groupchat with id %s does not exist".formatted(id));
        }

        return groupchat.getUsers();
    }

    public Groupchat createGroupchat() throws UserServiceException {
        Groupchat groupchat = new Groupchat();
        Groupchat nieuwe_groupchat = groupchatRepository.save(groupchat);

        return nieuwe_groupchat;
    }

    public Groupchat addUserToGroupChat(Long id, String email) throws GroupchatServiceException, UserServiceException {

        User user = userService.getUser(email);

        Groupchat groupchat = groupchatRepository.findGroupchatById(id);

        if (groupchat == null) {
            throw new GroupchatServiceException("Groupchat", "Groupchat with id %s does not exist".formatted(id));
        }

        for (Groupchat userGroupchat : user.getJoinedGroupchats()) {
            if (userGroupchat == groupchat) {
                throw new GroupchatServiceException("Groupchat", "User already in groupchat");
            }
        }

        groupchat.addUser(user);
        user.addJoinedGroupchat(groupchat);
        Groupchat nieuwe_groupchat = groupchatRepository.save(groupchat);
        userRepository.save(user);
        return nieuwe_groupchat;
    }
}
