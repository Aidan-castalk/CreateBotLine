package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader {
    public static List<String> readBotNames(String fileName) {
        List<String> botNames = new ArrayList<>();
        try (InputStream is = ExcelReader.class.getClassLoader().getResourceAsStream(fileName);
             Workbook workbook = new XSSFWorkbook(is)) {
            if (is == null) {
                throw new IllegalArgumentException("File not found: " + fileName);
            }
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Bỏ qua header

                Cell cell = row.getCell(0); // Lấy cột đầu tiên

                if (cell != null) {
                    switch (cell.getCellType()) {
                        case STRING:
                            botNames.add(cell.getStringCellValue().trim());
                            break;
                        case NUMERIC:
                            botNames.add(String.valueOf((long) cell.getNumericCellValue())); // Chuyển số thành String
                            break;
                        default:
                            System.out.println("⚠ Cảnh báo: Ô dữ liệu không hợp lệ tại dòng " + row.getRowNum());
                            botNames.add(""); // Thêm chuỗi rỗng nếu ô không hợp lệ
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return botNames;
    }
}