package org.dinsyaopin.ezchat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class MessageWithoutPassword {

    private String login;
    private String message;
}
