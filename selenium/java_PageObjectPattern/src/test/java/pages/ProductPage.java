package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ProductPage extends PageBase{

    @FindBy(xpath = "//*[@class='plus']")
    WebElement plusButton;

    public ProductPage(WebDriver driver) {
        super(driver);
    }

    public void clickOnPlusButton() {
        plusButton.click();
    }
}
