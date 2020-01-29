package org.dinsyaopin.ezchat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    private String login;
    private String password;
    private String message;

    public Message(String login, String message) {
        this.login = login;
        this.message = message;
    }
}
