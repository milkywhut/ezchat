package org.dinsyaopin.ezchat.controller;

import org.dinsyaopin.ezchat.model.Message;
import org.dinsyaopin.ezchat.model.MessageWithoutPassword;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    @MessageMapping("/message")
    @SendTo("/chat/messages")
    public MessageWithoutPassword getMessage(Message message) throws Exception {
        System.out.println(message);
        return new MessageWithoutPassword(message.getLogin(), message.getMessage());
    }
}
