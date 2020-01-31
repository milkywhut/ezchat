package org.dinsyaopin.chatbot.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.UniqueElements;

import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "userlink")
@Data
@ToString
public class UserLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "chat_user", unique = true)
    String chatUser;
    @Column(name = "link")
    String link;

    public UserLink() {

    }

    public UserLink(String link, String login) {
        this.link = link;
        this.chatUser = login;
    }
}
