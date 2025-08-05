package org.example.miroom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MiroomApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiroomApplication.class, args);
    }

}
