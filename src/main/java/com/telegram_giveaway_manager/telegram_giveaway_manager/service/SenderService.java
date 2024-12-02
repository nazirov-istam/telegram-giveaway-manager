package com.telegram_giveaway_manager.telegram_giveaway_manager.service;

import com.telegram_giveaway_manager.telegram_giveaway_manager.application.MessageUz;
import com.telegram_giveaway_manager.telegram_giveaway_manager.controller.TelegramBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

@Slf4j
@Service
public class SenderService {
    @Lazy
    @Autowired
    private  TelegramBot telegramBot;
    @Autowired
    private  ExelService exelService;
    public String message(String fieldName, String defaultText) {
        return getFieldValue(fieldName, defaultText);
    }

    private String getFieldValue(String fieldName, String defaultValue) {
        try {
            Field field = MessageUz.class.getDeclaredField(fieldName);
            return (String) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }


    public void sendExcelReportToUser(Long chatId) throws IOException, InterruptedException {
        exelService.generateExcelReport();
        Thread.sleep(10000);
        File file = new File("src/main/resources/data/" + "users_report.xlsx");

        if (file.exists()) {
            InputFile inputFile = new InputFile(file);

            SendDocument sendDocument = new SendDocument();
            sendDocument.setChatId(chatId.toString());
            sendDocument.setDocument(inputFile);

            try {
                telegramBot.execute(sendDocument);  // Отправляем файл через TelegramBot
                System.out.println("File sent to user: " + chatId);

                // После отправки удаляем файл
                if (file.delete()) {
                    System.out.println("Excel file deleted successfully.");
                } else {
                    System.err.println("Failed to delete the Excel file.");
                }
            } catch (TelegramApiException e) {
                e.printStackTrace();
                System.err.println("Failed to send file to user.");
            }
        } else {
            System.err.println("Excel file not found.");
        }
        exelService.deleteExcelFile();
    }

}
