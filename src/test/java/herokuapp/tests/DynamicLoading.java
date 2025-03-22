package herokuapp.tests;

import herokuapp.utils.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.NoSuchElementException;

public class DynamicLoading extends BaseTest {

    @BeforeClass
    public void setUp() {
        //Initializes driver and loads heroku-app page
        System.out.println("Starting the test");
        testContextSetup();
    }

    @Test
    public void testClickOnLink() {
        String linkLocator = "[href='/dynamic_loading']";

        FluentWait<WebDriver> wait = new FluentWait<>(webdriver).withTimeout(Duration.ofSeconds(3))
                        .pollingEvery(Duration.ofSeconds(1))
                                .ignoring(NoSuchElementException.class);
        WebElement elem = wait.until(ExpectedConditions.elementToBeClickable((By.cssSelector(linkLocator))));
        elem.click();
        Assert.assertEquals(webdriver.getCurrentUrl(), "https://the-internet.herokuapp.com/dynamic_loading");
    }

    @Test(dependsOnMethods = {"testClickOnLink"})
    public void testDynamicLoading() {
        String example1_text = "Example 1";
        String example2_text = "Example 2";
        String startButtonTag = "button";
        String dynamicLoc = "div[id='finish'] h4";

        FluentWait<WebDriver> wait = new FluentWait<>(webdriver).withTimeout(Duration.ofSeconds(3))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);

        WebElement example1 = wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText(example1_text)));
        example1.click();

        WebElement startButton = wait.until(ExpectedConditions.elementToBeClickable(By.tagName(startButtonTag)));
        startButton.click();

        WebElement dynamicElem1 = wait.withTimeout(Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(dynamicLoc)));
        Assert.assertEquals(dynamicElem1.getText(), "Hello World!");

        webdriver.navigate().back();
        WebElement example2 = wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText(example2_text)));
        example2.click();

        startButton = wait.until(ExpectedConditions.elementToBeClickable(By.tagName(startButtonTag)));
        startButton.click();

        WebElement dynamicElem2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(dynamicLoc)));
        Assert.assertEquals(dynamicElem2.getText(), "Hello World!");
    }

    @AfterClass
    public void tearDown() {
        System.out.println("Shutting down WebDriver");
        webdriver.quit();
    }
}
