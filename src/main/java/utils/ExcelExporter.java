package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.List;

public class ExcelExporter {

    public static void writeExcel(String fileName, String sheetName, List<String[]> data) {
        File file = new File(fileName);
        Workbook workbook;
        Sheet sheet;

        try {

            if (file.exists()) {
                FileInputStream fileIn = new FileInputStream(file);
                workbook = new XSSFWorkbook(fileIn);
                fileIn.close();
            } else {
                workbook = new XSSFWorkbook();
            }

            sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                sheet = workbook.createSheet(sheetName);
                // Ghi tiêu đề
                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < data.get(0).length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(data.get(0)[i]);
                    CellStyle style = workbook.createCellStyle();
                    Font font = workbook.createFont();
                    font.setBold(true);
                    style.setFont(font);
                    cell.setCellStyle(style);
                }
            }

            // Tìm dòng trống tiếp theo
            int rowNum = sheet.getLastRowNum() + 1;

            // Ghi dữ liệu vào dòng tiếp theo
            for (int i = 1; i < data.size(); i++) { // Bỏ qua dòng tiêu đề (data[0])
                Row row = sheet.createRow(rowNum++);
                String[] rowData = data.get(i);
                for (int j = 0; j < rowData.length; j++) {
                    row.createCell(j).setCellValue(rowData[j]);
                }
            }

            // Ghi dữ liệu vào file
            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
                System.out.println("✅ Dữ liệu đã được thêm vào file: " + fileName);
            }

            workbook.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}