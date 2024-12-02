package com.telegram_giveaway_manager.telegram_giveaway_manager.service;

import com.telegram_giveaway_manager.telegram_giveaway_manager.model.User;
import com.telegram_giveaway_manager.telegram_giveaway_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExelService {
    private final UserRepository userRepository;

    public void generateExcelReport() throws IOException {
        // Извлекаем все данные пользователей из базы
        List<User> users = (List<User>) userRepository.findAll();

        // Создаем новый Workbook и лист
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Users");

        // Создаем заголовки столбцов
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Chat ID");
        headerRow.createCell(1).setCellValue("First Name");
        headerRow.createCell(2).setCellValue("Last Name");
        headerRow.createCell(3).setCellValue("Phone");
        headerRow.createCell(4).setCellValue("Instagram ID");
        headerRow.createCell(5).setCellValue("Ticket Number");
        headerRow.createCell(6).setCellValue("Success");

        // Заполняем данными из базы
        int rowNum = 1;
        for (User user : users) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(user.getChatId());
            row.createCell(1).setCellValue(user.getFirstName());
            row.createCell(2).setCellValue(user.getLastName());
            row.createCell(3).setCellValue(user.getPhone());
            row.createCell(4).setCellValue(user.getInstagramId());
            row.createCell(5).setCellValue(user.getTicketNumber());
            row.createCell(6).setCellValue(user.getSuccess());
        }

        // Создаем директорию для сохранения файла
        File dir = new File("src/main/resources/data/");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Указываем путь и сохраняем файл
        String filePath = dir.getAbsolutePath() + File.separator + "users_report.xlsx";
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        }
        workbook.close();

        System.out.println("Excel report generated successfully: " + filePath);
    }


    // Метод для удаления Excel-файла
    public boolean deleteExcelFile() {
        File file = new File("src/main/resources/users_report.xlsx");  // Указываем путь к файлу

        // Проверяем, существует ли файл
        if (file.exists()) {
            // Пытаемся удалить файл
            if (file.delete()) {
                System.out.println("Excel file deleted successfully.");
                return true;
            } else {
                System.err.println("Failed to delete the Excel file.");
                return false;
            }
        } else {
            System.err.println("File not found, nothing to delete.");
            return false;
        }
    }
}
