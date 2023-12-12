package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class OrderPaymentPage extends PageBase{

    @FindBy(xpath = "//*[@id='userDetails']")
    WebElement userDetailsText;

    @FindBy(xpath = "//div[@class='mobileBtnHandler']//button[@id='next_btn']")
    WebElement nextButton;

    @FindBy(xpath = "//input[@name='masterCredit']")
    WebElement masterCreditButton;

    @FindBy(xpath = "//input[@id='creditCard']")
    WebElement creditCardField;
    @FindBy(xpath = "//input[@name='cvv_number']")
    WebElement cvvNumberField;
    @FindBy(xpath = "//select[@name='mmListbox']")
    WebElement monthField;
    @FindBy(xpath = "//select[@name='yyyyListbox']")
    WebElement yearField;
    @FindBy(xpath = "//input[@name='cardholder_name']")
    WebElement cardholderField;

    @FindBy(xpath = "//button[@id='pay_now_btn_ManualPayment']")
    WebElement payNowButton;

    @FindBy(xpath = "//label[@id='trackingNumberLabel']")
    WebElement trackingNumber;

    @FindBy(xpath = "//*[@id='orderNumberLabel']")
    WebElement orderNumber;

    @FindBy(xpath = "//div[@class!='ng-hide' and ./div/@id='orderPaymentSuccess']")
    WebElement orderPaymentSuccessElement;

    public OrderPaymentPage(WebDriver driver) {
        super(driver);
    }

    public String getUserDetailsText() {
        return userDetailsText.getText();
    }

    public void clickOnNextButton() {
        nextButton.click();
    }

    public void selectMasterCreditButton() {
        masterCreditButton.click();
    }

    public void fillCardDataFields(String cardNumber, String cvvNumber, String month, String year, String cardholderName) {
        type(creditCardField, cardNumber);
        type(cvvNumberField, cvvNumber);
        selectItem(monthField, month);
        selectItem(yearField, year);
        type(cardholderField, cardholderName);
    }

    public void selectItem (WebElement webElement, String text) {
        
        Select selectableItem = new Select(webElement);
        selectableItem.selectByVisibleText(text);
        
        //click(webElement);
        //webElement.sendKeys(text);
    }

    public void clickOnPayNowButton() {
        payNowButton.click();
    }

    public void fillCardNumberCVVOneMoreTime(String cardNumber, String cvvNumber) {
        type(creditCardField, cardNumber);
        type(cvvNumberField, cvvNumber + Keys.chord(Keys.CONTROL,"a") + cvvNumber);
    }

    public void waitforOrderToBeFinished() {
        explicitWait.until(ExpectedConditions.visibilityOf(orderPaymentSuccessElement));
    }

    public String getTrackingNumber() {
        return trackingNumber.getText();
    }
    public String getOrderNumber() {
        return orderNumber.getText();
    }

}
