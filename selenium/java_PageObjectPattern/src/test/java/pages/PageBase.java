package pages;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;

public class PageBase {
    WebDriver driver;
    static WebDriverWait explicitWait;

    public PageBase(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver,this);
        explicitWait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @FindBy(xpath = "//a[@id='shoppingCartLink']")
    WebElement menuCartLink;
    @FindBy(xpath = "//button[@name='save_to_cart']")
    WebElement addToCard;

    public void click(WebElement element){
        element.click();
    }
    public void clickOnMenuCartButton() {
        click(menuCartLink);
    }
    public void addToCart() {
        addToCard.click();
    }
    public void type(WebElement element, String text) {
        if (text != null) {
            //element.click();
            element.clear();
            element.sendKeys(text);
        }
    }

    public void pause(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void takeScreenshot() {
        File tmp = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File screenshot = new File("screenshots/screen" + System.currentTimeMillis() + ".png");
        try {
            Files.copy(tmp.toPath(), screenshot.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Screenshot takeScreenshotWithScrollDown () {
        Screenshot screen = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(ShootingStrategies.scaling(1.5f), 1000))
                .takeScreenshot(driver);
        try {
            ImageIO.write(screen.getImage(),"png",
                    new File("screenshots/screen" + System.currentTimeMillis() + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return screen;
    }

}
