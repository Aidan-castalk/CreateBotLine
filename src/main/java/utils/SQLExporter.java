package utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class SQLExporter {

    public static void writeSQL(String fileName, List<String[]> botData) {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (String[] bot : botData) {
                String sql = String.format(
                        "INSERT INTO public.line_integration_bots (line_business_account, bot_name, basic_id, channel_id, share_link, qr_url, access_token, channel_secret, status, created_at, updated_at) VALUES (\n" +
                                "    '%s',\n" +
                                "    '%s',\n" +
                                "    '%s',\n" +
                                "    '%s',\n" +
                                "    'https://line.me/R/ti/p/%s',\n" +
                                "    'https://qr-official.line.me/gs/M_%s_GW.png',\n" +
                                "    '%s',\n" +
                                "    '%s',\n" +
                                "    1,\n" +
                                "    now(),\n" +
                                "    now()\n" +
                                ");\n\n",
                        bot[0], bot[1], bot[2], bot[3], bot[2], bot[2], bot[4], bot[5]
                );
                writer.write(sql);
            }
            System.out.println("✅ File SQL đã được tạo: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
