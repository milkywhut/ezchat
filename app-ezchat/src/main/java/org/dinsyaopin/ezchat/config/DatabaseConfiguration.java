package org.dinsyaopin.ezchat.config;

import org.springframework.beans.factory.annotation.Value;

import lombok.Getter;

@Getter
public class DatabaseConfiguration {

    @Value("${jdbc.Url}")
    private String jdbcUrl;
    @Value("${jdbc.User}")
    private String username;
    @Value("${jdbc.Password}")
    private String password;
    @Value("${jdbc.Driver:org.postgresql.Driver}")
    private String driverClassName;

}
