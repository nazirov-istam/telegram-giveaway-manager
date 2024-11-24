package com.telegram_giveaway_manager.telegram_giveaway_manager.service;

import com.telegram_giveaway_manager.telegram_giveaway_manager.application.Commands;
import com.telegram_giveaway_manager.telegram_giveaway_manager.application.MessageUz;
import com.telegram_giveaway_manager.telegram_giveaway_manager.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainService {
    private final UserService registerService;
    private final UserService userService;
    private final RpButtonService rpButtonService;
    private final CheckService checkService;
    private final SenderService senderService;
    private final HelpService helpService;
    private final InfoService infoService;
    private final ChannelService channelService;
    private final SettingsService settingsService;
    private final Map<Long, String> userStates = new HashMap<>();
    private final Map<Long, Map<String, String>> userResponses = new HashMap<>();

    public SendMessage mainBot(Update update) {
        SendMessage sendMessage = new SendMessage();
        if (update.hasMessage()) {
            Long chatId = update.getMessage().getChatId();
            String userText = update.getMessage().getText();
            sendMessage.setChatId(chatId);
            if (userStates.containsKey(chatId)) {
                handleUserInput(chatId, userText, sendMessage);
            } else {
                if (userText.equals(Commands.START)){
                    log.info("'Start' command starter.");
                    registerService.registerUser(update.getMessage());
                    userStates.put(chatId, "ASK_FIRST_NAME");
                    userResponses.put(chatId, new HashMap<>());
                    sendMessage.setText("Iltimos, ismingizni kiriting:");
                }
                else if (userText.equals(Commands.HELP)){
                    log.info("'Help' command starter.");
                    User user = userService.getCurrentUser(chatId);
                    if (user == null) {
                        registerService.registerUser(update.getMessage());
                    } else {
                        sendMessage = helpService.sendHelp(chatId);
                    }
                }
                else if (userText.equals(Commands.INFO)){
                    log.info("'Info' command starter.");
                    User user = userService.getCurrentUser(chatId);
                    if (user == null) {
                        registerService.registerUser(update.getMessage());
                    } else {
                        sendMessage = infoService.sendInfo(chatId);
                    }
                }
                 else if (userText.split(":")[0].equals(Commands.CHANNELFIRST)){
                    log.info("'Update setting channel first");
                    sendMessage = channelService.updateChannel(update.getMessage());

                }
                else if (userText.split(":")[0].equals(Commands.CHANNELLAST)){
                    log.info("'Update setting channel last");
                    sendMessage = channelService.updateChannel(update.getMessage());

                }
                else if (userText.split(":")[0].equals(Commands.SENDINGID)){
                    log.info("'Update setting Id owner");
                    sendMessage = settingsService.updateId(update.getMessage());

                }
                else {
                    sendMessage.setText("Unknown command. Please use /start or /help.");
                }
               /* switch (userText) {
                 *//*   case Commands.START -> {
                        log.info("'Start' command starter.");
                        registerService.registerUser(update.getMessage());
                        userStates.put(chatId, "ASK_FIRST_NAME");
                        userResponses.put(chatId, new HashMap<>());
                        sendMessage.setText("Iltimos, ismingizni kiriting:");
                    }*//*
                   *//* case Commands.HELP -> {
                        log.info("'Help' command starter.");
                        User user = userService.getCurrentUser(chatId);
                        if (user == null) {
                            registerService.registerUser(update.getMessage());
                        } else {
                            sendMessage = helpService.sendHelp(chatId);
                        }
                    }*//*
                  *//*  case Commands.INFO -> {
                        log.info("'Info' command starter.");
                        User user = userService.getCurrentUser(chatId);
                        if (user == null) {
                            registerService.registerUser(update.getMessage());
                        } else {
                            sendMessage = infoService.sendInfo(chatId);
                        }
                    }*//*
                    case Commands.CHANNELFIRST -> {
                        log.info("'Update setting channel first");
                        sendMessage = channelService.updateChannel(update.getMessage());
                    }
                    case Commands.CHANNELLAST -> {
                        log.info("'Update setting channel last");
                        sendMessage = channelService.updateChannel(update.getMessage());
                    }
                    case Commands.SENDINGID -> {
                        log.info("'Update setting Id owner");
                        sendMessage = settingsService.updateId(update.getMessage());
                    }
                    default -> {
                        sendMessage.setText("Unknown command. Please use /start or /help.");
                    }
                }*/
            }
        } else if (update.hasCallbackQuery()) {
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            sendMessage.setChatId(chatId);
            if (update.getCallbackQuery().getData().equals(Commands.JOIN_CHANNEL)) {
                if (checkService.checkChatMember(chatId)) {
                    log.info("true joined_channel");
                    sendMessage = rpButtonService.mainByLanguage(chatId);
                } else {
                    sendMessage = inButtonService.inlineKeyboardChannels(chatId, senderService.message("channelText", MessageUz.channelText));
                    log.info("false joined_channel");
                }
            }
        }
        return sendMessage;
    }

    private void handleUserInput(Long chatId, String userText, SendMessage sendMessage) {
        String currentState = userStates.get(chatId);
        Map<String, String> responses = userResponses.get(chatId);
        switch (currentState) {
            case "ASK_FIRST_NAME" -> {
                responses.put("firstName", userText);
                userStates.put(chatId, "ASK_LAST_NAME");
                sendMessage.setText("Iltimos, familiyangizni kiriting:\nNamuna: Nazirov Istam");
            }
            case "ASK_LAST_NAME" -> {
                responses.put("lastName", userText);
                userStates.put(chatId, "ASK_PHONE_NUMBER");
                sendMessage.setText("Iltimos, telefon raqamingizni kiriting:\nNamuna: +998-99-999-99-99");
            }
            case "ASK_PHONE_NUMBER" -> {
                responses.put("phoneNumber", userText);
                userStates.put(chatId, "ASK_INSTAGRAM_ID");
                sendMessage.setText("Iltimos, Instagram ID-ingizni kiriting:\nNamuna: @instagram_id");
            }
            case "ASK_INSTAGRAM_ID" -> {
                responses.put("instagramId", userText);
                userStates.put(chatId, "ASK_TICKET_NUMBER");
                sendMessage.setText("Iltimos, chipta raqamingizni kiriting:\nNamuna: 12345678");
            }
            case "ASK_TICKET_NUMBER" -> {
                responses.put("ticketNumber", userText);
                userStates.remove(chatId);
                saveToExcel(responses);
                userResponses.remove(chatId);
                sendMessage.setText(MessageUz.successText);
            }
        }
    }

    private void saveToExcel(Map<String, String> userData) {
        File file = new File("user_data.xlsx");
        Workbook workbook;
        Sheet sheet;
        try {
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                workbook = new XSSFWorkbook(fis);
                sheet = workbook.getSheetAt(0);
                fis.close();
            } else {
                workbook = new XSSFWorkbook();
                sheet = workbook.createSheet("User Data");
                Row headerRow = sheet.createRow(0);
                String[] headers = {"First Name", "Last Name", "Phone Number", "Instagram ID", "Ticket Number"};
                for (int i = 0; i < headers.length; i++) {
                    headerRow.createCell(i).setCellValue(headers[i]);
                }
            }

            int rowCount = sheet.getPhysicalNumberOfRows();
            Row dataRow = sheet.createRow(rowCount);
            dataRow.createCell(0).setCellValue(userData.get("firstName"));
            dataRow.createCell(1).setCellValue(userData.get("lastName"));
            dataRow.createCell(2).setCellValue(userData.get("phoneNumber"));
            dataRow.createCell(3).setCellValue(userData.get("instagramId"));
            dataRow.createCell(4).setCellValue(userData.get("ticketNumber"));

            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }
            workbook.close();
        } catch (IOException e) {
            log.error("Error saving to Excel: ", e);
        }
    }
}
