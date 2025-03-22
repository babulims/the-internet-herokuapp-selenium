package herokuapp.tests;

import herokuapp.utils.BaseTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class BrokenImages extends BaseTest {

    @BeforeClass
    public void setUp() {
        //Initializes driver and loads heroku-app page
        System.out.println("Starting the test");
        testContextSetup();
    }

    //Verify that there are total 3 images. Count broken images and validate.
    @Test
    public void testClickBrokenImages() {
        String linkLocator = "[href='/broken_images']";
        WebElement link = webdriver.findElement(By.cssSelector(linkLocator));
        WebDriverWait explicitWait = new WebDriverWait(webdriver, Duration.ofSeconds(3));
        explicitWait.until(ExpectedConditions.elementToBeClickable(link));
        link.click();

        String imagesLocator = ".//h3[text()='Broken Images']/following-sibling::img";
        List<WebElement> images = webdriver.findElements(By.xpath(imagesLocator));

        int brokenImagesCount = 0;

        //collect all src values to check if images are broken or not
        for (WebElement image : images) {
            System.out.println("Value : " + image.getDomAttribute("src"));
            RequestSpecification requestSpec = RestAssured.given().baseUri(URL)
                    .basePath(image.getDomAttribute("src"));

            Response response = requestSpec.get().thenReturn();

            if (response.getStatusCode() == 404) {
                ++brokenImagesCount;
            }
        }

        Assert.assertEquals(brokenImagesCount, 2);
    }

    @AfterClass
    public void tearDown() {
        System.out.println("Shutting down WebDriver");
        webdriver.quit();
    }
}
