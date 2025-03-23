package herokuapp.tests;

import herokuapp.utils.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

public class Hovers extends BaseTest {

    @BeforeClass
    public void setUp() {
        //Initializes driver and loads heroku-app page
        System.out.println("Starting the test");
        testContextSetup();
    }

    @Test
    public void testClickOnLink() {
        String linkLocator = "[href='/hovers']";

        FluentWait<WebDriver> wait = new FluentWait<>(webdriver).withTimeout(Duration.ofSeconds(3))
                        .pollingEvery(Duration.ofSeconds(1))
                                .ignoring(NoSuchElementException.class);
        WebElement elem = wait.until(ExpectedConditions.elementToBeClickable((By.cssSelector(linkLocator))));
        elem.click();
        Assert.assertEquals(webdriver.getCurrentUrl(), "https://the-internet.herokuapp.com/hovers");
    }

    @Test(dependsOnMethods = {"testClickOnLink"})
    public void testHovers() {
        String imagesLoc = "div[class='figure']";
        String nameTag = "h5";
        String profileTag = "a";

        FluentWait<WebDriver> wait = new FluentWait<>(webdriver)
                .withTimeout(Duration.ofSeconds(3))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);

        List<WebElement> images = wait.until(ExpectedConditions.
                visibilityOfAllElementsLocatedBy(By.cssSelector(imagesLoc)));

        Actions actions = new Actions(webdriver);
        for (int i = 0; i < images.size(); i++) {
            images = wait.until(ExpectedConditions.
                    visibilityOfAllElementsLocatedBy(By.cssSelector(imagesLoc)));
            actions.moveToElement(images.get(i)).perform();
            WebElement userName = wait.until(ExpectedConditions.visibilityOf(images.get(i).findElement(By.tagName(nameTag))));
            System.out.println("UserName: " + userName.getText());
            WebElement profileLink = wait.until(ExpectedConditions.
                    visibilityOf(images.get(i).findElement(By.tagName(profileTag))));
            profileLink.click();
            webdriver.navigate().back();
        }
    }

    @AfterClass
    public void tearDown() {
        System.out.println("Shutting down WebDriver");
        webdriver.quit();
    }
}
