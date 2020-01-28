package org.dinsyaopin.ezchat.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Message {

    private String login;
    private String password;
    private String message;
}
