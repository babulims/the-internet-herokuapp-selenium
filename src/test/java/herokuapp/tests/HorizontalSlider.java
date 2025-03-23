package herokuapp.tests;

import herokuapp.utils.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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

public class HorizontalSlider extends BaseTest {

    @BeforeClass
    public void setUp() {
        //Initializes driver and loads heroku-app page
        System.out.println("Starting the test");
        testContextSetup();
    }

    @Test
    public void testClickOnLink() {
        String linkLocator = "[href='/horizontal_slider']";

        FluentWait<WebDriver> wait = new FluentWait<>(webdriver).withTimeout(Duration.ofSeconds(3))
                        .pollingEvery(Duration.ofSeconds(1))
                                .ignoring(NoSuchElementException.class);
        WebElement elem = wait.until(ExpectedConditions.elementToBeClickable((By.cssSelector(linkLocator))));
        elem.click();
        Assert.assertEquals(webdriver.getCurrentUrl(), "https://the-internet.herokuapp.com/horizontal_slider");
    }

    @Test(dependsOnMethods = {"testClickOnLink"})
    public void testHorizontalSlider() throws InterruptedException {
        String sliderLoc = "input[type='range']";
        String valLoc = "span[id='range']";

        WebDriverWait wait = new WebDriverWait(webdriver, Duration.ofSeconds(3));
        WebElement sliderElem = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(sliderLoc)));

        Actions actions = new Actions(webdriver);
//        int x = sliderElem.getLocation().getX();
//        int y = sliderElem.getLocation().getY();
//        actions.dragAndDropBy(sliderElem, x - 10, y).build().perform();

        sliderElem.click();
        Thread.sleep(2000);
        actions.sendKeys(sliderElem, Keys.LEFT).perform();
        Thread.sleep(2000);
        actions.sendKeys(sliderElem, Keys.RIGHT).perform();
        Thread.sleep(2000);
        actions.sendKeys(sliderElem, Keys.RIGHT).perform();
        Thread.sleep(2000);
        actions.sendKeys(sliderElem, Keys.LEFT).perform();
        Thread.sleep(2000);

        //JavascriptExecutor executor = (JavascriptExecutor) webdriver;
        //executor.executeScript("arguments[0].value = arguments[1];", sliderElem, 50);
        //executor.executeScript("arguments[0].dispatchEvent(new Event('input'));", sliderElem);
        //Thread.sleep(2000);
        WebElement valueElem = webdriver.findElement(By.cssSelector(valLoc));
        //Assert.assertEquals(valueElem.getText(), "1");
    }

    @AfterClass
    public void tearDown() {
        System.out.println("Shutting down WebDriver");
        webdriver.quit();
    }
}
