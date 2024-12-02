package com.telegram_giveaway_manager.telegram_giveaway_manager.service;

import com.telegram_giveaway_manager.telegram_giveaway_manager.model.User;
import com.telegram_giveaway_manager.telegram_giveaway_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void registerUser(Message message) {
        try {
            Optional<User> optional = userRepository.findById(message.getChatId());
            if (optional.isPresent()) {
                log.info("User with id {} already registered", message.getChatId());
                return;
            }
            User user = new User();
            user.setChatId(message.getChatId());
         //   user.setCreatedAt(LocalDateTime.now());
            user.setSuccess("true");
            log.info("User with id {} saved", message.getChatId());
            userRepository.save(user);
        } catch (Exception e) {
            log.error("Error registering user with id {}", message.getChatId(), e);
            throw new RuntimeException(e);
        }
    }

    public User getCurrentUser(Long chatId) {
        return userRepository.findById(chatId).orElse(null);
    }

    public void saveUser(User user) {
        try {
            userRepository.save(user);
        } catch (Exception e) {
            log.error("Error saving user with id {}", user.getChatId(), e);
        }
    }
}
