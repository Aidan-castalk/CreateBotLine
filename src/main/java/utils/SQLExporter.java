package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class SQLExporter {

    public static void writeSQL(String fileName, List<String[]> botData) {
        File file = new File(fileName);
        boolean fileExists = file.exists(); // Kiểm tra file đã tồn tại chưa

        try (FileWriter writer = new FileWriter(file, true)) { // Mở file ở chế độ append
            if (!fileExists) {
                // Nếu file chưa tồn tại, viết header hoặc dòng bắt đầu
                writer.write("-- SQL Insert Statements\n");
            }

            for (String[] bot : botData) {
                String sql = String.format("INSERT INTO bots (email, botName, basicId, channelId, accessToken, channelSecret) " +
                                "VALUES ('%s', '%s', '%s', '%s', '%s', '%s');\n",
                        bot[0], bot[1], bot[2], bot[3], bot[4], bot[5]);
                writer.write(sql);
            }
            writer.flush();
            System.out.println("✅ Dữ liệu đã được ghi vào " + fileName);
        } catch (IOException e) {
            System.out.println("❌ Lỗi khi ghi file SQL: " + e.getMessage());
        }
    }
}
