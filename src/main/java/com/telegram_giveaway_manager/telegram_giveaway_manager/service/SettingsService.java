package com.telegram_giveaway_manager.telegram_giveaway_manager.service;

import com.telegram_giveaway_manager.telegram_giveaway_manager.application.Commands;
import com.telegram_giveaway_manager.telegram_giveaway_manager.model.Setting;
import com.telegram_giveaway_manager.telegram_giveaway_manager.repository.SettingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SettingsService {
    private final SettingRepository repository;
    private final SettingRepository settingRepository;

    public SendMessage updateId(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        String request = message.getText();

        if (request.split(":")[0].equals(Commands.SENDINGID)) {
            log.info("start update sending id {}", request);
            Optional<Setting> optional = repository.findById(1);
            if (optional.isEmpty()) {
                log.info("settings is not found by id 1");
                sendMessage.setText("settings is not found by id 1");
                return sendMessage;
            }

            Setting byId = optional.get();
            String sendingId = byId.getSendingId() != null ? byId.getSendingId() : "null";
            log.info("settings id :{}, settings sending id :{}", 1, sendingId);

            String id = request.split(":")[1];
            byId.setSendingId(id);
            repository.save(byId);
            log.info("success updated sending id settings to :{}", id);
            sendMessage.setText("success updated sending id settings to :" + id);

        } else {
            log.info("............. {}", request);
            sendMessage.setText("this is :{} key word not found" + request.split(":")[0]);

        }
        return sendMessage;
    }

    public Optional<Setting> get() {
        try {
            return settingRepository.findById(1);
        }catch (Exception e){
            log.error("Setting is empty!");
            return Optional.empty();
        }
    }
}
