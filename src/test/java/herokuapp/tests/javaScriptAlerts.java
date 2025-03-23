package herokuapp.tests;

import herokuapp.utils.BaseTest;
import org.openqa.selenium.Alert;
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

public class javaScriptAlerts extends BaseTest {

    @BeforeClass
    public void setUp() {
        //Initializes driver and loads heroku-app page
        System.out.println("Starting the test");
        testContextSetup();
    }

    @Test
    public void testClickOnLink() {
        String linkLocator = "a[href='/javascript_alerts']";

        FluentWait<WebDriver> wait = new FluentWait<>(webdriver).withTimeout(Duration.ofSeconds(3))
                        .pollingEvery(Duration.ofSeconds(1))
                                .ignoring(NoSuchElementException.class);
        WebElement elem = wait.until(ExpectedConditions.elementToBeClickable((By.cssSelector(linkLocator))));
        elem.click();
        Assert.assertEquals(webdriver.getCurrentUrl(), "https://the-internet.herokuapp.com/javascript_alerts");
    }

    @Test(dependsOnMethods = {"testClickOnLink"})
    public void testAlerts() {
        String alertLoc = "ul li:nth-child(1) button";
        String confirmLoc = "ul li:nth-child(2) button";
        String promptLoc = "ul li:nth-child(3) button";
        String resultLoc = "p[id='result']";

        FluentWait<WebDriver> wait = new FluentWait<>(webdriver)
                .withTimeout(Duration.ofSeconds(3))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);

        WebElement alertElem = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(alertLoc)));
        alertElem.click();
        Alert alert1 = webdriver.switchTo().alert();
        alert1.accept();

        WebElement resultElem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(resultLoc)));
        Assert.assertEquals(resultElem.getText(), "You successfully clicked an alert");

        WebElement confirmElem = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(confirmLoc)));
        confirmElem.click();
        Alert alert2 = wait.until(ExpectedConditions.alertIsPresent());
        alert2.dismiss();

        resultElem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(resultLoc)));
        Assert.assertEquals(resultElem.getText(), "You clicked: Cancel");

        WebElement promptElem = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(promptLoc)));
        promptElem.click();
        Alert alert3 = wait.until(ExpectedConditions.alertIsPresent());
        alert3.sendKeys("I am an Engineer!");
        alert3.accept();

        resultElem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(resultLoc)));
        Assert.assertEquals(resultElem.getText(), "You entered: I am an Engineer!");
    }

    @AfterClass
    public void tearDown() {
        System.out.println("Shutting down WebDriver");
        webdriver.quit();
    }
}
