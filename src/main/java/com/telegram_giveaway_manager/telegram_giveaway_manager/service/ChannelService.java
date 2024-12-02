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
public class ChannelService {

    private final SettingRepository repository;

    public SendMessage updateChannel(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        String request = message.getText();
        log.info("start update channel settings {}", request);

        if (request.split(":")[0].equals(Commands.CHANNELFIRST)) {
            log.info("start update the channel settings {}", request);
            Optional<Setting> optional = repository.findById(1);
            if (optional.isEmpty()) {
                log.info("settings is not found by id 1");
                sendMessage.setText("settings is not found by id 1");
                return sendMessage;
            }

            Setting byId = optional.get();
            String channel1Url = byId.getChannelUrl() != null ? byId.getChannelUrl() : "null";
            String channel1Id = byId.getChannelId() != null ? byId.getChannelId() : "null";
            log.info("settings id :{}, settings the channel [url:id] :{}", 1, channel1Url.concat(":").concat(channel1Id));
            String url = request.split(":")[1];
            String id = request.split(":")[2];
            byId.setChannelUrl(url);
            byId.setChannelId(id);
            repository.save(byId);
            log.info("success updated first channel settings to :{}", url.concat(":").concat(id));
            sendMessage.setText("success updated the channel settings to :" + url.concat(":").concat(id));
        } else {
            log.info("Invalid command received: {}", request);
            sendMessage.setText("Invalid command: " + request.split(":")[0]);
        }

        return sendMessage;
    }
}
