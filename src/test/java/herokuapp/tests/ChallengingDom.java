package herokuapp.tests;

import herokuapp.utils.BaseTest;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ChallengingDom extends BaseTest {

    @BeforeClass
    public void setUp() {
        //Initializes driver and loads heroku-app page
        System.out.println("Starting the test");
        testContextSetup();
    }

    @Test
    public void testClickOnLink() {
        String linkLocator = "a[href='/challenging_dom']";
        WebElement link = webdriver.findElement(By.cssSelector(linkLocator));
        WebDriverWait wait = new WebDriverWait(webdriver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.elementToBeClickable(link));

        link.click();
        Assert.assertEquals(webdriver.getCurrentUrl(), "https://the-internet.herokuapp.com/challenging_dom");
    }

    /**
     * 1. Try clicking on each button and show the corresponding canvas value y taking screenshots
     * 2. Verify if a given value is present in the table or not.
     */
    @Test(dependsOnMethods = {"testClickOnLink"})
    public void testDomElements() throws IOException {
        String button1_loc = ".//a[@class='button']";
        String button2_loc = ".//a[@class='button alert']";
        String button3_loc = ".//a[@class='button success']";
        String canvasId = "canvas";

        webdriver.findElement(By.xpath(button1_loc)).click();
        //Take canvas screenshot
        WebElement canvas = webdriver.findElement(By.id(canvasId));
        ((JavascriptExecutor) webdriver).executeScript("arguments[0].scrollIntoView(true);", canvas);
        File screenshot = canvas.getScreenshotAs(OutputType.FILE);
        File destination = new File("src/test/resources/canvas-screenshot_" + System.currentTimeMillis() + ".png");
        FileUtils.copyFile(screenshot, destination);

        webdriver.findElement(By.xpath(button2_loc)).click();
        //Take canvas screenshot
        canvas = webdriver.findElement(By.id(canvasId));
        ((JavascriptExecutor) webdriver).executeScript("arguments[0].scrollIntoView(true);", canvas);
        screenshot = canvas.getScreenshotAs(OutputType.FILE);
        destination = new File("src/test/resources/canvas-screenshot_" + System.currentTimeMillis() + ".png");
        FileUtils.copyFile(screenshot, destination);

        webdriver.findElement(By.xpath(button3_loc)).click();
        //Take canvas screenshot
        canvas = webdriver.findElement(By.id(canvasId));
        ((JavascriptExecutor) webdriver).executeScript("arguments[0].scrollIntoView(true);", canvas);
        screenshot = canvas.getScreenshotAs(OutputType.FILE);
        destination = new File("src/test/resources/canvas-screenshot_" + System.currentTimeMillis() + ".png");
        FileUtils.copyFile(screenshot, destination);

        //Get All Values from the table
        String tableLoc = ".//table//tbody/tr";
        List<WebElement> rows = webdriver.findElements(By.xpath(tableLoc));
        List<String> allValues = new ArrayList<>();
        for (WebElement row : rows) {
            List<WebElement> columns = row.findElements(By.tagName("td"));
            for (int i = columns.size() - 2; i >= 0; i--) {
                allValues.add(columns.get(i).getText());
            }
        }

        Assert.assertTrue(allValues.contains("Consequuntur9"));
    }

    @AfterClass
    public void tearDown() {
        System.out.println("Shutting down WebDriver");
        webdriver.quit();
    }
}
