package com.telegram_giveaway_manager.telegram_giveaway_manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaRepositories
@SpringBootApplication
@EnableScheduling
public class TelegramGiveawayManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(TelegramGiveawayManagerApplication.class, args);
    }
}
