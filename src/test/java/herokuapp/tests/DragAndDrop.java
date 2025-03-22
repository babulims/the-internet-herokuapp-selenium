package herokuapp.tests;

import herokuapp.utils.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
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

public class DragAndDrop extends BaseTest {

    @BeforeClass
    public void setUp() {
        //Initializes driver and loads heroku-app page
        System.out.println("Starting the test");
        testContextSetup();
    }

    @Test
    public void testClickOnLink() {
        String linkLocator = "[href='/drag_and_drop']";

        FluentWait<WebDriver> wait = new FluentWait<>(webdriver).withTimeout(Duration.ofSeconds(3))
                        .pollingEvery(Duration.ofSeconds(1))
                                .ignoring(NoSuchElementException.class);
        WebElement elem = wait.until(ExpectedConditions.elementToBeClickable((By.cssSelector(linkLocator))));
        elem.click();
        Assert.assertEquals(webdriver.getCurrentUrl(), "https://the-internet.herokuapp.com/drag_and_drop");
    }

    @Test(dependsOnMethods = {"testClickOnLink"})
    public void testDRagAndDrop() {
        String aId = "column-a";
        String bId = "column-b";

        WebDriverWait wait = new WebDriverWait(webdriver, Duration.ofSeconds(3));
        WebElement a = wait.until(ExpectedConditions.presenceOfElementLocated(By.id(aId)));
        wait = new WebDriverWait(webdriver, Duration.ofSeconds(3));
        WebElement b = wait.until(ExpectedConditions.presenceOfElementLocated(By.id(bId)));

        Actions actions = new Actions(webdriver);
        //actions.clickAndHold(a).moveToElement(b).release().perform();
        actions.dragAndDrop(a, b).perform();

        a = webdriver.findElement(By.id(aId)).findElement(By.tagName("header"));
        b = webdriver.findElement(By.id(bId)).findElement(By.tagName("header"));

        Assert.assertEquals(a.getText(), "B");
        Assert.assertEquals(b.getText(), "A");
    }

    @AfterClass
    public void tearDown() {
        System.out.println("Shutting down WebDriver");
        webdriver.quit();
    }
}
