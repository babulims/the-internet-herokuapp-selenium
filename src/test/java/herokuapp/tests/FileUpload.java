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

public class FileUpload extends BaseTest {

    @BeforeClass
    public void setUp() {
        //Initializes driver and loads heroku-app page
        System.out.println("Starting the test");
        testContextSetup();
    }

    @Test
    public void testClickOnLink() {
        String linkLocator = "[href='/upload']";

        FluentWait<WebDriver> wait = new FluentWait<>(webdriver).withTimeout(Duration.ofSeconds(3))
                        .pollingEvery(Duration.ofSeconds(1))
                                .ignoring(NoSuchElementException.class);
        WebElement elem = wait.until(ExpectedConditions.elementToBeClickable((By.cssSelector(linkLocator))));
        elem.click();
        Assert.assertEquals(webdriver.getCurrentUrl(), "https://the-internet.herokuapp.com/upload");
    }

    @Test(dependsOnMethods = {"testClickOnLink"})
    public void testFileUpload() {
        String chooseFileLoc = "input[id='file-upload']";
        String uploadBtnLoc = "input[id='file-submit']";
        String uploadedFilesLoc = "div[id='uploaded-files']";

        String fileToUpload = "C:\\Users\\babulisethi\\OneDrive - Paysafe\\Desktop\\Error_SS.png";

        FluentWait<WebDriver> wait = new FluentWait<>(webdriver).withTimeout(Duration.ofSeconds(3))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);

        WebElement chooseFile = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(chooseFileLoc)));
        chooseFile.sendKeys(fileToUpload);
        WebElement uploadBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(uploadBtnLoc)));
        uploadBtn.click();

        WebElement uploadedFile = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(uploadedFilesLoc)));
        Assert.assertEquals(uploadedFile.getText(), "Error_SS.png");

        webdriver.navigate().back();

        //Verify for drag and drop file box
        String fileBoxId = "drag-drop-upload";
        WebElement fileBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(fileBoxId)));
        fileBox.sendKeys("C:\\Users\\babulisethi\\OneDrive - Paysafe\\Desktop\\Error_SS.png");
    }

    @AfterClass
    public void tearDown() throws InterruptedException {
        System.out.println("Shutting down WebDriver");
        Thread.sleep(3000);
        webdriver.quit();
    }
}
