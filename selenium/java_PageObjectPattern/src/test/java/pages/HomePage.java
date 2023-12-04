package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

public class HomePage extends PageBase{

    @FindBy(xpath = "//*[@id='miceImg']")
    WebElement miceCategory;

    @FindBy(xpath = "//article[@id='popular_items']")
    WebElement popularItems;
    @FindBy(xpath = "//label[@id='details_21']")
    WebElement selectSpeaker;
    

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void clickOnMiceCategory() {
        miceCategory.click();
    }

    public void selectSpeaker() {
        selectSpeaker.click();
    }

    public void scrollToPopularItems(WebDriver driver) {
        Actions actions = new Actions(driver);
        actions.moveToElement(popularItems);
    }
}
