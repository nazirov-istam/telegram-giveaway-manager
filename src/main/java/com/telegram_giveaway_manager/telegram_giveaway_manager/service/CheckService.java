package com.telegram_giveaway_manager.telegram_giveaway_manager.service;

import com.telegram_giveaway_manager.telegram_giveaway_manager.application.Constants;
import com.telegram_giveaway_manager.telegram_giveaway_manager.controller.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.objects.chatmember.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Service
public class CheckService {
    @Lazy
    @Autowired
    private TelegramBot bot;

    private boolean isSubscribed(String chatId, Long userId) {
        try {
            ChatMember chatMember = bot.execute(new GetChatMember(chatId, userId));

            if (chatMember instanceof ChatMemberAdministrator
                    || chatMember instanceof ChatMemberOwner
                    || chatMember instanceof ChatMemberRestricted
                    || chatMember instanceof ChatMemberMember
            ) {
                return true;
            } else if (chatMember instanceof ChatMemberLeft || chatMember instanceof ChatMemberBanned) {
                return false;
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkChatMember(Long userId) {
        return isSubscribed(Constants.SETTING().getChannel1Id(), userId) && isSubscribed(Constants.SETTING().getChannel2Id(), userId);
    }
}
