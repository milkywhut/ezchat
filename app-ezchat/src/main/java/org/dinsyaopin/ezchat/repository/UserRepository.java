package org.dinsyaopin.ezchat.repository;

import org.dinsyaopin.ezchat.model.User;

import java.util.List;

public interface UserRepository {

    List<User> getActiveUsers();

    List<String> getActiveUsersLogins();

    void add(User user);

    void remove(User user);

    List<User> getBySessionId(String sessionId);

    long countNumberOfUserSessions(String login);
}
