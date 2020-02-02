package org.dinsyaopin.ezchat.repository;

import org.dinsyaopin.ezchat.model.User;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private Set<User> users = Collections.synchronizedSet(new HashSet<>());

    @Override
    public List<User> getActiveUsers() {
        return users.parallelStream()
                .filter(distinctByLogin(User::getLogin))
                .collect(Collectors.toList());
    }

    private static <T> Predicate<T> distinctByLogin(Function<? super T, Object> loginExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(loginExtractor.apply(t), Boolean.TRUE) == null;
    }

    @Override
    public List<String> getActiveUsersLogins() {
        return users.parallelStream()
                .map(User::getLogin)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<User> getBySessionId(String sessionId) {
        return users.parallelStream()
                .filter(user -> Objects.equals(user.getSessionId(), sessionId))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public long countNumberOfUserSessions(String login) {
        return users.parallelStream()
                .filter(user -> Objects.equals(user.getLogin(), login))
                .count();
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
