package tests;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.BrowserWebDriverContainer.VncRecordingMode;
import org.testcontainers.containers.DefaultRecordingFileFactory;
import org.testcontainers.containers.VncRecordingContainer.VncRecordingFormat;
import org.testcontainers.utility.DockerImageName;

import java.io.File;
import java.time.Duration;

public class TestBase {
    public WebDriver driver;
    

    public static final String URL = "https://advantageonlineshopping.com/#/";
    //public static final String URL = "http://172.16.15.213:8080/";
    static File file = new File("recordings");


    @ClassRule
    public static BrowserWebDriverContainer chrome = new BrowserWebDriverContainer<>(DockerImageName.parse("selenium/standalone-chrome:119.0"))
                    .withCapabilities(new ChromeOptions())
                    .withRecordingMode(VncRecordingMode.RECORD_ALL, file, VncRecordingFormat.MP4)
                    .withRecordingFileFactory(new DefaultRecordingFileFactory());

    @Before
    public  void setupTest() {
        init();
    }

    public void init() {        
        
        driver = new RemoteWebDriver(chrome.getSeleniumAddress(), new ChromeOptions());
        driver.get(URL);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
    }

    @After
    public void tearDown(){
        driver.quit();
    }
}
