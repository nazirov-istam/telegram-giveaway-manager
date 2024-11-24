package com.telegram_giveaway_manager.telegram_giveaway_manager.service;

import com.telegram_giveaway_manager.telegram_giveaway_manager.application.MessageUz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.lang.reflect.Field;

@Slf4j
@Service
@RequiredArgsConstructor
public class SenderService {
    public String message(String fieldName, String defaultText) {
        return getFieldValue(MessageUz.class, fieldName, defaultText);
    }

    private String getFieldValue(Class<?> clazz, String fieldName, String defaultValue) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            return (String) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }
}
