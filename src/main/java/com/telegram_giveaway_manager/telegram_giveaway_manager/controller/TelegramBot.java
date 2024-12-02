package com.telegram_giveaway_manager.telegram_giveaway_manager.controller;

import com.telegram_giveaway_manager.telegram_giveaway_manager.application.BotProperties;
import com.telegram_giveaway_manager.telegram_giveaway_manager.service.MainService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


@Configuration
@Slf4j
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {
    private final BotProperties botProperties;
    private final MainService mainService;

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        try {
            SendMessage response = mainService.mainBot(update);
            if (response != null) {
                execute(response);
            } else {
                log.warn("main service returned null");
                System.err.println("main service returned null");
            }
        } catch (Exception e) {
            log.error("Error occurred while processing update: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return botProperties.getUsername();
    }

    @Override
    public String getBotToken() {
        return botProperties.getToken();
    }
}
