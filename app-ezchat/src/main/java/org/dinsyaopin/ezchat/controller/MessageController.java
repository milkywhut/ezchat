package org.dinsyaopin.ezchat.controller;

import java.util.List;

import org.dinsyaopin.ezchat.model.Message;
import org.dinsyaopin.ezchat.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    private MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @MessageMapping("/message")
    @SendTo("/chat/message")
    public Message getMessage(Message message) {
        messageService.add(message);
        return message;
    }

    @MessageMapping("/messages")
    @SendToUser("/chat/messages")
    public List<Message> getMessages() {
        return messageService.getMessages();
    }
}
