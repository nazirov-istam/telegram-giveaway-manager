package com.telegram_giveaway_manager.telegram_giveaway_manager.job;

import com.telegram_giveaway_manager.telegram_giveaway_manager.application.BotProperties;
import com.telegram_giveaway_manager.telegram_giveaway_manager.model.Setting;
import com.telegram_giveaway_manager.telegram_giveaway_manager.service.ExelService;
import com.telegram_giveaway_manager.telegram_giveaway_manager.service.SenderService;
import com.telegram_giveaway_manager.telegram_giveaway_manager.service.SettingsService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class Job {
    @Autowired
    private BotProperties botProperties;
    @Autowired
    private SettingsService settingsService;
    @Autowired
    private SenderService senderService;

    @Scheduled(cron = "0 0 18 * * *", zone = "Asia/Tashkent")
    public void job() throws IOException, InterruptedException {
        Optional<Setting> setting = settingsService.get();
        if (setting.isEmpty()) {
            log.error("SSSSSSSSSSSSS");
        }
        Setting entity = setting.get();
        senderService.sendExcelReportToUser(Long.valueOf(entity.getSendingId()));
    }

    public void sendFile() {
        sendFileToTelegram("Qatnashuvchilar ro'yxati.xlsx");
    }


    public void saveToExcel(Map<String, String> userData) {
        File file = new File("Qatnashuvchilar ro'yxati.xlsx");
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
                String[] headers = {"Ism", "Familiya", "Telefon Raqam", "Instagram ID", "Chipta Raqam"};
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


    private void sendFileToTelegram(String filePath) {
        File excelFile = new File(filePath);
        if (!excelFile.exists()) {
            System.err.println("File not found: " + filePath);
            return;
        }

        OkHttpClient client = new OkHttpClient();
        MultipartBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("chat_id", "1386819485")
                .addFormDataPart("document", excelFile.getName(),
                        RequestBody.create(excelFile, MediaType.parse("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")))
                .build();

        Request request = new Request.Builder()
                .url("https://api.telegram.org/bot<" + botProperties.getToken() + ">/sendDocument")
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.body() != null && response.isSuccessful()) {
                System.out.println("File sent successfully! Response: " + response.body().string());
            } else {
                System.err.println("Failed to send file. Response: " + (response.body() != null ? response.body().string() : "No response body"));
            }
        } catch (IOException e) {
            System.err.println("Error sending file to Telegram: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
