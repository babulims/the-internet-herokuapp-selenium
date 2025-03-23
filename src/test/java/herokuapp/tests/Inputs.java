package herokuapp.tests;

import herokuapp.utils.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.NoSuchElementException;

public class Inputs extends BaseTest {

    @BeforeClass
    public void setUp() {
        //Initializes driver and loads heroku-app page
        System.out.println("Starting the test");
        testContextSetup();
    }

    @Test
    public void testClickOnLink() {
        String linkLocator = "[href='/inputs']";

        FluentWait<WebDriver> wait = new FluentWait<>(webdriver).withTimeout(Duration.ofSeconds(3))
                        .pollingEvery(Duration.ofSeconds(1))
                                .ignoring(NoSuchElementException.class);
        WebElement elem = wait.until(ExpectedConditions.elementToBeClickable((By.cssSelector(linkLocator))));
        elem.click();
        Assert.assertEquals(webdriver.getCurrentUrl(), "https://the-internet.herokuapp.com/inputs");
    }

    @Test(dependsOnMethods = {"testClickOnLink"})
    public void testInputs() throws InterruptedException {
        String inputLoc = "input[type='number']";

        WebDriverWait wait = new WebDriverWait(webdriver, Duration.ofSeconds(3));
        WebElement inputElem = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(inputLoc)));

        inputElem.sendKeys("34");
        inputElem.clear();

        inputElem.sendKeys("109");
        inputElem.clear();

        for (int i = 0; i < 100; i++) {
            inputElem.sendKeys(Keys.ARROW_UP);
        }

        for (int i = 0; i < 200; i++) {
            inputElem.sendKeys(Keys.ARROW_DOWN);
        }

        Thread.sleep(2000);
    }

    @AfterClass
    public void tearDown() {
        System.out.println("Shutting down WebDriver");
        webdriver.quit();
    }
}
