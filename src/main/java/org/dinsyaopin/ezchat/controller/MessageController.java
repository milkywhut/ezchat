package org.dinsyaopin.ezchat.controller;

import org.dinsyaopin.ezchat.model.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    @MessageMapping("/message")
    @SendTo("/chat/messages")
    public Message getMessage(Message message) {
        return new Message(message.getLogin(), message.getMessage());
    }
}
