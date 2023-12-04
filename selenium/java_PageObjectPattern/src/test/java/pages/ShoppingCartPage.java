package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ShoppingCartPage extends PageBase {

    @FindBy(xpath = "//label[@class='roboto-bold ng-scope']")
    WebElement cartIsEmptyText;

    @FindBy(xpath = "//a[@class='a-button ng-scope']")
    WebElement continueShopping;

    @FindBy(xpath = "(//p[@class='price roboto-regular ng-binding'][normalize-space()='$339.98'])[2]")
    WebElement priceNumberOne;

    @FindBy(xpath = "//td[@class='smollCell']//p[@class='price roboto-regular ng-binding'][normalize-space()='$59.99']")
    WebElement priceNumberTwo;

    @FindBy(xpath = "//span[@class='roboto-medium ng-binding'][normalize-space()='$399.97']")
    WebElement totalPrice;

    @FindBy(xpath = "//*[@id='checkOutButton']")
    WebElement checkoutButton;

    @FindBy(xpath = "//tbody/tr[1]/td[5]")
    WebElement quantitySpeaker;
    @FindBy(xpath = "//tbody/tr[2]/td[5]")
    WebElement quantityMouse;


    public ShoppingCartPage(WebDriver driver) {
        super(driver);
    }

    public String getCartIsEmptyText() {
        return cartIsEmptyText.getText();
    }


    public void clickOnContinueShoppingLink() {
        continueShopping.click();
    }


    public double getPrice(WebElement element) {
        double number = Double.parseDouble(element.getText().replace("$", ""));
        return number;

    }

    public double getPriceItemNumberOne() {
        double element = getPrice(priceNumberOne);
        return element;
    }

    public double getPriceItemNumberTwo() {
        double element = getPrice(priceNumberTwo);
        return element;
    }

    public double getTotalPrice() {
        double element = getPrice(totalPrice);
        return element;
    }

    public String getTotalPrice2() {
        return totalPrice.getText();
    }


    public void clickOnCheckoutButton() {
        checkoutButton.click();
    }

    public String getQuantityItem1() {
return quantitySpeaker.getText();
    }

    public String getQuantityItem2() {
        return quantityMouse.getText();
    }
}
