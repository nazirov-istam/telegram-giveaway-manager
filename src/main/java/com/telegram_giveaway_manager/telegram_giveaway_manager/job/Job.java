package com.telegram_giveaway_manager.telegram_giveaway_manager.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class Job {
    @Scheduled(cron = "0 0 6 * * *", zone = "Asia/Tashkent")
    public void job() {
        // 6 утра
        System.out.println("Job started at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));


        /////// logic

    }
}
