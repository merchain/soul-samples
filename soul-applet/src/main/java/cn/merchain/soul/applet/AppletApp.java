package cn.merchain.soul.applet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@SpringBootApplication
@EnableScheduling
public class AppletApp {

    public static void main(String[] args) {
        SpringApplication.run(AppletApp.class, args);
    }

}

