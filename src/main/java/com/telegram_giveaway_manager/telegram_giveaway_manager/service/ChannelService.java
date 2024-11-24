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
            log.info("start update first channel settings {}", request);
            Optional<Setting> optional = repository.findById(1);
            if (optional.isEmpty()){
               log.info("settings is not found by id 1");
               sendMessage.setText("settings is not found by id 1");
               return sendMessage;
            }
            Setting byId = optional.get();
            String channel1Url = byId.getChannel1Url() != null ? byId.getChannel1Url() : "null";
            String channel1Id = byId.getChannel1Id() != null ? byId.getChannel1Id() : "null";
            log.info("settings id :{}, settings first channel [url:id] :{}", 1, channel1Url.concat(":").concat(channel1Id));
            String url = request.split(":")[1];
            String id = request.split(":")[2];
            byId.setChannel1Url(url);
            byId.setChannel1Id(id);
            repository.save(byId);
            log.info("success updated first channel settings to :{}", url.concat(":").concat(id));
            sendMessage.setText("success updated first channel settings to :" + url.concat(":").concat(id));
        } else if (request.split(":")[0].equals(Commands.CHANNELLAST)) {
            log.info("start update last channel settings {}", request);
            Optional<Setting> optional = repository.findById(1);
            if (optional.isEmpty()){
                log.info("settings is not found by id 1");
                sendMessage.setText("settings is not found by id 1");
                return sendMessage;
            }

            Setting byId = optional.get();
            String channel2Url = byId.getChannel2Url() != null ? byId.getChannel2Url() : "null";
            String channel2Id = byId.getChannel2Id() != null ? byId.getChannel2Id() : "null";
            log.info("settings id :{}, settings last channel [url:id] :{}", 1, channel2Url.concat(":").concat(channel2Id));
            String url = request.split(":")[1];
            String id = request.split(":")[2];
            byId.setChannel2Url(url);
            byId.setChannel2Id(id);
            repository.save(byId);
            log.info("success updated last channel settings to :{}", url.concat(":").concat(id));
            sendMessage.setText("success updated last channel settings to :" + url.concat(":").concat(id));

        } else {
            log.info("............. {}", request);
            sendMessage.setText("this is :{} key word not found" + request.split(":")[0]);
        }
        return sendMessage;
    }
}
