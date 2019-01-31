package io.moyada.sharingan.repository.mysql.test;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Indexed;

@SpringBootApplication
@Indexed
public class Application  {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
