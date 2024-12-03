package com.telegram_giveaway_manager.telegram_giveaway_manager.application;

import com.telegram_giveaway_manager.telegram_giveaway_manager.controller.TelegramBot;
import com.telegram_giveaway_manager.telegram_giveaway_manager.model.Setting;
import com.telegram_giveaway_manager.telegram_giveaway_manager.repository.SettingRepository;
import com.telegram_giveaway_manager.telegram_giveaway_manager.service.MainService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.Basic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SettingsApplication {
    private final SettingRepository repository;

    @PostConstruct
    public void init() {
        log.info("init settings application");
        createSetting();
    }

    @Bean
    public DefaultBotOptions botOptions() {
        log.info("DefaultBotOptions initialized");
        return new DefaultBotOptions();
    }
    private void createSetting() {
        log.info("create setting");
        Setting setting = Setting.builder()
                .id(1)
                .channelId("-1001254163719")
                .channelUrl("https://t.me/Rokhat_Travel")
                .sendingId("734497784")
                .build();
        repository.save(setting);
        log.info("successfully created setting with id 1");
    }


}
