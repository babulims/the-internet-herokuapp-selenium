package herokuapp.tests;

import herokuapp.utils.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.NoSuchElementException;

public class DynamicContent extends BaseTest {

    @BeforeClass
    public void setUp() {
        //Initializes driver and loads heroku-app page
        System.out.println("Starting the test");
        testContextSetup();
    }

    @Test
    public void testClickOnLink() {
        String linkLocator = "[href='/dynamic_content']";

        FluentWait<WebDriver> wait = new FluentWait<>(webdriver).withTimeout(Duration.ofSeconds(3))
                        .pollingEvery(Duration.ofSeconds(1))
                                .ignoring(NoSuchElementException.class);
        WebElement elem = wait.until(ExpectedConditions.elementToBeClickable((By.cssSelector(linkLocator))));
        elem.click();
        Assert.assertEquals(webdriver.getCurrentUrl(), "https://the-internet.herokuapp.com/dynamic_content");
    }

    @Test(dependsOnMethods = {"testClickOnLink"})
    public void testDynamicContent() {
        String dynamicLocator = "(.//div[@class='large-10 columns'])[3]";

        FluentWait<WebDriver> wait = new FluentWait<>(webdriver).withTimeout(Duration.ofSeconds(3))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);
        WebElement dynamicElement = webdriver.findElement(By.xpath(dynamicLocator));
        wait.until(ExpectedConditions.visibilityOf(dynamicElement));
        String text1 = dynamicElement.getText();

        String clickHereLinkText = "click here";
        webdriver.findElement(By.linkText(clickHereLinkText)).click();

        wait.until(ExpectedConditions.stalenessOf(dynamicElement));
        dynamicElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(dynamicLocator)));
        String text2 = dynamicElement.getText();

        Assert.assertFalse(text1.equalsIgnoreCase(text2));
    }

    @AfterClass
    public void tearDown() {
        System.out.println("Shutting down WebDriver");
        webdriver.quit();
    }
}
