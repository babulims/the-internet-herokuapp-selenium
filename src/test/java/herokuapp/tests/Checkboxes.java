package herokuapp.tests;

import herokuapp.utils.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class Checkboxes extends BaseTest {

    @BeforeClass
    public void setUp() {
        //Initializes driver and loads heroku-app page
        System.out.println("Starting the test");
        testContextSetup();
    }

    @Test(priority = 0)
    public void testClickOnLink() {
        String linkLocator = "[href='/checkboxes']";

        WebElement link = webdriver.findElement(By.cssSelector(linkLocator));
        FluentWait<WebDriver> wait = new FluentWait<>(webdriver).withTimeout(Duration.ofSeconds(3))
                        .pollingEvery(Duration.ofSeconds(1))
                                .ignoring(NoSuchElementException.class);
        wait.until(ExpectedConditions.elementToBeClickable(link));
        link.click();

        Assert.assertEquals(webdriver.getCurrentUrl(), "https://the-internet.herokuapp.com/checkboxes");
    }

    @Test(priority = 1)
    public void testCheckBoxes() throws InterruptedException {
        String checkboxesLoc = "input[type='checkbox']";
        List<WebElement> checkboxes = webdriver.findElements(By.cssSelector(checkboxesLoc));

        //1st verify that 1st checkbox is not checked but the 2nd one is checked
        Assert.assertFalse(checkboxes.get(0).isSelected());
        Assert.assertTrue(checkboxes.get(1).isSelected());

        checkboxes.get(0).click();
        Assert.assertTrue(checkboxes.get(0).isSelected());


        checkboxes.get(1).click();
        Assert.assertFalse(checkboxes.get(1).isSelected());
    }

    @AfterClass
    public void tearDown() {
        System.out.println("Shutting down WebDriver");
        webdriver.quit();
    }
}
