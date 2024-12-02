package com.telegram_giveaway_manager.telegram_giveaway_manager.service;

import com.telegram_giveaway_manager.telegram_giveaway_manager.application.Commands;
import com.telegram_giveaway_manager.telegram_giveaway_manager.application.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class inButtonService {
    public static SendMessage inlineKeyboardChannels(long chatId, String text) {
        try {
            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
            InlineKeyboardButton button1 = new InlineKeyboardButton();
            InlineKeyboardButton button2 = new InlineKeyboardButton();
            button1.setText(" ➕ Kanal ➕ ");
            button1.setUrl(Constants.SETTING().getChannelUrl());
            button2.setText(text.split("00001")[1]);
            button2.setCallbackData(Commands.JOIN_CHANNEL);
            markupInline.setKeyboard(Arrays.asList(
                    List.of(button1),
                    List.of(button2)
            ));
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText(text.split("00001")[0]);
            message.setReplyMarkup(markupInline);
            return message;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
