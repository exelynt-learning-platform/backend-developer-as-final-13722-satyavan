package com.resourcebookingsystem;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class ResourceBookingSystemApplication {

    public static void main(String[] args) {
        // .env file load and set to system properties
        Dotenv dotenv=Dotenv.configure().ignoreIfMissing().load();
        dotenv.entries().forEach(entry->System.setProperty(entry.getKey(),entry.getValue()));
        SpringApplication.run(ResourceBookingSystemApplication.class, args);
    }

}
