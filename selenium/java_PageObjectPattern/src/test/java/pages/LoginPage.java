package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class LoginPage extends PageBase{
    @FindBy(xpath = "//*[contains(@id, 'registration_btn')]")
    WebElement registrationButton;

    @FindBy(xpath = "//div[@id='form']//input[@name='usernameRegisterPage']")
    WebElement usernameField;
    @FindBy(xpath = "//div[@id='form']//input[@name='emailRegisterPage']")
    WebElement emailField;
    @FindBy(xpath = "//div[@id='form']//input[@name='passwordRegisterPage']")
    WebElement passwordField;
    @FindBy(xpath = "//div[@id='form']//input[@name='confirm_passwordRegisterPage']")
    WebElement confirmPasswordField;
    @FindBy(xpath = "//div[@id='form']//input[@name='first_nameRegisterPage']")
    WebElement firstNameField;
    @FindBy(xpath = "//div[@id='form']//input[@name='last_nameRegisterPage']")
    WebElement lastNameField;

    @FindBy(xpath = "//div[@id='form']//select[@name='countryListboxRegisterPage']")
    WebElement countryField;

    @FindBy(xpath = "//div[@id='form']//input[@name='cityRegisterPage']")
    WebElement cityField;

    @FindBy(xpath = "//div[@id='form']//input[@name='allowOffersPromotion']")
    WebElement checkboxPromotion;

    @FindBy(xpath = "//div[@id='form']//input[@name='i_agree']")
    WebElement checkboxUsePrivacy;

    @FindBy(xpath = "//button[contains(@id, 'register_btn')]")
    WebElement registerButton;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void clickOnRegistrationButton() {
        registrationButton.click();
    }
    public void fillCreateAccountFields(String username, String email, String password, String fName, String lName,
                                        String country, String city) {
        type(usernameField, username);
        type(emailField, email);
        type(passwordField, password);
        type(confirmPasswordField, password);
        type(firstNameField, fName);
        type(lastNameField, lName);
        selectCountry(countryField, country);
        type(cityField, city);
    }

    private void selectCountry (WebElement webElement, String country) {
        
        Select selectableItem = new Select(webElement);
        selectableItem.selectByVisibleText(country);
    }

    public void clickOnCheckboxPromotion() {
        checkboxPromotion.click();
    }

    public void clickOnCheckboxUsePrivacyNotice() {
        checkboxUsePrivacy.click();
    }

    public void clickOnRegisterButton() {
        explicitWait.until(ExpectedConditions.elementToBeClickable(registerButton));
        registerButton.click();
    }
}
