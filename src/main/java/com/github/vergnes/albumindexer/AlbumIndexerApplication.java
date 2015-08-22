package com.github.vergnes.albumindexer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AlbumIndexerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlbumIndexerApplication.class, args);
    }
}
