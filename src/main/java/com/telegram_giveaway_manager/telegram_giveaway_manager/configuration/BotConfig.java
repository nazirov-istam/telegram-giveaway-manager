package com.telegram_giveaway_manager.telegram_giveaway_manager.configuration;

import com.telegram_giveaway_manager.telegram_giveaway_manager.application.BotProperties;
import com.telegram_giveaway_manager.telegram_giveaway_manager.controller.TelegramBot;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BotConfig {
  /*  @Autowired
    TelegramBot bot;

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }*/

    private final BotProperties botProperties;

    private final TelegramBot bot;

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