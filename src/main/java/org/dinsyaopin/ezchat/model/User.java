package org.dinsyaopin.ezchat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String login;
    private String sessionId;
    private Type type;

    public User(String login, Type type) {
        this.login = login;
        this.type = type;
    }

    public User(String login, String sessionId) {
        this.login = login;
        this.sessionId = sessionId;
    }
}
