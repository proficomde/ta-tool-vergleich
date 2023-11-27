package de.proficom.guitests;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BuyProductsTest {

    static final String URL = "https://www.advantageonlineshopping.com/#/";
    static final boolean HEADLESS_MODE = true;
    static final boolean TAKE_SCREENSHOTS = false;
    static final String PRODUCT1_NAME = "Kensington Orbit 72352 Trackball";
    static final String PRODUCT2_NAME = "HP ROAR PLUS WIRELESS SPEAKER";

    protected WebDriver driver;
    protected WebDriverWait explicitWait;

    @BeforeClass
    public void beforeClass() {
        ChromeOptions options = new ChromeOptions();
        if (HEADLESS_MODE) {
            options.addArguments("--headless=new");
        }
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        explicitWait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterClass
    public void afterClass() {
        driver.quit();
    }

    @Test
    public void RunTestCase() throws Exception{
        LocalDateTime timeNowTestStart = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");

        // STEP 0   - Go to home page
        driver.get(URL);

        // STEP 1   - Check if shopping cart is empty
        driver.findElement(By.xpath("//a[@id='shoppingCartLink']")).click();
        WebElement emptyText = driver.findElement(By.xpath("//div[@id='shoppingCart']/div/label[@translate='Your_shopping_cart_is_empty']"));

        if (TAKE_SCREENSHOTS) {
            takeScreenshotFromDriver(driver, "screenshots/empty_shopping_cart_"+timeNowTestStart.format(dateTimeFormatter)+".png");
        }

        Assert.assertEquals(emptyText.getText(), "Your shopping cart is empty");

        // STEP 2.1 - Go to home page category 'Mice'
        driver.findElement(By.xpath("//a[text()='CONTINUE SHOPPING']")).click();
        driver.findElement(By.xpath("//div[@class='shop_now_slider']/span[text()='MICE']")).click();
        // STEP 2.2 - Filter for Scroller type 'Scroll Ring' and 'Scroll Ball'
        driver.findElement(By.xpath("//li[contains(@*, 'attributesToShow')][.//*[contains(text(), 'SCROLLER TYPE')]]")).click();
        driver.findElement(By.xpath("//label[text()='Scroll Ball']/../input")).click();
        driver.findElement(By.xpath("//label[text()='Scroll Ring']/../input")).click();

        if (TAKE_SCREENSHOTS) {
            // We need to wait for JavaScript to apply the filter correctly
            explicitWait.until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver driver) {
                    int elementCount = driver.findElements(By.xpath("//*[contains(@class, 'productName')]")).size();
                    if (elementCount == 2) return true; else return false;
                }
            });
            takeScreenshotFromDriver(driver, "screenshots/filter_applied_"+timeNowTestStart.format(dateTimeFormatter)+".png");
        }

        // STEP 2.3 - Click item 'KENSINGTON ORGIT 72352 TRACKBALL'
        driver.findElement(By.xpath("//a[contains(@class, 'productName') and text()='"+PRODUCT1_NAME+"']")).click();
        // STEP 2.4 - Choose color 'red'
        driver.findElement(By.xpath("//span[contains(@class, 'productColor') and @title='RED']")).click();
        // STEP 2.5 - Put item in shopping cart
        driver.findElement(By.xpath("//button[@name='save_to_cart']")).click();

        // STEP 3   - Go back to home page
        driver.findElement(By.xpath("//nav[contains(@class, 'pages')]/a[text()='HOME']")).click();

        // STEP 4.1 - Scroll to popular items
        Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElement(By.xpath("//article[@id='popular_items']")));

        if (TAKE_SCREENSHOTS) {
            takeScreenshotFromDriver(driver, "screenshots/popular_items_view_"+timeNowTestStart.format(dateTimeFormatter)+".png");
        }

        // STEP 4.2 - Choose item 'HP ROAR PLUS WIRELESS SPEAKER'
        driver.findElement(By.xpath("//article[@id='popular_items']//div[starts-with(@name, 'popular_item')][.//*[contains(@class, 'productName') and text()='"+PRODUCT2_NAME+"']]//label[contains(@class, 'viewDetail')]")).click();
        // STEP 4.3 - Put amount of '2'
        WebElement quantityInput = driver.findElement(By.xpath("//input[@name='quantity']"));
        quantityInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), "2");
        // STEP 4.4 - Put item in shopping cart
        driver.findElement(By.xpath("//button[@name='save_to_cart']")).click();

        // STEP 5.1 - View shopping cart
        driver.findElement(By.xpath("//a[@id='shoppingCartLink']")).click();
        // STEP 5.2 - Check if both selected items are in shopping cart and check if total cost is correct
        WebElement product1Name = driver.findElement(By.xpath("//label[contains(@class, 'productName') and text()='"+PRODUCT1_NAME.toUpperCase()+"']"));
        Assert.assertTrue(product1Name.isDisplayed());
        WebElement product2Name = driver.findElement(By.xpath("//label[contains(@class, 'productName') and text()='"+PRODUCT2_NAME.toUpperCase()+"']"));
        Assert.assertTrue(product2Name.isDisplayed());
        WebElement totalCost = driver.findElement(By.xpath("//span[contains(text(), 'TOTAL')]/..//span[2][contains(text(), '$')]"));
        Assert.assertEquals(totalCost.getText(), "$399.97");

        if (TAKE_SCREENSHOTS) {
            takeScreenshotFromDriver(driver, "screenshots/shopping_cart_summary_"+timeNowTestStart.format(dateTimeFormatter)+".png");
        }

        // STEP 6.1 - Go to checkout
        driver.findElement(By.xpath("//button[@id='checkOutButton']")).click();
        // STEP 6.2 - Create new user
        driver.findElement(By.xpath("//button[@id='registration_btn']")).click();
        // Create username in format: pc<date><hour> //pc<YYMMDD><hhmmss>
        LocalDateTime timeNowUserRegistration = LocalDateTime.now();
        driver.findElement(By.xpath("//input[@name='usernameRegisterPage']")).sendKeys("pc"+timeNowUserRegistration.format(dateTimeFormatter));
        driver.findElement(By.xpath("//input[@name='emailRegisterPage']")).sendKeys("a.b@c.de");
        driver.findElement(By.xpath("//input[@name='passwordRegisterPage']")).sendKeys("Pc12345");
        driver.findElement(By.xpath("//input[@name='confirm_passwordRegisterPage']")).sendKeys("Pc12345");
        driver.findElement(By.xpath("//input[@name='first_nameRegisterPage']")).sendKeys("profi");
        driver.findElement(By.xpath("//input[@name='last_nameRegisterPage']")).sendKeys("Worker");
        Select countrySelect = new Select(driver.findElement(By.xpath("//select[@name='countryListboxRegisterPage']")));
        countrySelect.selectByVisibleText("Germany");
        driver.findElement(By.xpath("//input[@name='cityRegisterPage']")).sendKeys("Dresden");
        driver.findElement(By.xpath("//input[@name='allowOffersPromotion']")).click();

        if (TAKE_SCREENSHOTS) {
            takeScreenshotFromWebElement(driver.findElement(By.xpath("//div[@id='form']")), "screenshots/registration_summary_"+timeNowTestStart.format(dateTimeFormatter)+".png");
        }

        driver.findElement(By.xpath("//input[@name='i_agree']")).click();

        driver.findElement(By.xpath("//button[@id='register_btn']")).click();
        // STEP 6.3 - Check if shipping details are correct
        // Important! Need to wait for element to be visible as underlying JavaScript will refresh page content!
        WebElement userNameLabel = explicitWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='userDetails']//label[text()='profi Worker']")));
        Assert.assertTrue(userNameLabel.isDisplayed());

        WebElement addressLabel = driver.findElement(By.xpath("//div[@id='userDetails'][//label[text()='Dresden']][//label[text()='Germany']]"));
        Assert.assertTrue(addressLabel.isDisplayed());
        // STEP 6.4 - Click 'NEXT'
        driver.findElement(By.xpath("//button[contains(@class, 'nextBtn')]")).click();
        // STEP 6.5 - Choose payment option 'Master Credit' and fill values
        driver.findElement(By.xpath("//input[@name='masterCredit']")).click();

        // There seems to be an issue when using Locator.fill(String) on the card number input,
        // so we simulate a manual input field click and wait for the JavaScript to complete the animation.
        WebElement cardNumberInput = driver.findElement(By.xpath("//input[@name='card_number']"));
        cardNumberInput.click();
        explicitWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='card_number' and contains(@class, 'in-focus')]")));

        // Possible bug in Advantage Online Shop, maybe even intentionally:
        // After clicking in card number field and filling data, the next input after click in CVV field
        // will be ignored/skipped and not be displayed correctly. Inspecting the input field element gave this result:
        //  =>  Only after class attribute 'ng-dirty' has been set, which is typically after the first input symbol,
        //      following inputs are accepted for the CVV field.
        if (!cardNumberInput.getAttribute("class").contains("dirty")) {
            cardNumberInput.sendKeys("1");
            // Fallback cleanup of input in case the hacky method is redundant
            cardNumberInput.sendKeys(Keys.BACK_SPACE);
        }
        cardNumberInput.sendKeys("123456789123");

        WebElement cvvNumberInput = driver.findElement(By.xpath("//input[@name='cvv_number']"));
        cvvNumberInput.click();
        explicitWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='cvv_number' and contains(@class, 'in-focus')]")));

        if (!cvvNumberInput.getAttribute("class").contains("dirty")) {
            cvvNumberInput.sendKeys("1");
            cvvNumberInput.sendKeys(Keys.BACK_SPACE);
        }
        cvvNumberInput.sendKeys("123");

        Select monthSelect = new Select(driver.findElement(By.xpath("//select[@name='mmListbox']")));
        monthSelect.selectByVisibleText("04");
        Select yearSelect = new Select(driver.findElement(By.xpath("//select[@name='yyyyListbox']")));
        yearSelect.selectByVisibleText("2031");
        driver.findElement(By.xpath("//input[@name='cardholder_name']")).sendKeys("proficom");
        // STEP 6.6 - Click 'NEXT'
        driver.findElement(By.xpath("//button[@id='pay_now_btn_ManualPayment']")).click();

        // STEP 7   - Save and display order number and tracking number
        WebElement trackingNumberLabel = driver.findElement(By.xpath("//label[@id='trackingNumberLabel' and string-length(text())>0]"));
        WebElement orderNumberLabel = driver.findElement(By.xpath("//label[@id='orderNumberLabel' and string-length(text())>0]"));
        System.out.println("Tracking number: "+trackingNumberLabel.getText());
        System.out.println("Order number: "+orderNumberLabel.getText());
    }

    public static void takeScreenshotFromDriver(WebDriver driver, String path) throws IOException {
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(src, new File(path));
    }

    public static void takeScreenshotFromWebElement(WebElement element, String path) throws IOException {
        File src = element.getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(src, new File(path));
    }
}
