package com.example.sesacrunback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SesacrunBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(SesacrunBackApplication.class, args);
    }

}
