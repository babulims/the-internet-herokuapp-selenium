package herokuapp.tests;

import herokuapp.utils.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.NoSuchElementException;

public class ExitIntent extends BaseTest {

    @BeforeClass
    public void setUp() {
        //Initializes driver and loads heroku-app page
        System.out.println("Starting the test");
        testContextSetup();
    }

    @Test
    public void testClickOnLink() {
        String linkLocator = "[href='/exit_intent']";

        FluentWait<WebDriver> wait = new FluentWait<>(webdriver).withTimeout(Duration.ofSeconds(3))
                        .pollingEvery(Duration.ofSeconds(1))
                                .ignoring(NoSuchElementException.class);
        WebElement elem = wait.until(ExpectedConditions.elementToBeClickable((By.cssSelector(linkLocator))));
        elem.click();
        Assert.assertEquals(webdriver.getCurrentUrl(), "https://the-internet.herokuapp.com/exit_intent");
    }

    @Test(dependsOnMethods = {"testClickOnLink"})
    public void testExitIntent() {
        String headerLoc = "div[class='example'] h3";

        WebDriverWait wait = new WebDriverWait(webdriver, Duration.ofSeconds(10));
        WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(headerLoc)));

        int x = header.getLocation().getX();
        int y = header.getLocation().getY();

        System.out.println("x: " + x + ", y: " + y);

        Actions actions = new Actions(webdriver);
        actions.moveToElement(header).perform();
        //This is supposed to move mouse out of the view ,but it is not happening, not sure why :(
        actions.moveByOffset(x - 250, y - 30).build().perform();

        String modalLoc = "div[class='modal-footer'] p";
        WebElement modal = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(modalLoc)));
        modal.click();
    }

    @AfterClass
    public void tearDown() {
        System.out.println("Shutting down WebDriver");
        webdriver.quit();
    }
}
