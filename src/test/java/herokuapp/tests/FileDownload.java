package herokuapp.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * As per online resources this should work, but its only downloading .tmp files.
 * Better tryout with Firefox driver once and check.
 */
public class FileDownload {
    private WebDriver webdriver;
    private final String URL = "https://the-internet.herokuapp.com/";
    private final String DRIVER_PATH = "src/test/resources/chromedriver/chromedriver.exe";

    @BeforeClass
    public void setUp() {
        //Initializes driver and loads heroku-app page
        System.out.println("Starting the test");
        System.setProperty("webdriver.chrome.driver", DRIVER_PATH);

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", "C:\\Users\\babulisethi\\Practice\\the-internet-herokuapp-selenium\\src\\test\\resources\\downloads");
        prefs.put("download.prompt_for_download", false);
        prefs.put("profile.default_content_settings.popups", 0);
        prefs.put("safebrowsing.enabled", false);
        prefs.put("directory_upgrade", true);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-extensions");
        options.setExperimentalOption("prefs", prefs);
        options.setExperimentalOption("useAutomationExtension", false);

        webdriver = new ChromeDriver(options);
        webdriver.get(URL);
    }

    @Test
    public void testClickOnLink() {
        String linkLocator = "[href='/download']";

        FluentWait<WebDriver> wait = new FluentWait<>(webdriver).withTimeout(Duration.ofSeconds(3))
                        .pollingEvery(Duration.ofSeconds(1))
                                .ignoring(NoSuchElementException.class);
        WebElement elem = wait.until(ExpectedConditions.elementToBeClickable((By.cssSelector(linkLocator))));
        elem.click();
        Assert.assertEquals(webdriver.getCurrentUrl(), "https://the-internet.herokuapp.com/download");
    }

    @Test(dependsOnMethods = {"testClickOnLink"})
    public void testFileDownloads() throws InterruptedException {
        String allLinkLocs = ".//h3[text()='File Downloader']/following-sibling::a";

        FluentWait<WebDriver> wait = new FluentWait<>(webdriver).withTimeout(Duration.ofSeconds(3))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);

        List<WebElement> allDownloadLinks =
                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(allLinkLocs)));

        for (WebElement elem : allDownloadLinks) {
            System.out.println("Trying to download: " + elem.getText());
            elem.click();
            Thread.sleep(4000);
        }
    }

    @AfterClass
    public void tearDown() throws InterruptedException {
        System.out.println("Shutting down WebDriver");
        Thread.sleep(10000);
        webdriver.quit();
    }
}
