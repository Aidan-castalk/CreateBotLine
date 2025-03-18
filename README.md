# Hướng dẫn cài đặt và chạy BotTest với Selenium và TestNG

## 1. Cài đặt Java 17

### **Trên Windows:**
1. **Tải JDK 17:** [Link tải](https://jdk.java.net/17/)
2. **Cài đặt:**
    - Chạy file `.exe` và làm theo hướng dẫn.
    - Sau khi cài đặt, thêm đường dẫn JDK vào biến môi trường:
        - Mở *Control Panel* → *System* → *Advanced system settings*.
        - Chọn *Environment Variables* → *System Variables*.
        - Thêm biến `JAVA_HOME` trỏ đến thư mục cài đặt JDK.
        - Cập nhật `Path` với `%JAVA_HOME%\bin`.
3. **Kiểm tra cài đặt:**
   ```sh
   java -version
   javac -version
   ```

### **Trên macOS/Linux:**
1. Cài đặt Brew nếu chưa có:
    Check xem brew đã được cài hay chưa bằng lệnh:
    brew --version
   ```sh
   /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
   ```
2. Cài đặt JDK 17 qua Homebrew:
   ```sh
   brew install openjdk@17
   ```
3. Cấu hình biến môi trường:
   ```sh
   echo 'export JAVA_HOME=$(brew --prefix openjdk@17)' >> ~/.zshrc
   echo 'export PATH="$JAVA_HOME/bin:$PATH"' >> ~/.zshrc
   source ~/.zshrc
   ```
4. Kiểm tra cài đặt:
   ```sh
   java -version
   ```

---

## 2. Cài đặt Maven

### **Trên Windows:**
1. **Tải Maven:** [Apache Maven](https://maven.apache.org/download.cgi)
2. **Cấu hình biến môi trường:**
    - Thêm biến `MAVEN_HOME` trỏ đến thư mục Maven.
    - Cập nhật `Path` với `%MAVEN_HOME%\bin`.
3. **Kiểm tra cài đặt:**
   ```sh
   mvn -version
   ```

### **Trên macOS/Linux:**
Cài đặt qua Homebrew:
```sh
brew install maven
```
Kiểm tra cài đặt:
```sh
mvn -version
```

---

## 3. Chạy file `BotTest.java`

### **Bước 1: Clone project về máy**
```sh
git clone https://github.com/Aidan-castalk/CreateBotLine.git
cd CreateBotLine
```

### **Bước 2: Cài đặt dependencies bằng Maven**
```sh
mvn clean install
```

### **Bước 3: Chạy test với TestNG**
Nếu có `testng.xml`, chạy lệnh:
```sh
mvn test
```
Nếu chạy trực tiếp file `BotTest.java`:
```sh
mvn exec:java -Dexec.mainClass="path.to.BotTest"
```
*(Thay `path.to.BotTest` bằng package đầy đủ của file `BotTest.java`)*

---



