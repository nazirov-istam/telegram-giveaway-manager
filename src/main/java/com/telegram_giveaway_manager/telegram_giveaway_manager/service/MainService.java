package com.telegram_giveaway_manager.telegram_giveaway_manager.service;

import com.telegram_giveaway_manager.telegram_giveaway_manager.application.Commands;
import com.telegram_giveaway_manager.telegram_giveaway_manager.application.MessageUz;
import com.telegram_giveaway_manager.telegram_giveaway_manager.job.Job;
import com.telegram_giveaway_manager.telegram_giveaway_manager.model.Setting;
import com.telegram_giveaway_manager.telegram_giveaway_manager.model.User;
import com.telegram_giveaway_manager.telegram_giveaway_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainService {
    private final UserService registerService;
    private final UserService userService;
    private final CheckService checkService;
    private final SenderService senderService;
    private final HelpService helpService;
    private final InfoService infoService;
    private final ChannelService channelService;
    private final SettingsService settingsService;
    private final UserRepository userRepository;
    private final Job job;

    public SendMessage mainBot(Update update) throws IOException, InterruptedException {
        SendMessage sendMessage = new SendMessage();


        if (update.hasMessage()) {
            long chatId = update.getMessage().getChatId();
            String userText = update.getMessage().getText();
            sendMessage.setChatId(chatId);

            User user = userService.getCurrentUser(chatId);
            if (userText.equals(Commands.HELP)) {
                log.info("'Help' command starter.");
                if (!userRepository.existsById(chatId)) {
                    registerService.registerUser(update.getMessage());
                } else {
                    sendMessage = helpService.sendHelp(chatId);
                }
            } else if (userText.equals(Commands.INFO)) {
                log.info("'Info' command starter.");
                if (!userRepository.existsById(chatId)) {
                    registerService.registerUser(update.getMessage());
                } else {
                    sendMessage = infoService.sendInfo(chatId);
                }
            } else if (userText.equals("/download")) {
                Optional<Setting> setting = settingsService.get();
                if (setting.isEmpty()){
                    log.warn("'setting is null or empty");
                    return sendMessage;
                }
                Setting entity = setting.get();
                senderService.sendExcelReportToUser(Long.valueOf(entity.getSendingId()));
               // exelService.deleteExcelFile();
                sendMessage.setChatId(Long.valueOf(entity.getSendingId()));
                sendMessage.setText("success sending");
                return sendMessage;
            } else if (user != null && user.getStep() != null && user.getStep().equals("SUCCESS")) {
                sendMessage.setText(MessageUz.userAlreadyExist);
                return sendMessage;
            } else if (userText.equals(Commands.START)) {
                userService.registerUser(update.getMessage());
                if (!checkService.checkChatMember(chatId)) {
                    sendMessage = inButtonService.inlineKeyboardChannels(chatId, MessageUz.entryMessage + MessageUz.channelText);
                } else {
                    sendMessage = handleUserInput(chatId);
                }
            } else if (user.getStep() != null && user.getStep().equals("ASK_FIRST_NAME")) {
                user.setFirstName(userText);
                userService.saveUser(user);
                sendMessage = handleUserInput(chatId);
            } else if (user.getStep() != null && user.getStep().equals("ASK_LAST_NAME")) {
                user.setLastName(userText);
                userService.saveUser(user);
                sendMessage = handleUserInput(chatId);
            } else if (user.getStep() != null && user.getStep().equals("ASK_PHONE_NUMBER")) {
                user.setPhone(userText);
                userService.saveUser(user);
                sendMessage = handleUserInput(chatId);

            } else if (user.getStep() != null && user.getStep().equals("ASK_INSTAGRAM_ID")) {
                user.setInstagramId(userText);
                userService.saveUser(user);
                sendMessage = handleUserInput(chatId);

            } else if (user.getStep() != null && user.getStep().equals("ASK_TICKET_NUMBER")) {
                user.setTicketNumber(userText);
                userService.saveUser(user);
                sendMessage = handleUserInput(chatId);
            } else if (user.getStep().equals("SUCCESS")) {
                sendMessage.setText(MessageUz.successText);
                return sendMessage;
            } else if (userText.split(":")[0].equals(Commands.CHANNELFIRST)) {
                log.info("'Update setting channel first");
                sendMessage = channelService.updateChannel(update.getMessage());

            } else if (userText.split(":")[0].equals(Commands.SENDINGID)) {
                log.info("'Update setting Id owner");
                sendMessage = settingsService.updateId(update.getMessage());
            } else {
                sendMessage.setText("Unknown command. Please use /start or /help.");
            }

        } else if (update.hasCallbackQuery()) {
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            sendMessage.setChatId(chatId);
            if (update.getCallbackQuery().getData().equals(Commands.JOIN_CHANNEL)) {
                if (checkService.checkChatMember(chatId)) {
                    log.info("true joined_channel");
                    sendMessage = handleUserInput(chatId);
                } else {
                    sendMessage = inButtonService.inlineKeyboardChannels(chatId, senderService.message("channelText", MessageUz.channelText));
                    log.info("false joined_channel");
                }
            }

            return sendMessage;
        }
        return sendMessage;
    }

    private SendMessage handleUserInput(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        User user = userService.getCurrentUser(chatId);
        if (user.getStep() != null && user.getStep().equals("SUCCESS")) {
            sendMessage.setText("error");
        } else if (user.getFirstName() == null) {
            user.setStep("ASK_FIRST_NAME");
            sendMessage.setText("\uD83D\uDCCB Iltimos, ismingizni kiriting:\n" +
                    "Namuna: Sardor");
        } else if (user.getLastName() == null) {
            user.setStep("ASK_LAST_NAME");
            sendMessage.setText("\uD83D\uDCCB Iltimos, familiyangizni kiriting:\n" +
                    "Namuna: Sardorov");
        } else if (user.getPhone() == null) {
            user.setStep("ASK_PHONE_NUMBER");
            sendMessage.setText("\uD83D\uDCDE Endi telefon raqamingizni kiriting:\n" +
                    "Namuna: +998901234567");
        } else if (user.getInstagramId() == null) {
            user.setStep("ASK_INSTAGRAM_ID");
            sendMessage.setText("\uD83D\uDCF7 Instagram profilingizni ID yoki username tarzida kiriting \n" +
                    "Namuna: @username");
        } else if (user.getTicketNumber() == null) {
            user.setStep("ASK_TICKET_NUMBER");
            sendMessage.setText("\uD83D\uDD22 Iltimos, bilet raqamingizni kiriting:  \n" +
                    "Namuna: 12345678");
        } else if (user.getFirstName() != null
                && user.getLastName() != null
                && user.getPhone() != null
                && user.getInstagramId() != null
                && user.getTicketNumber() != null
        ) {
            user.setStep("SUCCESS");
            Map<String, String> userData = new HashMap<>();
            userData.put("firstName", user.getFirstName());
            userData.put("lastName", user.getLastName());
            userData.put("phoneNumber", user.getPhone());
            userData.put("instagramId", user.getInstagramId());
            userData.put("ticketNumber", user.getTicketNumber());
          //  job.saveToExcel(userData);
            sendMessage.setText(MessageUz.successText);
        }
        userService.saveUser(user);
        return sendMessage;
    }
}
