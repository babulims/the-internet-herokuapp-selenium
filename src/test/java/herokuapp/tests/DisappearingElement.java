package herokuapp.tests;

import herokuapp.utils.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
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

public class DisappearingElement extends BaseTest {

    @BeforeClass
    public void setUp() {
        //Initializes driver and loads heroku-app page
        System.out.println("Starting the test");
        testContextSetup();
    }

    @Test
    public void testClickOnLink() {
        String linkLocator = "[href='/disappearing_elements']";

        FluentWait<WebDriver> wait = new FluentWait<>(webdriver).withTimeout(Duration.ofSeconds(3))
                        .pollingEvery(Duration.ofSeconds(1))
                                .ignoring(NoSuchElementException.class);
        WebElement elem = wait.until(ExpectedConditions.visibilityOf(webdriver.findElement(By.cssSelector(linkLocator))));
        elem.click();
        Assert.assertEquals(webdriver.getCurrentUrl(), "https://the-internet.herokuapp.com/disappearing_elements");
    }

    @Test(dependsOnMethods = {"testClickOnLink"})
    public void testDisappearingElement() {
        String galleryLoc = "a[href='/gallery/']";
        WebElement elem = null;
        int retryCount = 3;
        while (retryCount > 0) {
            try {
                FluentWait<WebDriver> wait = new FluentWait<>(webdriver).withTimeout(Duration.ofSeconds(3))
                        .pollingEvery(Duration.ofSeconds(1))
                        .ignoring(NoSuchElementException.class);
                elem = wait.until(
                        ExpectedConditions.elementToBeClickable(By.cssSelector(galleryLoc)));
                break;
            } catch (TimeoutException ex) {
                System.out.println("Timed out waiting, Retrying ...");
                webdriver.navigate().refresh();
                --retryCount;
            }
        }

        if (retryCount > 0) {
            elem.click();
            System.out.println("Element Appeared and clicked");
        } else {
            System.out.println("All retry attempts exhausted but element did not appear");
        }
    }

    @AfterClass
    public void tearDown() {
        System.out.println("Shutting down WebDriver");
        webdriver.quit();
    }
}
