package herokuapp.tests;

import herokuapp.utils.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class BasicAuth extends BaseTest {

    @BeforeClass
    public void setUp() {
        //Initializes driver and loads heroku-app page
        System.out.println("Starting the test");
        testContextSetup();
    }

    @Test
    public void testBasicAuth() throws InterruptedException {
        String url = "https://admin:admin@the-internet.herokuapp.com/basic_auth";
        webdriver.get(url);

        String textLocator = ".//div/p";
        WebElement textElement = webdriver.findElement(By.xpath(textLocator));
        Assert.assertEquals(textElement.getText().trim(), "Congratulations! You must have the proper credentials.");
    }

    @AfterClass
    public void tearDown() {
        System.out.println("Shutting down WebDriver");
        webdriver.quit();
    }
}
