package com.telegram_giveaway_manager.telegram_giveaway_manager.application;

import com.telegram_giveaway_manager.telegram_giveaway_manager.model.Setting;
import com.telegram_giveaway_manager.telegram_giveaway_manager.repository.SettingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public final class Constants {
    private static SettingRepository settingRepository;

    @Autowired
    public Constants(SettingRepository settingRepository) {
        Constants.settingRepository = settingRepository;
    }

    public static Setting SETTING() {
        try {
            return settingRepository.findById(1).get();
        } catch (Exception e) {
            log.error("error getting setting with id 1");
            throw new RuntimeException(e);
        }
    }
}

