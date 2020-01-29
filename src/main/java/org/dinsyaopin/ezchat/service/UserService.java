package org.dinsyaopin.ezchat.service;

import org.dinsyaopin.ezchat.model.User;
import org.dinsyaopin.ezchat.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<String> getActiveUsersLogins() {
        return userRepository.getActiveUsersLogins();
    }

    public Set<User> getActiveUsers() {
        return userRepository.getActiveUsers();
    }

    public void add(@NotNull User user) {
        userRepository.add(user);
    }

    public void remove(@NotNull User user) {
        userRepository.remove(user);
    }
}
