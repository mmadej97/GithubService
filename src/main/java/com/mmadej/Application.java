package com.mmadej;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    //before start you should initialize your environmental variables: GITHUB_USER and GITHUB_PASSWORD
    //application uses them to make basic authentication with github
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
