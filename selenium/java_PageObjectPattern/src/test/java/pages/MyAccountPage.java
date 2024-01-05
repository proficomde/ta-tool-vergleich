package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MyAccountPage extends PageBase{

    @FindBy(css = "[class='hi-user containMiniTitle ng-binding']") // !!!find xpath
    WebElement menuUser;

    @FindBy(xpath = "//label[@role='link'][normalize-space()='My account']")
    WebElement myAccount;

    @FindBy(xpath = "//button[@class='deleteMainBtnContainer a-button ng-scope']")
    WebElement deleteAccount;

    @FindBy(xpath = " //*[@class='deletePopupBtn deleteRed']")
    WebElement confirmDeleteAccount;

    public MyAccountPage(WebDriver driver) {
        super(driver);
    }

    public void clickOnMenuUserLink() {
        menuUser.click();
    }

    public void clickOnMyAccountLink() {
        myAccount.click();
    }

    public void clickOnDeleteAccountButton() {
        deleteAccount.click();
    }

    public void clickOnConfirmDeleteAccountButton() {
        confirmDeleteAccount.click();
    }
}
