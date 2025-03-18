package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.DatabaseUtils;
import utils.ExcelExporter;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BotTest {
    private static WebDriver driver;
    private String passWord;
    private  String email;
    private String provider;
    private String botName;
    private String basicId;
    private String channelId;
    private String accessToken;
    private String channelSecret;
    private static int runCount = 0;

    @BeforeClass
    public void setup() throws InterruptedException {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
        email = "dannt.c25@gmail.com";
        passWord = "Tiendan2109@";
        botName = generateBotName();
        provider = "AidanTest1";
    }
    @DataProvider(name = "runMultipleTimes")
    public Object[][] createTestData() {
        return new Object[2][0]; // Chạy 10 lần
    }

    @Test(dataProvider = "runMultipleTimes")
    public void runBotTests() {
        try {
            botName = generateBotName();
            if (runCount == 0) {
                LoginPage1();
                runCount++;
            } else {
                driver.get("https://manager.line.biz/");
            }
            LoginPage();
            createBotLine();
            insertBotDataIntoDatabase();
            testExportToExcel();

        } catch (Exception e) {
            System.out.println("⚠ Lỗi xảy ra nhưng test vẫn tiếp tục: " + e.getMessage());
        }
    }
    public void LoginPage1() {
        driver.get("https://manager.line.biz/");
        Actions actions = new Actions(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[normalize-space()='Log in with business account']"))).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@placeholder='Email address']"))).sendKeys(email);
        driver.findElement(By.xpath("//input[@placeholder='Password']")).sendKeys(passWord);
        driver.findElement(By.xpath("//button[normalize-space()='Log in']")).click();
    }
    public void LoginPage() throws InterruptedException {
//        driver.get("https://manager.line.biz/");
        Actions actions = new Actions(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement buttonLine = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Create new')]")));
        buttonLine.click();
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(tabs.size() - 1));
        try {
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[normalize-space()='Log in']")));
            loginButton.click();
            System.out.println("✅ Đã nhấn vào nút 'Log in'");
        } catch (TimeoutException e) {
            System.out.println("⚠ Không tìm thấy nút 'Log in', tiếp tục chạy các bước tiếp theo...");
        }
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='bot.name']"))).sendKeys(botName);
        selectByValue(By.xpath("//select[@name='legalCountryCode']"), "JP");
        driver.findElement(By.xpath("//input[@name='account.name']")).sendKeys("Castalk");
        selectByValue(By.xpath("//select[@name='category_group']"), "2307");
        Thread.sleep(2000);
        selectByValue(By.xpath("//select[@name='category']"), "42841");
        Thread.sleep(2000);
        driver.findElement(By.xpath("//button[normalize-space()='Continue']")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Submit']"))).click();

        WebElement basicIdElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//th[text()='Basic ID']/following-sibling::td/span")));
        basicId = basicIdElement.getText();
        System.out.println("✅ Basic ID: " + basicId);
        String url = "https://uat-cc-backend.castalk.com/api/public/v1/line_integration/message//";
        String modifiedUrl = url.replaceAll("(.*)//$", "$1/" + basicId + "/");
        System.out.println("✅ URL mới: " + modifiedUrl);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[normalize-space()='Go to LINE Official Account Manager']"))).click();
        try {
            WebElement closeButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@class='close']")));

            if (closeButton.isDisplayed()) {
                closeButton.click();
                System.out.println("✅ Đã đóng popup thành công!");
            }
        } catch (Exception e) {
            System.out.println("⚠ Không tìm thấy popup, tiếp tục chạy bước tiếp theo.");
        }
        try {
            WebElement closeButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@class='btn btn-secondary' and text()='Close']")));

            if (closeButton.isDisplayed()) {
                closeButton.click();
                System.out.println("✅ Đã đóng popup thành công!");
            }
        } catch (Exception e) {
            System.out.println("⚠ Không tìm thấy popup, tiếp tục chạy bước tiếp theo.");
        }

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[normalize-space()='Settings']"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Messaging API')]"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Enable Messaging API']"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[span[text()='AidanTest1']]"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Agree']"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='OK']"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='OK']"))).click();
        Thread.sleep(4000);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement webhookInput = driver.findElement(By.xpath("//input[@name='webhookEndpoint']"));
        webhookInput.click();
        webhookInput.sendKeys(modifiedUrl);
        webhookInput.sendKeys(Keys.TAB);
        actions.sendKeys(Keys.ENTER).perform();
        WebElement ChanelID = driver.findElement(By.xpath("//div[contains(text(),'Channel ID')]/following-sibling::div[1]"));
        channelId = ChanelID.getText();
        System.out.println("✅ Channel ID: " + channelId);
        WebElement ChaneSecret = driver.findElement(By.xpath("//div[contains(text(), 'Channel secret')]/following-sibling::div[1]"));
        channelSecret = ChaneSecret.getText();
        System.out.println("✅ Channel Secret: " + channelSecret);
        driver.findElement(By.xpath("//span[contains(text(),'Response settings')]")).click();
        WebElement switchLabel = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[name='welcomeMessage'] + label.switch-label")));
        switchLabel.click();
        WebElement switchLabel2 = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[name='webhook'] + label.switch-label")));
        switchLabel2.click();
        WebElement switchLabel3 = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[name='autoResponse'] + label.switch-label")));
        switchLabel3.click();
    }

    //@Test(dependsOnMethods = "LoginPage")
    public void createBotLine() throws InterruptedException {
        driver.get("https://developers.line.biz/console/");
        Actions actions = new Actions(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        String botNameProvider = "//h3[normalize-space()='" + botName + "']";
        WebElement providerElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@class='dc-provider-name dc-provider-link dc-provider-link']//span[@class='highlighter'][normalize-space()='" + provider + "']")));
        providerElement.click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(botNameProvider))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Messaging API']"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Issue']"))).click();

        WebElement tokenElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class, 'token-row')]//span"))
        );
        accessToken = tokenElement.getText();
        System.out.println("Token: " + accessToken);
    }
    //@Test(dependsOnMethods = "createBotLine")
    public void insertBotDataIntoDatabase() {
        DatabaseUtils.insertBotData(email, botName, basicId, channelId, accessToken, channelSecret);
    }

    //@Test(dependsOnMethods = "insertBotDataIntoDatabase")
    public void testExportToExcel() {
        String fileName = "line_bots.xlsx";
        String sheetName = "Line Bots";
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"Basic ID", "Channel ID", "Channel Secret", "Access Token", "Email"}); // Header
        data.add(new String[]{basicId, channelId, channelSecret, accessToken, email});
        ExcelExporter.writeExcel(fileName, sheetName, data);
    }

    public void selectByValue(By locator, String value) {
        try {
            WebElement dropdown = driver.findElement(locator);
            Select select = new Select(dropdown);
            select.selectByValue(value);
            System.out.println("✅ Đã chọn: " + value);
        } catch (Exception e) {
            System.out.println("⚠ Lỗi khi chọn dropdown: " + e.getMessage());
        }
    }
    public static String generateBotName() {
        String randomUUID = UUID.randomUUID().toString().replaceAll("[^0-9]", "").substring(0, 4);
        return "AidanBot" + randomUUID;
    }

    @AfterClass
    public static void teardown() {
        driver.quit();
    }
}