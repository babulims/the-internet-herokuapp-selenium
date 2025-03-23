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
import java.util.Optional;
import java.util.Set;

public class MultipleWindows extends BaseTest {

    @BeforeClass
    public void setUp() {
        //Initializes driver and loads heroku-app page
        System.out.println("Starting the test");
        testContextSetup();
    }

    @Test
    public void testClickOnLink() {
        String linkLocator = "a[href='/windows']";

        FluentWait<WebDriver> wait = new FluentWait<>(webdriver).withTimeout(Duration.ofSeconds(3))
                        .pollingEvery(Duration.ofSeconds(1))
                                .ignoring(NoSuchElementException.class);
        WebElement elem = wait.until(ExpectedConditions.elementToBeClickable((By.cssSelector(linkLocator))));
        elem.click();
        Assert.assertEquals(webdriver.getCurrentUrl(), "https://the-internet.herokuapp.com/windows");
    }

    @Test(dependsOnMethods = {"testClickOnLink"})
    public void testMultipleWindows() {
        String linkText = "Click Here";
        FluentWait<WebDriver> wait = new FluentWait<>(webdriver)
                .withTimeout(Duration.ofSeconds(3))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);

        WebElement linkElem = wait.until(ExpectedConditions.elementToBeClickable(By.linkText(linkText)));
        linkElem.click();
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));

        String parentWindow = webdriver.getWindowHandle();
        Set<String> windowHandles = webdriver.getWindowHandles();
        Optional<String> newHandle = windowHandles.stream()
                .filter(s -> !s.equals(parentWindow))
                .findFirst();

        newHandle.ifPresent(s -> webdriver.switchTo().window(s));

        Assert.assertEquals(
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h3"))).getText(),
                "New Window");

        webdriver.switchTo().window(parentWindow);
        linkElem = wait.until(ExpectedConditions.elementToBeClickable(By.linkText(linkText)));
        linkElem.click();
        wait.until(ExpectedConditions.numberOfWindowsToBe(3));

        webdriver.getWindowHandles().stream()
                .filter(s -> !s.equals(parentWindow))
                .forEach(s -> {
                    webdriver.switchTo().window(s);
                    webdriver.close();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });

        webdriver.switchTo().window(parentWindow);
    }

    @AfterClass
    public void tearDown() {
        System.out.println("Shutting down WebDriver");
        webdriver.quit();
    }
}
