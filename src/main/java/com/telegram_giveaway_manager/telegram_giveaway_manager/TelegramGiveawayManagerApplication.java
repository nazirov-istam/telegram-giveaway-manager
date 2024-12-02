package com.telegram_giveaway_manager.telegram_giveaway_manager;

import com.telegram_giveaway_manager.telegram_giveaway_manager.application.BotProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaRepositories
@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(BotProperties.class)
public class TelegramGiveawayManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(TelegramGiveawayManagerApplication.class, args);
    }
}
