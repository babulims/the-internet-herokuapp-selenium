package herokuapp.tests;

import herokuapp.utils.BaseTest;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.NoSuchElementException;

public class FormAuthentication extends BaseTest {

    @BeforeClass
    public void setUp() {
        //Initializes driver and loads heroku-app page
        System.out.println("Starting the test");
        testContextSetup();
    }

    @Test
    public void testClickOnLink() {
        String linkLocator = "[href='/login']";

        FluentWait<WebDriver> wait = new FluentWait<>(webdriver).withTimeout(Duration.ofSeconds(3))
                        .pollingEvery(Duration.ofSeconds(1))
                                .ignoring(NoSuchElementException.class);
        WebElement elem = wait.until(ExpectedConditions.elementToBeClickable((By.cssSelector(linkLocator))));
        elem.click();
        Assert.assertEquals(webdriver.getCurrentUrl(), "https://the-internet.herokuapp.com/login");
    }

    @Test(dependsOnMethods = {"testClickOnLink"})
    public void testLogin() {
        //*****************          Test with Valid credentials        **************
        String userName = "tomsmith";
        String password = "SuperSecretPassword!";

        String userNameId = "username";
        String passwordId = "password";
        String btnLoc = "button[type='submit']";
        String logoutLoc = "a[href='/logout']";
        String flashMessageLoc = ".//div[@id='flash']";
        String logOutSuccessMessage = "You logged out of the secure area!";
        String loginSuccessMessage = "You logged into a secure area!";

        FluentWait<WebDriver> wait = new FluentWait<>(webdriver).withTimeout(Duration.ofSeconds(3))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);

        WebElement userNameElem = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id(userNameId)));
        userNameElem.sendKeys(userName);
        WebElement passElem = wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.id(passwordId)));
        passElem.sendKeys(password);

        WebElement loginBtnElem = wait.until(ExpectedConditions.
                elementToBeClickable(By.cssSelector(btnLoc)));
        loginBtnElem.click();

        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            alert.accept();
        } catch (TimeoutException e) {
            //As no alert came, you can continue
            System.out.println("No alert pop up");
        }

        WebElement successElem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(flashMessageLoc)));
        System.out.println("Login Success message: " + successElem.getText());
        Assert.assertTrue(successElem.getText().contains(loginSuccessMessage));

        WebElement logoutBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(logoutLoc)));
        logoutBtn.click();

        wait.until(ExpectedConditions.stalenessOf(successElem));
        WebElement success2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(flashMessageLoc)));
        System.out.println("Logout Success message: " + success2.getText());
        Assert.assertTrue(success2.getText().contains(logOutSuccessMessage));

        //***************       Test with invalid credentials       ***************
        String errorMessage = "Your password is invalid!";

        userNameElem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(userNameId)));
        userNameElem.sendKeys(userName);
        passElem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(passwordId)));
        passElem.sendKeys("dummy");
        loginBtnElem  =wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(btnLoc)));
        loginBtnElem.click();
        wait.until(ExpectedConditions.stalenessOf(success2));
        WebElement errorElem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(flashMessageLoc)));
        System.out.println("Login error message: " + errorElem.getText());
        Assert.assertTrue(errorElem.getText().contains(errorMessage));
    }

    @AfterClass
    public void tearDown() {
        System.out.println("Shutting down WebDriver");
        webdriver.quit();
    }
}
