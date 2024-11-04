package de.proficom.guitests;

import com.microsoft.playwright.*;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BuyProductsTest {

    //static final String URL = "https://www.advantageonlineshopping.com/#/";
    static final String URL = "http://172.16.15.213:8080/";
    static final boolean HEADLESS_MODE = true;
    static final boolean TAKE_SCREENSHOTS = false;
    static final boolean TAKE_RECORDING = false;
    static final String PRODUCT1_NAME = "Kensington Orbit 72352 Trackball";
    static final String PRODUCT2_NAME = "HP ROAR PLUS WIRELESS SPEAKER";

    static long beforeBrowserStartTS =0;
    static long beforeTestStartTS = 0;
    LocalDateTime timeNowTestStart = LocalDateTime.now();
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
    
    Playwright playwright;
    Browser browser;
    BrowserContext browserContext;
    Page page;


    @AfterMethod 
    public void cleanUp() {
        long testStoppedTS = System.currentTimeMillis();
        String testRunTimeWithBrowser = Long.toString(testStoppedTS - beforeBrowserStartTS);
        String testRunTime = Long.toString(testStoppedTS - beforeTestStartTS);

        
        try {
            Files.writeString(new File("timings.csv").toPath(), testRunTimeWithBrowser + "\t" + testRunTime + "\n", StandardOpenOption.APPEND, StandardOpenOption.CREATE);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        page.close();
        if (TAKE_RECORDING) {
            browserContext.close();
            String originalRecordingPath = page.video().path().toString();
            File newRecordingFile = new File("recordings/", "screen_recording_"+timeNowTestStart.format(dateTimeFormatter)+".webm");
            try {
                Files.move(Paths.get(originalRecordingPath), newRecordingFile.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        browser.close();
        playwright.close();

    }

    @Test
    public void RunTestCase() {
        
        beforeBrowserStartTS = System.currentTimeMillis();

        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(HEADLESS_MODE)
        ); 
        if (TAKE_RECORDING) {
            browserContext = browser.newContext(new Browser.NewContextOptions()
                    .setRecordVideoDir(Paths.get("recordings/"))
            );
            page = browserContext.newPage();

        } else {
            page = browser.newPage();
        }

        

        // STEP 0   - Go to home page
        page.navigate(URL);
        beforeTestStartTS = System.currentTimeMillis();

        // STEP 1   - Check if shopping cart is empty
        page.locator("//a[@id='shoppingCartLink']").click();
        Locator emptyText = page.locator("//div[@id='shoppingCart']/div/label[@translate='Your_shopping_cart_is_empty']");

        if (TAKE_SCREENSHOTS) {
            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get("screenshots/empty_shopping_cart_"+timeNowTestStart.format(dateTimeFormatter)+".png")));
        }

        assertThat(emptyText).hasText("Your shopping cart is empty");

        // STEP 2.1 - Go to home page category 'Mice'
        page.locator("//a[text()='CONTINUE SHOPPING']").click();
        page.locator("//div[@class='shop_now_slider']/span[text()='MICE']").click();
        // STEP 2.2 - Filter for Scroller type 'Scroll Ring' and 'Scroll Ball'
        page.locator("//li[contains(@*, 'attributesToShow')][.//*[contains(text(), 'SCROLLER TYPE')]]").click();
        page.locator("//label[text()='Scroll Ball']/../input").check();
        page.locator("//label[text()='Scroll Ring']/../input").check();
        page.waitForCondition(() -> page.locator("//*[contains(@class, 'productName')]").count() == 2);
        if (TAKE_SCREENSHOTS) {
            // We need to wait for JavaScript to apply the filter correctly
            
            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get("screenshots/filter_applied_"+timeNowTestStart.format(dateTimeFormatter)+".png")));
        }

        // STEP 2.3 - Click item 'KENSINGTON ORGIT 72352 TRACKBALL'
        page.locator("//a[contains(@class, 'productName') and text()='"+PRODUCT1_NAME+"']").click();
        // STEP 2.4 - Choose color 'red'
        page.locator("//span[contains(@class, 'productColor') and @title='RED']").click();
        // STEP 2.5 - Put item in shopping cart
        page.locator("//button[@name='save_to_cart']").click();

        // STEP 3   - Go back to home page
        page.locator("//nav[contains(@class, 'pages')]/a[text()='HOME']").click();

        // STEP 4.1 - Scroll to popular items
        page.locator("//article[@id='popular_items']").scrollIntoViewIfNeeded();

        if (TAKE_SCREENSHOTS) {
            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get("screenshots/popular_items_view_"+timeNowTestStart.format(dateTimeFormatter)+".png")));
        }

        // STEP 4.2 - Choose item 'HP ROAR PLUS WIRELESS SPEAKER'
        page.locator("//article[@id='popular_items']//div[starts-with(@name, 'popular_item')][.//*[contains(@class, 'productName') and text()='"+PRODUCT2_NAME+"']]//label[contains(@class, 'viewDetail')]").click();
        // STEP 4.3 - Put amount of '2'
        Locator quantityInput = page.locator("//input[@name='quantity']");
        quantityInput.fill("2");
        // STEP 4.4 - Put item in shopping cart
        page.locator("//button[@name='save_to_cart']").click();

        // STEP 5.1 - View shopping cart
        page.locator("//a[@id='shoppingCartLink']").click();
        // STEP 5.2 - Check if both selected items are in shopping cart and check if total cost is correct
        Locator product1Name = page.locator("//label[contains(@class, 'productName') and text()='"+PRODUCT1_NAME.toUpperCase()+"']");
        assertThat(product1Name).isVisible();
        Locator product2Name = page.locator("//label[contains(@class, 'productName') and text()='"+PRODUCT2_NAME.toUpperCase()+"']");
        assertThat(product2Name).isVisible();
        Locator totalCost = page.locator("//span[contains(text(), 'TOTAL')]/..//span[2][contains(text(), '$')]");
        assertThat(totalCost).hasText("$399.97");

        if (TAKE_SCREENSHOTS) {
            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get("screenshots/shopping_cart_summary_"+timeNowTestStart.format(dateTimeFormatter)+".png")));
        }

        // STEP 6.1 - Go to checkout
        page.locator("//button[@id='checkOutButton']").click();
        // STEP 6.2 - Create new user
        page.locator("//button[contains(@id, 'registration_btn')]").click();
        // Create username in format: pc<date><hour> //pc<YYMMDD><hhmmss>
        LocalDateTime timeNowUserRegistration = LocalDateTime.now();
        page.locator("//input[@name='usernameRegisterPage']").fill("pc"+timeNowUserRegistration.format(dateTimeFormatter));
        page.locator("//input[@name='emailRegisterPage']").fill("a.b@c.de");
        page.locator("//input[@name='passwordRegisterPage']").fill("Pc12345");
        page.locator("//input[@name='confirm_passwordRegisterPage']").fill("Pc12345");
        page.locator("//input[@name='first_nameRegisterPage']").fill("profi");
        page.locator("//input[@name='last_nameRegisterPage']").fill("Worker");
        page.locator("//select[@name='countryListboxRegisterPage']").selectOption("Germany");
        page.locator("//input[@name='cityRegisterPage']").fill("Dresden");
        page.locator("//input[@name='allowOffersPromotion']").uncheck();
        page.locator("//input[@name='i_agree']").check();

        if (TAKE_SCREENSHOTS) {
            page.locator("//div[@id='form']").screenshot(new Locator.ScreenshotOptions()
                    .setPath(Paths.get("screenshots/registration_summary_"+timeNowTestStart.format(dateTimeFormatter)+".png")));
        }

        page.locator("//button[contains(@id, 'register_btn')]").click();
        // STEP 6.3 - Check if shipping details are correct
        Locator userNameLabel = page.locator("//div[@id='userDetails']//label[text()='profi Worker']");

        // Important! Need to wait for element to be visible as underlying JavaScript will refresh page content!
        page.waitForCondition(() -> userNameLabel.isVisible());

        assertThat(userNameLabel).isVisible();
        Locator addressLabel = page.locator("//div[@id='userDetails'][//label[text()='Dresden']][//label[text()='Germany']]");
        assertThat(addressLabel).isVisible();
        // STEP 6.4 - Click 'NEXT'
        page.locator("//button[contains(@class, 'nextBtn')]").click();
        // STEP 6.5 - Choose payment option 'Master Credit' and fill values
        page.locator("//input[@name='masterCredit']").check();

        // There seems to be an issue when using Locator.fill(String) on the card number input,
        // so we simulate a manual input field click and wait for the JavaScript to complete the animation.
        Locator cardNumberInput = page.locator("//input[@name='card_number']");
        cardNumberInput.click();
        page.locator("//input[@name='card_number' and contains(@class, 'in-focus')]").waitFor();

        // Possible bug in Advantage Online Shop, maybe even intentionally:
        // After clicking in card number field and filling data, the next input after click in CVV field
        // will be ignored/skipped and not be displayed correctly. Inspecting the input field element gave this result:
        //  =>  Only after class attribute 'ng-dirty' has been set, which is typically after the first input symbol,
        //      following inputs are accepted for the CVV field.
        if (!cardNumberInput.getAttribute("class").contains("dirty")) {
            cardNumberInput.pressSequentially("1");
            // Fallback cleanup of input in case the hacky method is redundant
            cardNumberInput.press("Backspace");
        }
        

        Locator cvvNumberInput = page.locator("//input[@name='cvv_number']");
        cvvNumberInput.click();
        page.locator("//input[@name='cvv_number' and contains(@class, 'in-focus')]").waitFor();

        if (!cvvNumberInput.getAttribute("class").contains("dirty")) {
            cvvNumberInput.pressSequentially("1");
            cardNumberInput.press("Backspace");
        }


        page.locator("//div[@id='paymentMethod']").click();
        page.locator("//div[@id='paymentMethod']//input[@name='card_number']/../label[contains(@class, 'invalid')]").waitFor();
        page.locator("//div[@id='paymentMethod']").click();
        page.locator("//div[@id='paymentMethod']//input[@name='cvv_number']/../label[contains(@class, 'invalid')]").waitFor();

        cardNumberInput.pressSequentially("123456789123");
        cvvNumberInput.pressSequentially("123");
        cvvNumberInput.press("Control+A");
        cvvNumberInput.pressSequentially("123");



        page.locator("//select[@name='mmListbox']").selectOption("04");
        page.locator("//select[@name='yyyyListbox']").selectOption("2031");
        page.locator("//input[@name='cardholder_name']").fill("proficom");

        //fill out card number and cvv again

        // STEP 6.6 - Click 'NEXT'
        page.locator("//button[@id='pay_now_btn_ManualPayment']").click();

        // STEP 7   - Save and display order number and tracking number

        page.locator("//div[@class!='ng-hide' and ./div/@id='orderPaymentSuccess']").waitFor();
        Locator trackingNumberLabel = page.locator("//label[@id='trackingNumberLabel' and string-length(text())>0]");
        Locator orderNumberLabel = page.locator("//label[@id='orderNumberLabel' and string-length(text())>0]");
        System.out.println("Tracking number: "+trackingNumberLabel.textContent());
        System.out.println("Order number: "+orderNumberLabel.textContent());
    }
}
