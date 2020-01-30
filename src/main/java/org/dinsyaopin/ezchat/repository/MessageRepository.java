package org.dinsyaopin.ezchat.repository;

import java.util.List;

import org.dinsyaopin.ezchat.model.Message;

public interface MessageRepository {

    List<Message> getMessages();

    void add(Message message);
}
