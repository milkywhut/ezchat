package org.dinsyaopin.chatbot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    @JsonProperty("from")
    private String from;
    @JsonProperty("message")
    private String message;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("lastName")
    private String lastName;

    public Message(String from, String message) {
        this.from = from;
        this.message = message;
        this.firstName = "";
        this.lastName = "";
    }
}
