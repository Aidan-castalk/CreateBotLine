package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseUtils {

    private static final String DB_URL = "jdbc:postgresql://cheercast-postgre-uat.c7a2cgwkkz46.ap-northeast-1.rds.amazonaws.com:5432/cc_backend";
    private static final String DB_USER = "aidan";
    private static final String DB_PASSWORD = "Aik5elilea7Eweib";

    public static void insertBotData(String email, String name, String basicId, String channelId,
                                     String accessToken, String channelSecret) {
        String sql = "INSERT INTO public.line_integration_bots " +
                "(line_business_account, bot_name, basic_id, channel_id, share_link, qr_url, " +
                "access_token, channel_secret, status, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 1, now(), now())";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            pstmt.setString(2, name);
            pstmt.setString(3, basicId);
            pstmt.setString(4, channelId);
            pstmt.setString(5, "https://line.me/R/ti/p/" + basicId);
            pstmt.setString(6, "https://qr-official.line.me/gs/M_" + basicId + "_GW.png");
            pstmt.setString(7, accessToken);
            pstmt.setString(8, channelSecret);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("✅ Dữ liệu đã được chèn thành công!");
            }

        } catch (SQLException e) {
            System.err.println("❌ Lỗi khi chèn dữ liệu vào PostgreSQL: " + e.getMessage());
        }
    }
}
