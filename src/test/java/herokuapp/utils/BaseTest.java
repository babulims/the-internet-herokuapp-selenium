package herokuapp.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class BaseTest {
    protected WebDriver webdriver;
    protected final String URL = "https://the-internet.herokuapp.com/";
    private final String DRIVER_PATH = "src/test/resources/chromedriver/chromedriver.exe";

    protected void testContextSetup() {
        System.setProperty("webdriver.chrome.driver", DRIVER_PATH);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--start-maximized");
        webdriver = new ChromeDriver(options);
        webdriver.get(URL);
    }
}
