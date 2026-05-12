package org.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelWriter {

    public static void markAttendance(String name, int id) {

        File file = new File("attendance.xlsx");

        try {

            Workbook workbook;
            Sheet sheet;


            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                workbook = WorkbookFactory.create(fis);
                sheet = workbook.getSheetAt(0);
                fis.close();
            } else {
                workbook = new XSSFWorkbook();
                sheet = workbook.createSheet("Attendance");

                Row h = sheet.createRow(0);
                h.createCell(0).setCellValue("Name");
                h.createCell(1).setCellValue("UserID");
                h.createCell(2).setCellValue("Date");
                h.createCell(3).setCellValue("Time");
            }

            String date =
                    new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            String time =
                    new SimpleDateFormat("HH:mm").format(new Date());


            int rowNum = sheet.getLastRowNum() + 1;
            Row row = sheet.createRow(rowNum);

            row.createCell(0).setCellValue(name);
            row.createCell(1).setCellValue(id);
            row.createCell(2).setCellValue(date);
            row.createCell(3).setCellValue(time);


            FileOutputStream fos = new FileOutputStream(file);
            workbook.write(fos);
            fos.flush();
            fos.close();
            workbook.close();

            System.out.println("Attendance saved OK");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
