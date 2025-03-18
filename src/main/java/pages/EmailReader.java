package pages;

import jakarta.mail.*;
import jakarta.mail.search.FlagTerm;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailReader {
    public static void main(String[] args) {
        String host = "imap.gmail.com";
        String username = "aidan@castalk.com";
        String password = "zpqd qgke loqb zapq"; // Dùng mật khẩu ứng dụng nếu là Gmail

        try {
            // Cấu hình kết nối IMAP
            Properties properties = new Properties();
            properties.put("mail.store.protocol", "imaps");
            properties.put("mail.imaps.host", host);
            properties.put("mail.imaps.port", "993");

            Session session = Session.getDefaultInstance(properties, null);
            Store store = session.getStore("imaps");
            store.connect(username, password);

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            // Lấy email mới nhất
            Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            if (messages.length > 0) {
                Message message = messages[messages.length - 1]; // Email mới nhất
                String content = message.getContent().toString();

                // Dùng regex để tìm mã OTP (6 chữ số)
                Pattern pattern = Pattern.compile("\\b\\d{6}\\b");
                Matcher matcher = pattern.matcher(content);
                if (matcher.find()) {
                    String otpCode = matcher.group();
                    System.out.println("✅ OTP: " + otpCode);
                } else {
                    System.out.println("⚠ Không tìm thấy mã OTP");
                }
            } else {
                System.out.println("⚠ Không có email nào mới!");
            }

            inbox.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
