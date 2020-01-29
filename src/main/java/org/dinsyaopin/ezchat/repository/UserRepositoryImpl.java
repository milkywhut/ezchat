package org.dinsyaopin.ezchat.repository;

import org.dinsyaopin.ezchat.model.User;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private Set<User> users = Collections.synchronizedSet(new HashSet<>());

    @Override
    public Set<User> getActiveUsers() {
        return users;
    }

    @Override
    public List<String> getActiveUsersLogins() {
        return users.parallelStream().map(User::getLogin).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void add(User user) {
        users.add(user);
    }

    @Override
    public void remove(User user) {
        users.remove(user);
    }
}
