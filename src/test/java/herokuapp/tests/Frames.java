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

public class Frames extends BaseTest {

    @BeforeClass
    public void setUp() {
        //Initializes driver and loads heroku-app page
        System.out.println("Starting the test");
        testContextSetup();
    }

    @Test
    public void testClickOnLink() {
        String linkLocator = "[href='/frames']";

        FluentWait<WebDriver> wait = new FluentWait<>(webdriver).withTimeout(Duration.ofSeconds(3))
                        .pollingEvery(Duration.ofSeconds(1))
                                .ignoring(NoSuchElementException.class);
        WebElement elem = wait.until(ExpectedConditions.elementToBeClickable((By.cssSelector(linkLocator))));
        elem.click();
        Assert.assertEquals(webdriver.getCurrentUrl(), "https://the-internet.herokuapp.com/frames");
    }

    @Test(dependsOnMethods = {"testClickOnLink"})
    public void testIFrames() {
        String iFrameLinkLoc = "//*[@href='/iframe']";
        String closeLoc = "div[aria-label='Close']";
        String contentLoc = "body[id='tinymce'] p";
        String iframeId = "mce_0_ifr";

        FluentWait<WebDriver> wait = new FluentWait<>(webdriver).withTimeout(Duration.ofSeconds(3))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);

        //Click on iFrame link
        WebElement iframeLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(iFrameLinkLoc)));
        iframeLink.click();
        //Close the warning pop up
        WebElement popUpElem = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(closeLoc)));
        popUpElem.click();
        //Get the content inside iFrame
        WebElement iFrame = wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id(iframeId)))
                .findElement(By.cssSelector(contentLoc));
        Assert.assertEquals(iFrame.getText(), "Your content goes here.");

        webdriver.switchTo().defaultContent();
        webdriver.navigate().back();

        //Test begin for nested iFrames
        String nestedFrameLinkLoc = "//*[@href='/nested_frames']";
        WebElement nestedFrameLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(nestedFrameLinkLoc)));
        nestedFrameLink.click();

        String frameTop = "frame-top";
        String frameBottom = "frame-bottom";
        String frameLeft = "frame-left";
        String frameMiddle = "frame-middle";
        String frameRight = "frame-right";

        WebElement iFrameTop = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(frameTop)));
        webdriver.switchTo().frame(iFrameTop);
        System.out.println("I am in Top Frame");
        webdriver.switchTo().frame(frameLeft);
        System.out.println("I am in Left Frame");
        webdriver.switchTo().parentFrame();
        webdriver.switchTo().frame(frameMiddle);
        System.out.println("I am in middle frame");
        webdriver.switchTo().parentFrame();
        webdriver.switchTo().frame(frameRight);
        System.out.println("I am in right frame");
        webdriver.switchTo().defaultContent();

        webdriver.switchTo().frame(frameBottom);
        System.out.println("I am in Bottom Frame");
        webdriver.switchTo().defaultContent();
    }

    @AfterClass
    public void tearDown() {
        System.out.println("Shutting down WebDriver");
        webdriver.quit();
    }
}
