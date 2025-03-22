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

public class EntryAd extends BaseTest {

    @BeforeClass
    public void setUp() {
        //Initializes driver and loads heroku-app page
        System.out.println("Starting the test");
        testContextSetup();
    }

    @Test
    public void testClickOnLink() {
        String linkLocator = "[href='/entry_ad']";

        FluentWait<WebDriver> wait = new FluentWait<>(webdriver).withTimeout(Duration.ofSeconds(3))
                        .pollingEvery(Duration.ofSeconds(1))
                                .ignoring(NoSuchElementException.class);
        WebElement elem = wait.until(ExpectedConditions.elementToBeClickable((By.cssSelector(linkLocator))));
        elem.click();
        Assert.assertEquals(webdriver.getCurrentUrl(), "https://the-internet.herokuapp.com/entry_ad");
    }

    @Test(dependsOnMethods = {"testClickOnLink"})
    public void testEntryAd() {
        String modalBody = "div[class='modal-body']";
        String modalFooter = "div[class='modal-footer']";
        String modalId = "modal";

        FluentWait<WebDriver> wait = new FluentWait<>(webdriver).withTimeout(Duration.ofSeconds(3))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(modalId)));
        WebElement modalBodyElem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(modalBody)));
        Assert.assertEquals(modalBodyElem.getText(), "It's commonly used to encourage a user to take an action " +
                "(e.g., give their e-mail address to sign up for something or disable their ad blocker).");

        WebElement modalFooterElem = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(modalFooter)));
        modalFooterElem.click();
    }

    @AfterClass
    public void tearDown() {
        System.out.println("Shutting down WebDriver");
        webdriver.quit();
    }
}
