const { test, expect } = require('@playwright/test');

//const BASE_URL = "http://advantage.proficom.de:8080/";
const BASE_URL = "http://172.16.15.213:8080/"
const TAKE_SCREENSHOTS = false;

const PRODUCT_DATA = [
    ["HP ROAR PLUS WIRELESS SPEAKER", "BLUE", "2"],
    ["Kensington Orbit 72352 Trackball", "RED", "1"]
];

const TESTSTART_TIMESTAMP = Date.now();


let beforeBrowserStartTS
let beforeTestStartTS

test.beforeEach(async ({ page }) => {
    beforeBrowserStartTS = Date.now()
    await page.goto(BASE_URL);
    await page.waitForLoadState();
    console.log(`Time until initial page load: ${Date.now() - TESTSTART_TIMESTAMP} ms`);
});

test.afterEach(async ({ page }) => {
    
    let testStoppedTS = Date.now()
    let testRunTimeWithBrowser = testStoppedTS - beforeBrowserStartTS
    let testRunTime = testStoppedTS - beforeTestStartTS

    //write times to text-file for later processing
    console.log(`${testRunTimeWithBrowser}\t${testRunTime}`)
    require('fs').appendFileSync("timings.csv", `${testRunTimeWithBrowser}\t${testRunTime}\n`, "utf-8")
    
    await page.close();
    console.log(`Time until test finished: ${Date.now() - TESTSTART_TIMESTAMP} ms`);



});


test('orders a mouse and a speaker as new user', async ({ page }, testInfo) => {
    beforeTestStartTS = Date.now()
    await test.step('Shopping cart should be empty', async () => {
        // Go to Shopping Cart
        await page.locator("//A[@id='shoppingCartLink']").click();
        await page.waitForLoadState();

        // Check Shopping cart is empty: Text "Your shopping cart is empty"
        let cartTextContent = await page.locator("//DIV[@id='shoppingCart']/DIV/LABEL").textContent();
        expect(cartTextContent).toBe("Your shopping cart is empty");

        if (TAKE_SCREENSHOTS) {
            // Make a screenshot (Cart Text Element)
            const buffer = await page.locator("//DIV[@id='shoppingCart']/DIV/LABEL").screenshot();
            await testInfo.attach("01_shopping_cart_is_empty", { body: buffer, contentType: 'image/png' });
        }
    });

    await test.step('Filtering mice products for scroller types', async () => {
        // Go back to landing page
        await page.locator("//*[@class='logo']").click();

        // Go to category "Mice"
        await page.locator("//*[@id='miceImg']").click();

        // Filter for Scroller Type "Scroll Ball" & "Scroll Ring"
        await page.locator("//*[@id='accordionAttrib0']").click();
        await page.locator("//INPUT[@id='scroller_type_0']").click();
        await page.locator("//INPUT[@id='scroller_type_1']").click();

        // Check only two products are available after applying the filter - necessary for sync
        await expect(page.locator("//*[contains(@class, 'categoryRight')]//UL/LI")).toHaveCount(2);

        if (TAKE_SCREENSHOTS) {
            // Make a screenshot (Full Page)
            const buffer = await page.screenshot({ fullPage: true });
            await testInfo.attach("02_filter_set", { body: buffer, contentType: 'image/png' });
        }
    });

    await test.step('Adding a mouse to the cart', async () => {
        // Go to product details page of "Kensington Orbit 72352 Trackball"
        await page.locator(`//A[text()='${PRODUCT_DATA[1][0]}']`).click();

        // Configure color: RED
        await page.locator(`//*[@id='productProperties']//*[@title='${PRODUCT_DATA[1][1]}']`).click();

        // Add to cart
        await page.locator("//BUTTON[@name='save_to_cart']").click();
    });

    await test.step('Scrolling to popular items section on the landing page', async () => {
        // Go back to landing page
        await page.locator("//*[@class='logo']").click();

        // Scroll to section "Popular Items"
        await page.locator("//ARTICLE[@id='popular_items']").scrollIntoViewIfNeeded();

        if (TAKE_SCREENSHOTS) {
            // Make a screenshot (Viewport)
            const buffer = await page.screenshot();
            await testInfo.attach("03_popular_items_section_after_scroll", { body: buffer, contentType: 'image/png' });
        }
    });

    await test.step('Adding a speaker to the cart', async () => {
        // Go to product details page of "HP ROAR PLUS WIRELESS SPEAKER"
        await page.locator(`//*[text()='${PRODUCT_DATA[0][0]}']/following-sibling::A/LABEL[text()='View Details']`).click();

        // Increase quantity to "2" (by using + icon)
        await page.locator("//*[@class='plus' and @increment-value-attr='+']").click();

        // Add to cart
        await page.locator("//BUTTON[@name='save_to_cart']").click();
    });

    await test.step('Shopping cart should contain the mouse and speaker', async () => {
        // Go to shopping cart
        await page.locator("//A[@id='shoppingCartLink']").click();

        // Check that both products are added with the correct color and amount each
        let amountOfProductsInCart = (await page.locator("//*[@id='shoppingCart']/TABLE/TBODY/TR").all()).length;
        expect(amountOfProductsInCart).toBe(PRODUCT_DATA.length);
        for (let index = 1; index <= amountOfProductsInCart; index++) {
            let productName = await page.locator("//TR[" + index + "]//*[contains(@class,'productName')]").textContent();
            expect(productName).toBe(PRODUCT_DATA[index - 1][0].toUpperCase());
            let productColor = await page.locator("//TR[" + index + "]//*[contains(@class,'productColor')]").getAttribute("title");
            expect(productColor).toBe(PRODUCT_DATA[index - 1][1]);
            let productAmount = await page.locator("//TR[" + index + "]//*[text()='QUANTITY']/following-sibling::LABEL").textContent();
            expect(productAmount).toBe(PRODUCT_DATA[index - 1][2]);
        }

        // Check that the total price is correct
        let totalPrice = await page.locator("//*[text()='TOTAL:']/following-sibling::SPAN").textContent();
        expect(totalPrice).toBe("$399.97");

        if (TAKE_SCREENSHOTS) {
            // Make a screenshot (Cart Element)
            const buffer = await page.locator("//*[@id='shoppingCart']").screenshot();
            await testInfo.attach("04_shopping_cart_with_products", { body: buffer, contentType: 'image/png' });
        }
    });

    await test.step('Checking out the cart and registering as new user', async () => {
        // Go to checkout
        await page.locator("//BUTTON[@id='checkOutButton']").click();

        // Go to user registration
        await page.locator("//BUTTON[@id='registration_btnundefined']").click();

        // In the user registration form, enter details
        const username = `pc-${new Date().getDate()}${new Date().getMonth()}-${Math.floor(Math.random() * 99999)}`;
        console.log(`Username: ${username}`);
        await page.locator("//INPUT[@name='usernameRegisterPage']").fill(username);
        await page.locator("//INPUT[@name='emailRegisterPage']").fill("a.b@c.de");
        await page.locator("//INPUT[@name='passwordRegisterPage']").fill("Pc12345");
        await page.locator("//INPUT[@name='confirm_passwordRegisterPage']").fill("Pc12345");
        await page.locator("//INPUT[@name='first_nameRegisterPage']").fill("profi");
        await page.locator("//INPUT[@name='last_nameRegisterPage']").fill("Worker");
        await page.locator("//SELECT[@name='countryListboxRegisterPage']").selectOption("Germany");
        await page.locator("//INPUT[@name='cityRegisterPage']").fill("Dresden");

        const checkboxAllowOffers = await page.locator("//INPUT[@name='allowOffersPromotion']");
        let allowOffersCounter = 1;
        do {
            try {
                await checkboxAllowOffers.setChecked(false);
                break;
            } catch(error) {
                allowOffersCounter++;
            }
        } while(checkboxAllowOffers.isChecked() && allowOffersCounter <= 3);

        const checkboxAgreeToTOS = await page.locator("//INPUT[@name='i_agree']");
        let agreeTOSCounter = 1;
        do {
            try {
                await checkboxAgreeToTOS.setChecked(true);
                break;
            } catch(error) {
                agreeTOSCounter++;
            }
        } while(!checkboxAgreeToTOS.isChecked() && agreeTOSCounter <= 3);


        if (TAKE_SCREENSHOTS) {
            // Make a screenshot (Account Details Element)
            const buffer = await page.locator("//*[@id='registerPage']").screenshot();
            await testInfo.attach("05_create_account_page", { body: buffer, contentType: 'image/png' });
        }

        // Submit the form
        await page.locator("//BUTTON[@id='register_btnundefined']").click();
    });

    await test.step('Shipping details should be correct', async () => {
        // Check the shipping details: Name and Address
        let shippingName = await page.locator("//*[@id='userDetails']//IMG[contains(@src,'User.jpg')]/following-sibling::LABEL").textContent();
        expect(shippingName).toBe("profi Worker");
        let shippingCity = await page.locator("//*[@id='userDetails']//LABEL[contains(@data-ng-show,'user.cityName')]").textContent();
        expect(shippingCity).toBe("Dresden");
        let shippingCountry = await page.locator("//*[@id='userDetails']//LABEL[contains(@data-ng-show,'country.name')]").textContent();
        expect(shippingCountry).toBe("Germany");

        if (TAKE_SCREENSHOTS) {
            // Make a screenshot (Order Payment Element)
            const buffer = await page.locator("//*[@id='orderPayment']").screenshot();
            await testInfo.attach("06_order_payment_page", { body: buffer, contentType: 'image/png' });
        }
    });

    await test.step('Setting a payment method', async () => {
        // Go to payments
        await page.locator("//*[@id='next_btn']").click();

        // Set payment method to "Master Credit"
        await page.locator("//INPUT[@name='masterCredit']").setChecked(true);

        // Enter payment info
        const inputCardNumber = await page.locator("//INPUT[@id='creditCard']");
        const inputCardNumberInvalid = await page.locator("//INPUT[@id='creditCard']/following-sibling::LABEL[contains(@class,'invalid')]")
        let cardNumberCounter = 1;
        do {
            await inputCardNumber.fill("123456789123");
            await inputCardNumber.blur();
            cardNumberCounter++;
        } while(await inputCardNumberInvalid.isVisible() && cardNumberCounter <= 3);
        
        const inputCvvNumber = await page.locator("//INPUT[@name='cvv_number']");
        const inputCvvNumberInvalid = await page.locator("//INPUT[@name='cvv_number']/following-sibling::LABEL[contains(@class,'invalid')]")
        let cvvNumberCounter = 1;
        do {
            await inputCvvNumber.fill("123");
            await inputCvvNumber.blur();
            cvvNumberCounter++;
        } while(await inputCvvNumberInvalid.isVisible() && cvvNumberCounter <= 3);

        await page.locator("//SELECT[@name='mmListbox']").selectOption("04");
        await page.locator("//SELECT[@name='yyyyListbox']").selectOption("2031");
        await page.locator("//INPUT[@name='cardholder_name']").fill("proficom");

        if (TAKE_SCREENSHOTS) {
            // Make a screenshot (Order Payment Element)
            const buffer = await page.locator("//*[@id='orderPayment']").screenshot();
            await testInfo.attach("07_payment_method", { body: buffer, contentType: 'image/png' });
        }

        // Submit order
        await page.locator("//BUTTON[@id='pay_now_btn_ManualPayment']").click();
    });

    await test.step('Order should be successful', async () => {
        // Check Order was successful
        await expect(page.locator("//*[@id='orderPaymentSuccess']")).toBeVisible();

        // Save and log the tracking number & order number
        let trackingNumber = await page.locator("//LABEL[@id='trackingNumberLabel']").textContent();
        console.log(`Tracking Number: ${trackingNumber}`);
        let orderNumber = await page.locator("//LABEL[@id='orderNumberLabel']").textContent();
        console.log(`Order Number: ${orderNumber}`);

        if (TAKE_SCREENSHOTS) {
            // Make a screenshot (Order Payment Success Element)
            const buffer = await page.locator("//*[@id='orderPaymentSuccess']").screenshot();
            await testInfo.attach("08_order_finished", { body: buffer, contentType: 'image/png' });
        }
    });
});