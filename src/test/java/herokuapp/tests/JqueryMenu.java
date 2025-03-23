package herokuapp.tests;

import herokuapp.utils.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class JqueryMenu{
    private WebDriver driver;
    private final String DRIVER_PATH = "src/test/resources/chromedriver/chromedriver.exe";
    private final String DOWNLOAD_DIRECTORY = "C:\\Users\\babulisethi\\Practice\\the-internet-herokuapp-selenium" +
            "\\src\\test\\resources\\downloads";
    private final String URL = "https://the-internet.herokuapp.com/";

    @BeforeClass
    public void setUp() {
        //Initializes driver and loads heroku-app page
        System.out.println("Starting the test");
        System.setProperty("webdriver.chrome.driver", DRIVER_PATH);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-popup-blocking");
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", DOWNLOAD_DIRECTORY);
        prefs.put("download.prompt_for_download", false);
        prefs.put("directory_upgrade", true);
        //prefs.put("safebrowsing.enabled", false);
        options.setExperimentalOption("prefs", prefs);

        driver = new ChromeDriver(options);
        driver.get(URL);
    }

    @Test
    public void testClickOnLink() {
        String linkLocator = "a[href='/jqueryui/menu']";

        FluentWait<WebDriver> wait = new FluentWait<>(driver).withTimeout(Duration.ofSeconds(3))
                        .pollingEvery(Duration.ofSeconds(1))
                                .ignoring(NoSuchElementException.class);
        WebElement elem = wait.until(ExpectedConditions.elementToBeClickable((By.cssSelector(linkLocator))));
        elem.click();
        Assert.assertEquals(driver.getCurrentUrl(), "https://the-internet.herokuapp.com/jqueryui/menu");
    }

    @Test(dependsOnMethods = {"testClickOnLink"})
    public void testMenu() throws InterruptedException {
        String enabledLinkText = "Enabled";
        String downloadsLinkText = "Downloads";
        String pdfLinkText = "PDF";

        FluentWait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(3))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);

        WebElement enabledMenu = wait.until(ExpectedConditions.elementToBeClickable(By.linkText(enabledLinkText)));
        Actions actions = new Actions(driver);
        actions.moveToElement(enabledMenu).perform();

        WebElement downloadsMenu = wait.until(ExpectedConditions.elementToBeClickable(By.linkText(downloadsLinkText)));
        actions.moveToElement(downloadsMenu).perform();

        WebElement pdfMenu = wait.until(ExpectedConditions.elementToBeClickable(By.linkText(pdfLinkText)));
        actions.moveToElement(pdfMenu).click().perform();
    }

    @AfterClass
    public void tearDown() {
        System.out.println("Shutting down WebDriver");
        driver.quit();
    }
}
