package org.dinsyaopin.ezchat.repository;

import org.dinsyaopin.ezchat.model.User;

import java.util.List;
import java.util.Set;

public interface UserRepository {

    Set<User> getActiveUsers();

    List<String> getActiveUsersLogins();

    void add(User user);

    void remove(User user);
}
