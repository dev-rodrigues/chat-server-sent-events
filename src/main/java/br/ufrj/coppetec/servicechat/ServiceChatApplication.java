package br.ufrj.coppetec.servicechat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ServiceChatApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceChatApplication.class, args);
    }

}
