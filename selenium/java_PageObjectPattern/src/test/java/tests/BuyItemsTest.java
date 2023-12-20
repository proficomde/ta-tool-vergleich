package tests;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.Assert;
import org.junit.Test;

import pages.*;

public class BuyItemsTest extends TestBase{
    HomePage homePage;
    ShoppingCartPage shoppingCartPage;
    MiceCategoryPage miceCategoryPage;
    ProductPage productPage;
    LoginPage loginPage;
    MyAccountPage myAccountPage;
    OrderPaymentPage orderPaymentPage;

    @Test
    public void buyItemsTest(){
        
        // check that shopping cart is empty
        homePage = new HomePage(driver);
        homePage.clickOnMenuCartButton();
        shoppingCartPage = new ShoppingCartPage(driver);
        Assert.assertTrue("empty Shopping cart not found.", shoppingCartPage.getCartIsEmptyText().contains("Your shopping cart is empty") );
        //shoppingCartPage.pause(2000);
        shoppingCartPage.takeScreenshot();
        shoppingCartPage.clickOnContinueShoppingLink();


        // buy mouse
        homePage.clickOnMiceCategory();
        miceCategoryPage = new MiceCategoryPage(driver);
        miceCategoryPage.chooseScrollRingScrollBall();
        //miceCategoryPage.pause(2000);
        //miceCategoryPage.takeScreenshotWithScrollDown();
        miceCategoryPage.chooseMouse();
        miceCategoryPage.selectMouseColor();
        miceCategoryPage.addToCart();
        miceCategoryPage.clickOnHomeLink();
        //homePage.takeScreenshotWithScrollDown();
        
        // buy popular product
        homePage.scrollToPopularItems(driver);
        homePage.selectSpeaker();
        productPage = new ProductPage(driver);
        productPage.clickOnPlusButton();
        productPage.addToCart();

        //check shopping cart
        productPage.clickOnMenuCartButton();
        Assert.assertTrue(shoppingCartPage.getQuantityItem1().contains("2"));
        Assert.assertTrue(shoppingCartPage.getQuantityItem2().contains("1"));
        Assert.assertTrue(shoppingCartPage.getTotalPrice2().contains("$399.97"));

        double price1 = shoppingCartPage.getPriceItemNumberOne();
        double price2 = shoppingCartPage.getPriceItemNumberTwo();
        double totalPrice = shoppingCartPage.getTotalPrice();
        Assert.assertEquals(price1+price2, totalPrice, 0.1);

        shoppingCartPage.takeScreenshot();
        shoppingCartPage.clickOnCheckoutButton();

        //checkout - register user
        loginPage = new LoginPage(driver);
        loginPage.clickOnRegistrationButton();
        //loginPage.fillCreateAccountFields("pc281120231549", "a.b@c.de", "Pc12345",
        //        "profi", "Worker", "Germany", "Dresden");

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
        loginPage.fillCreateAccountFields("pc"+LocalDateTime.now().format(dateTimeFormatter), "a.b@c.de", "Pc12345",
                "profi", "Worker", "Germany", "Dresden");
        
        loginPage.clickOnCheckboxPromotion();
        loginPage.clickOnCheckboxUsePrivacyNotice();
        loginPage.clickOnRegisterButton();


        //checkout check user details
        orderPaymentPage = new OrderPaymentPage(driver);
        orderPaymentPage.getUserDetailsText();
        Assert.assertTrue(orderPaymentPage.getUserDetailsText().contains("profi Worker"));
        Assert.assertTrue(orderPaymentPage.getUserDetailsText().contains("Dresden\n" + "Germany"));
        orderPaymentPage.clickOnNextButton();

        // checkout - select payment
        orderPaymentPage.selectMasterCreditButton();
        orderPaymentPage.fillCardDataFields("123456789123", "123", "04", "2031", "proficom");
        //orderPaymentPage.pause(1000);
        orderPaymentPage.fillCardNumberCVVOneMoreTime("123456789123", "123");
        orderPaymentPage.clickOnPayNowButton();
        //orderPaymentPage.pause(2000);
        
        
        //checkout - check tacking and order number

        orderPaymentPage.waitforOrderToBeFinished();
        System.out.println("Tracking number is" + " " + orderPaymentPage.getTrackingNumber());
        System.out.println("Order number is" + " " + orderPaymentPage.getOrderNumber());
        
        /*
        myAccountPage = new MyAccountPage(driver);
        myAccountPage.clickOnMenuUserLink();
        myAccountPage.clickOnMyAccountLink();
        myAccountPage.clickOnDeleteAccountButton();
        myAccountPage.clickOnConfirmDeleteAccountButton();
        */
    }
}



