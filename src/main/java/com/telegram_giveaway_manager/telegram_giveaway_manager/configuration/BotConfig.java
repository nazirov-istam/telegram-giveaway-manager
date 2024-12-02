package com.telegram_giveaway_manager.telegram_giveaway_manager.configuration;

import com.telegram_giveaway_manager.telegram_giveaway_manager.application.BotProperties;
import com.telegram_giveaway_manager.telegram_giveaway_manager.controller.TelegramBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BotConfig {

    private final BotProperties botProperties;

    private final TelegramBot bot;

    @Bean
    public TelegramBot telegramBot(DefaultBotOptions botOptions) {
        return new TelegramBot(botProperties, bot, botOptions);
    }

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        if (botProperties.getToken() == null || botProperties.getToken().isEmpty() ||
                botProperties.getUsername() == null || botProperties.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Bot token and username can't be empty");
        }

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

}
