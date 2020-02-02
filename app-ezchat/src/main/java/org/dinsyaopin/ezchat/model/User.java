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
    private String firstName;
    private String lastName;
    private Type type;

}
