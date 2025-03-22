package herokuapp.tests;

import herokuapp.utils.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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

public class Add_Remove_Elements extends BaseTest {

    @BeforeClass
    public void setUp() {
        //Initializes driver and loads heroku-app page
        System.out.println("Starting the test");
        testContextSetup();
    }

    @Test(priority = 0)
    public void testClickOnAddRemoveElements() {
        String linkText = "Add/Remove Elements";

        WebElement ab_test = webdriver.findElement(By.linkText(linkText));

        System.out.println("Waiting to be clickable");
        WebDriverWait explicitWait = new WebDriverWait(webdriver, Duration.ofSeconds(3));
        explicitWait.until(ExpectedConditions.elementToBeClickable(ab_test));

        System.out.println("Performing click");
        ab_test.click();

        Assert.assertEquals(webdriver.getCurrentUrl(), "https://the-internet.herokuapp.com/add_remove_elements/");
    }

    @Test(priority = 1)
    public void testAddElement() {
        String addElementButtonLocator = "//div[@class='example']/button[1]";

        WebElement addElementButton = webdriver.findElement(By.xpath(addElementButtonLocator));

        System.out.println("Waiting to be clickable");
        WebDriverWait explicitWait = new WebDriverWait(webdriver, Duration.ofSeconds(3));
        explicitWait.until(ExpectedConditions.elementToBeClickable(addElementButton));

        System.out.println("Performing click one time");
        addElementButton.click();

        //Verify one delete button is visible now
        String deleteButtonsLocator = "button[class='added-manually']";
        List<WebElement> deleteButtons = webdriver.findElements(By.cssSelector(deleteButtonsLocator));
        Assert.assertEquals(deleteButtons.size(), 1);

        //Verify Clicking on delete once, removes delete button from the page
        FluentWait<WebDriver> fluentWait = new FluentWait<>(webdriver).withTimeout(Duration.ofSeconds(3))
                .pollingEvery(Duration.ofSeconds(1)).ignoring(NoSuchElementException.class);
        fluentWait.until(ExpectedConditions.visibilityOf(deleteButtons.get(0)));

        System.out.println("Clicking on delete button");
        deleteButtons.get(0).click();
        deleteButtons = webdriver.findElements(By.cssSelector(deleteButtonsLocator));

        Assert.assertEquals(deleteButtons.size(), 0);

        //Click on Add element 3 times and verify there are 3 delete buttons
        System.out.println("Clicking on Add Element button 3 times");
        int counter = 0;

        while (counter < 3) {
            addElementButton.click();
            ++counter;
        }

        deleteButtons = webdriver.findElements(By.cssSelector(deleteButtonsLocator));
        Assert.assertEquals(deleteButtons.size(), 3);

        //Click on all delete buttons and verify there is none left.
        fluentWait = new FluentWait<>(webdriver).withTimeout(Duration.ofSeconds(3))
                .pollingEvery(Duration.ofSeconds(1)).ignoring(NoSuchElementException.class);
        fluentWait.until(ExpectedConditions.visibilityOfAllElements(deleteButtons));

        System.out.println("Clicking on all delete buttons");
        for (WebElement button : deleteButtons) {
            button.click();
        }

        deleteButtons = webdriver.findElements(By.cssSelector(deleteButtonsLocator));
        Assert.assertEquals(deleteButtons.size(), 0);

    }

    @AfterClass
    public void tearDown() {
        System.out.println("Shutting down WebDriver");
        webdriver.quit();
    }
}
