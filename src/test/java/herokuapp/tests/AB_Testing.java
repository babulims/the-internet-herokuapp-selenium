package herokuapp.tests;

import herokuapp.utils.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

public class AB_Testing extends BaseTest {

    @BeforeClass
    public void setUp() {
        //Initializes driver and loads heroku-app page
        System.out.println("Starting the test");
        testContextSetup();
    }

    @Test
    public void testClickOnAB() {
        String LinkLocator = "[href='/abtest']";

        WebElement ab_test = webdriver.findElement(By.cssSelector(LinkLocator));

        System.out.println("Waiting to be clickable");
        WebDriverWait explicitWait = new WebDriverWait(webdriver, Duration.ofSeconds(3));
        explicitWait.until(ExpectedConditions.elementToBeClickable(ab_test));

        System.out.println("Performing click");
        ab_test.click();

        Assert.assertEquals(webdriver.getCurrentUrl(), "https://the-internet.herokuapp.com/abtest");
    }

    @AfterClass
    public void tearDown() {
        System.out.println("Shutting down WebDriver");
        webdriver.quit();
    }
}
