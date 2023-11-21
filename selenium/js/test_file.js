const baseURI = "https://advantageonlineshopping.com"

// import chromedriver so that selenium can by itself open a chrome driver
const { SeleniumContainer } = require("@testcontainers/selenium");
var assert = require('chai')

// import this class from selenium
const { By, Builder } = require("selenium-webdriver");

describe('AOS-TestScript', function() {
  let driver;
  let container;
  let actions;
  this.bail(true)
  

  before('Pull image and attach browser', async function() {
    this.timeout(60000)
    console.log("Before")
    container = await new SeleniumContainer("selenium/standalone-chrome:112.0")
      .withEnvironment({ SCREEN_WIDTH: 1920,  SCREEN_HEIGHT: 1080})
      .withAddedCapabilities({"chromeOptions": {
        "binary": "",
        "args": [
        "--disable-gpu",
        "--window-size=1920,1080"
        ]
    }})
      .withRecording()
      .start();
    driver = await new Builder()
              .forBrowser('chrome')
              .usingServer(container.getServerUrl())
            .build();
    actions = driver.actions({async: true});

    await driver.manage().setTimeouts({implicit: 2000});
  });

  it('Open Browser', async function() {
    this.timeout(10000)
    console.log("run test")
    await driver.get(baseURI);

  });


  it('check empty cart', async function() {
    this.timeout(10000)
    let cartBtn = await driver.findElement(By.id("shoppingCartLink"));
    await cartBtn.click();

    let shoppingCartTxt = await driver.findElement(By.id("shoppingCart"));
    assert.expect(await shoppingCartTxt.getText()).to.include("Your shopping cart is empty")
    
  });

  it('buy mouse', async function() {
    this.timeout(5000)
    await driver.get(baseURI);
    //let homeBtn = driver.findElement(By.xpath("//div[@class='logo']/a[@href='#/']"))
    //actions.move({origin: homeBtn}).click().perform();

    let miceBtn = await driver.findElement(By.id("miceImg"))
    await miceBtn.click();

    let filterMenu = await driver.findElement(By.id("mobileSlide")).findElement(By.xpath(".//ul/li[2]"))
    await actions.move({origin: filterMenu}).click().perform();

    let fiterValues = ["Scroll Ball", "Scroll Ring"]

    for( filter of filterValues) {
      let filterCheckbox = await filterMenu.findElement(By.xpath("//label[./text()='Scroll Ring']/../input"))
      await filterCheckbox.click();
    }


  });

  after('Clean up', async function() {
    this.timeout(60000)
    const stoppedContainer = await container.stop();
    await stoppedContainer.saveRecording("recording.mp4");
  });
})