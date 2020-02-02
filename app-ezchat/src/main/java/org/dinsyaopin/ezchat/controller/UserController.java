package org.dinsyaopin.ezchat.controller;

import org.dinsyaopin.ezchat.model.User;
import org.dinsyaopin.ezchat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @MessageMapping("/users")
    @SendToUser("/users")
    public List<User> getActiveUsers() {
        return userService.getActiveUsers();
    }
}
