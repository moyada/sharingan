package io.moyada.spring.boot.sharingan.test;

import io.moyada.spring.boot.sharingan.annotation.EnableSharinganMonitor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author xueyikang
 * @since 1.0
 **/
@EnableSharinganMonitor
@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
