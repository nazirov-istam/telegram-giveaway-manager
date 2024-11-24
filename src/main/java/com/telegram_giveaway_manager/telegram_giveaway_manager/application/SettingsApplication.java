package com.telegram_giveaway_manager.telegram_giveaway_manager.application;

import com.telegram_giveaway_manager.telegram_giveaway_manager.model.Setting;
import com.telegram_giveaway_manager.telegram_giveaway_manager.repository.SettingRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
@Slf4j
@Configuration
@RequiredArgsConstructor
public class SettingsApplication {
    private final SettingRepository repository;

    @PostConstruct
    public void init(){
      log.info("init settings application");
      createSetting();
    }

    private void createSetting() {
        log.info("create setting");
        Setting setting = Setting.builder()
                .id(1)
                .build();
        repository.save(setting);
        log.info("successfully created setting with id 1");
    }


}
