package org.dinsyaopin.ezchat.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dinsyaopin.ezchat.model.Message;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;

@Repository
public class MessageRepositoryImpl implements MessageRepository {

    private List<Message> messages = Collections.synchronizedList(new ArrayList<>());

    @Override
    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public void add(@NotNull Message message) {
        messages.add(message);
    }
}
