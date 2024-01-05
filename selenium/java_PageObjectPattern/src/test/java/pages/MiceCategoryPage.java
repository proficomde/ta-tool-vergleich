package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MiceCategoryPage extends PageBase{

    @FindBy(xpath = "//*[@ng-repeat='attrib in attributesToShow']")
    WebElement scrollerType;

    @FindBy(xpath = "//input[@name='scroller_type_0']")
    WebElement scrollerType0;

    @FindBy(xpath = "//input[@name='scroller_type_1']")
    WebElement scrollerType1;

    @FindBy(xpath = "//img[@id='26']")
    WebElement mouse;

    @FindBy(xpath = "//span[@title='RED']")
    WebElement mouseColor;

    @FindBy(xpath = "//a[@translate='HOME']")
    WebElement homeLink;

    public MiceCategoryPage(WebDriver driver) {
        super(driver);
    }

    public void chooseScrollRingScrollBall() {
        scrollerType.click();
        scrollerType0.click();
        scrollerType1.click();
    }

    public void chooseMouse() {
        mouse.click();
    }

    public void selectMouseColor() {
        mouseColor.click();
    }

    public void clickOnHomeLink() {
        homeLink.click();
    }
}
