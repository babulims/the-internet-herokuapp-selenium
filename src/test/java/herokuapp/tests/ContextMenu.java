package herokuapp.tests;

import herokuapp.utils.BaseTest;
import org.openqa.selenium.Alert;
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
import java.util.List;
import java.util.NoSuchElementException;

public class ContextMenu extends BaseTest {

    @BeforeClass
    public void setUp() {
        //Initializes driver and loads heroku-app page
        System.out.println("Starting the test");
        testContextSetup();
    }

    @Test(priority = 0)
    public void testClickOnLink() {
        String linkLocator = "[href='/context_menu']";
        WebElement link = webdriver.findElement(By.cssSelector(linkLocator));
        WebDriverWait wait = new WebDriverWait(webdriver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.elementToBeClickable(link));
        link.click();

        Assert.assertEquals(webdriver.getCurrentUrl(), "https://the-internet.herokuapp.com/context_menu");
    }

    @Test(priority = 1)
    public void testContextMenuClick() {
        String contextMenuId = "hot-spot";
        WebElement contextMenu = webdriver.findElement(By.id(contextMenuId));
        Actions actions = new Actions(webdriver);
        FluentWait<WebDriver> wait = new FluentWait<>(webdriver).withTimeout(Duration.ofSeconds(3))
                        .pollingEvery(Duration.ofSeconds(1))
                                .ignoring(NoSuchElementException.class);
        actions.contextClick(contextMenu).perform();
        WebDriverWait explicitWait = new WebDriverWait(webdriver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = webdriver.switchTo().alert();
        Assert.assertEquals(alert.getText(), "You selected a context menu");
        alert.accept();
    }

    @AfterClass
    public void tearDown() {
        System.out.println("Shutting down WebDriver");
        webdriver.quit();
    }
}
