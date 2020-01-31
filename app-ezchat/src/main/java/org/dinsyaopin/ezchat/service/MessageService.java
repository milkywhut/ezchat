package org.dinsyaopin.ezchat.service;

import java.util.List;

import org.dinsyaopin.ezchat.model.Message;
import org.dinsyaopin.ezchat.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> getMessages() {
        return messageRepository.getMessages();
    }

    public void add(Message message) {
        messageRepository.add(message);
    }
}
