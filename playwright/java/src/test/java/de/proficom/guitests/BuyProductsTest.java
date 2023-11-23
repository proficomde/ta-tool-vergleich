package de.proficom.guitests;

import com.microsoft.playwright.*;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BuyProductsTest {

    static final String URL = "https://www.advantageonlineshopping.com/#/";
    static final boolean HEADLESS_MODE = false;
    static final boolean TAKE_SCREENSHOTS = false;
    static final boolean TAKE_RECORDING = false;
    static final String PRODUCT1_NAME = "Kensington Orbit 72352 Trackball";
    static final String PRODUCT2_NAME = "HP ROAR PLUS WIRELESS SPEAKER";

    @Test
    public void RunTestCase() {
        LocalDateTime timeNowTestStart = LocalDateTime.now();

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(HEADLESS_MODE)
        );

        BrowserContext browserContext;
        Page page;

        if (TAKE_RECORDING) {
            browserContext = browser.newContext(new Browser.NewContextOptions()
                    .setRecordVideoDir(Paths.get("recordings/"))
            );
            page = browserContext.newPage();

        } else {
            page = browser.newPage();
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");

        // STEP 0   - Go to home page
        page.navigate(URL);

        // STEP 1   - Check if shopping cart is empty
        page.locator("//a[@id='shoppingCartLink']").click();
        Locator emptyText = page.locator("//div[@id='shoppingCart']/div/label[@translate='Your_shopping_cart_is_empty']");
        assertThat(emptyText).hasText("Your shopping cart is empty");

        // STEP 2.1 - Go to home page category 'Mice'
        page.locator("//a[text()='CONTINUE SHOPPING']").click();
        page.locator("//div[@class='shop_now_slider']/span[text()='MICE']").click();
        // STEP 2.2 - Filter for Scroller type 'Scroll Ring' and 'Scroll Ball'
        page.locator("//li[contains(@*, 'attributesToShow')][.//*[contains(text(), 'SCROLLER TYPE')]]").click();
        page.locator("//label[text()='Scroll Ball']/../input").check();
        page.locator("//label[text()='Scroll Ring']/../input").check();
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
                    .setPath(Paths.get("screenshots/shopping_cart_summary_"+timeNowTestStart.format(dateTimeFormatter)+".png"))
                    .setFullPage(true));
        }

        // STEP 6.1 - Go to checkout
        page.locator("//button[@id='checkOutButton']").click();
        // STEP 6.2 - Create new user
        page.locator("//button[@id='registration_btn']").click();
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
            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get("screenshots/user_details_"+timeNowTestStart.format(dateTimeFormatter)+".png"))
                    .setFullPage(true));
        }

        page.locator("//button[@id='register_btn']").click();
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
        cardNumberInput.pressSequentially("123456789123");

        Locator cvvNumberInput = page.locator("//input[@name='cvv_number']");
        cvvNumberInput.click();
        page.locator("//input[@name='cvv_number' and contains(@class, 'in-focus')]").waitFor();

        if (!cvvNumberInput.getAttribute("class").contains("dirty")) {
            cvvNumberInput.pressSequentially("1");
            cardNumberInput.press("Backspace");
        }
        cvvNumberInput.pressSequentially("123");

        page.locator("//select[@name='mmListbox']").selectOption("04");
        page.locator("//select[@name='yyyyListbox']").selectOption("2031");
        page.locator("//input[@name='cardholder_name']").fill("proficom");
        // STEP 6.6 - Click 'NEXT'
        page.locator("//button[@id='pay_now_btn_ManualPayment']").click();

        // STEP 7   - Save and display order number and tracking number
        Locator trackingNumberLabel = page.locator("//label[@id='trackingNumberLabel' and string-length(text())>0]");
        Locator orderNumberLabel = page.locator("//label[@id='orderNumberLabel' and string-length(text())>0]");
        System.out.println("Tracking number: "+trackingNumberLabel.textContent());
        System.out.println("Order number: "+orderNumberLabel.textContent());

        if (TAKE_SCREENSHOTS) {
            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get("screenshots/order_summary_"+timeNowTestStart.format(dateTimeFormatter)+".png"))
                    .setFullPage(true));
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
}
