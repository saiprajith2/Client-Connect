package com.sails.client_connect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class ClientConnectApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientConnectApplication.class, args);
    }

}
