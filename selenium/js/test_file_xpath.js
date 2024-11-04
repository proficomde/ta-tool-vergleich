
//import required libraries
const { SeleniumContainer } = require("@testcontainers/selenium");
const assert = require('chai')
const { By, Builder, Select, until, Key} = require("selenium-webdriver");


//set test case specific variables
//const baseURI = "https://advantageonlineshopping.com"
const baseURI= "http://172.16.15.213:8080/"
const TAKE_SCREENSHOT = false

describe('AOS-TestScript', function() {
  let driver;
  let container;
  let actions;
  this.bail(true)
  this.timeout(60000);

  let beforeBrowserStartTS
  let beforeTestStartTS

  products = [ {prodName: 'Kensington Orbit 72352 Trackball', prodAmount: 1} , {prodName: 'HP ROAR PLUS WIRELESS SPEAKER', prodAmount: 2}]



  before('Pull image and attach browser', async function() {
    this.timeout(120000)
    console.log("Before")
    container = await new SeleniumContainer("selenium/standalone-chrome:126.0")
        
    .withEnvironment({ SCREEN_WIDTH: 1920,  SCREEN_HEIGHT: 1080})
    /*  
    .withAddedCapabilities({"chromeOptions": {
        "binary": "",
        "args": [
        "--disable-gpu",
        "--window-size=1920,1080"
        ]
    }})
      .withRecording()
      */
      .start();

    beforeBrowserStartTS = Date.now()
    
    driver = await new Builder()
              .forBrowser('chrome')
              .usingServer(container.getServerUrl())
            .build();
    actions = driver.actions({async: true});

    await driver.manage().setTimeouts({implicit: 10000});
  });

  after('Clean up', async function() {
    this.timeout(60000)

    let testStoppedTS = Date.now()
    testRunTimeWithBrowser = testStoppedTS - beforeBrowserStartTS
    testRunTime = testStoppedTS - beforeTestStartTS

    //write times to text-file for later processing
    console.log(`${testRunTimeWithBrowser}\t${testRunTime}`)
    require('fs').appendFileSync("timings.csv", `${testRunTimeWithBrowser}\t${testRunTime}\n`, "utf-8")


    await new Promise(r => setTimeout(r, 2000));
    const stoppedContainer = await container.stop();
    //await stoppedContainer.saveRecording("recording.mp4");
  });


  it('Open Browser', async function() {
    console.log("run test")
    await driver.get(baseURI);   
    beforeTestStartTS = Date.now()
  });


  it('check empty cart', async function() {
    let cartBtn = await driver.findElement(By.xpath("//a[@id='shoppingCartLink']"));
    await cartBtn.click();

    let shoppingCartTxt = await driver.findElement(By.xpath("//div[@id='shoppingCart']"));
    await driver.wait(until.elementIsVisible(shoppingCartTxt, 2000));
    assert.expect(await shoppingCartTxt.getText()).to.include("Your shopping cart is empty")
    
    TAKE_SCREENSHOT ? await driver.takeScreenshot().then((image) => saveScreenShot(image, "01_empty_cart.png")) : null

  });

  it('buy mouse', async function() {
    productName = products[0].prodName
    
    let homeBtn = driver.findElement(By.xpath("//a[@translate='HOME']"))
    await homeBtn.click();

    await driver.findElement(By.xpath("//div[@class='shop_now_slider']/span[text()='MICE']")).click();

    let filterMenu = await driver.findElement(By.xpath("//div[@id='mobileSlide']//ul/li[2]"))
    await filterMenu.click()



    let filterValues = ["Scroll Ball", "Scroll Ring"]

    for( filter of filterValues) {
      console.log(`clicking checkbox '${filter}'`)
      let filterCheckbox = await filterMenu.findElement(By.xpath(`.//label[./text()='${filter}']/../input`))
      await driver.wait(until.elementIsVisible(filterCheckbox), 2000);
      await filterCheckbox.click();
    }

    let productCatalog = await driver.findElement(By.xpath("//div[@class='cell categoryRight']/ul"))

    await driver.wait(until.stalenessOf(productCatalog.findElement(By.xpath("./li[2]"))), 2000)
    TAKE_SCREENSHOT ? await driver.takeScreenshot().then((image) => saveScreenShot(image, "02_filtered_mice.png")): null


    await productCatalog.findElement(By.xpath(`//a[./text()='${productName}']`)).click();
    await driver.findElement(By.xpath("//div[./h2/@translate='Color']//span[@title='RED']")).click();
    
    await driver.findElement(By.xpath("//button[@name='save_to_cart']")).click();
  });

  it('buy offer', async function() {
    productName = products[1].prodName
    await driver.findElement(By.xpath("//a[@translate='HOME']")).click();

    let popularItemsWe = await driver.findElement(By.xpath("//article[@id='popular_items']"))
    await actions.scroll(0,0,0,0, popularItemsWe).perform();

    TAKE_SCREENSHOT ? await driver.takeScreenshot().then((image) => saveScreenShot(image, "03_scroll_viewport.png")): null

    await popularItemsWe.findElement(By.xpath(`.//div[./p/text()='${productName}']/a`)).click();

    let quantityInput = driver.findElement(By.xpath("//input[@name='quantity' and @type='text']"))

    await quantityInput.click()
    await quantityInput.sendKeys(products[1].prodAmount)

    await driver.findElement(By.xpath("//button[@name='save_to_cart']")).click();
  });

  it('check filled shopping cart', async function() {

    await driver.findElement(By.xpath("//a[@id='shoppingCartLink']")).click();

    let shoppingCartTbl = driver.findElement(By.xpath("//article//div[@id='shoppingCart']/table"));
    await driver.wait(until.elementIsVisible(shoppingCartTbl, 2000));
    TAKE_SCREENSHOT ? await driver.takeScreenshot().then((image) => saveScreenShot(image, "04_filled_cart.png")): null

    for (product of products) {
      let tblRow = shoppingCartTbl.findElement(By.xpath(`.//tr[./td/label/text()='${product.prodName.toUpperCase()}']`))
      let prodQuantElement = tblRow.findElement(By.xpath(".//td[./label/text()='QUANTITY']"))
      assert.expect(await prodQuantElement.isDisplayed()).to.be.true
      assert.expect(await prodQuantElement.getText()).to.equal(`${product.prodAmount}`)
    }

    assert.expect(await shoppingCartTbl.findElement(By.xpath(".//td[./span/text()='TOTAL:']")).getText()).to.include("$399.97")

  });

  it('register new user', async function() {

    firstName = "Profi"
    lastName = "Worker"

    city = "Dresden"
    country = "Germany"

    await driver.findElement(By.xpath("//button[@id='checkOutButton']")).click();
    await driver.findElement(By.xpath("//button[contains(@id, 'registration_btn')]")).click();

    let registrationForm = driver.findElement(By.xpath("//div[@id='form']"))
      await registrationForm.findElement(By.xpath(".//input[@name='usernameRegisterPage']")).sendKeys(`pc${formatDate(new Date(), "YYMMDDhhmmss")}`)
      await registrationForm.findElement(By.xpath(".//input[@name='emailRegisterPage']")).sendKeys('a.b@c.de')

      await registrationForm.findElement(By.xpath(".//input[@name='passwordRegisterPage']")).sendKeys('Pc12345')
      await registrationForm.findElement(By.xpath(".//input[@name='confirm_passwordRegisterPage']")).sendKeys('Pc12345')

      await registrationForm.findElement(By.xpath(".//input[@name='first_nameRegisterPage']")).sendKeys(firstName)
      await registrationForm.findElement(By.xpath(".//input[@name='last_nameRegisterPage']")).sendKeys(lastName)

      let countrySelect = new Select (await registrationForm.findElement(By.xpath(".//select[@name='countryListboxRegisterPage']")))
      await countrySelect.selectByVisibleText(country)
      await registrationForm.findElement(By.xpath(".//input[@name='cityRegisterPage']")).sendKeys(city)

      // interaction with checkboxes can only by done by clicking
      // no function to select/unselect by value
      await registrationForm.findElement(By.xpath(".//input[@name='allowOffersPromotion']")).click()
      await registrationForm.findElement(By.xpath(".//input[@name='i_agree']")).click()
    
      TAKE_SCREENSHOT ? await registrationForm.takeScreenshot().then((image) => saveScreenShot(image, "04.5_registrationForm_big.png")): null 

    let registrationButton = await driver.findElement(By.xpath(".//button[contains(@id, 'register_btn')]"))
    driver.wait(until.elementIsEnabled(registrationButton), 2000)
    await registrationButton.click()


    let userSection = await driver.findElement(By.xpath("//article/div[@id='orderPayment']//div[@id='userSection']"))
    driver.wait(until.elementIsVisible(userSection))


    TAKE_SCREENSHOT ? await driver.takeScreenshot().then((image) => saveScreenShot(image, "05_userDetails.png")) : null

    assert.expect(await userSection.findElement(By.xpath(".//div[./img/@alt='user']/label")).getText()).to.equal(`${firstName} ${lastName}`)
    assert.expect(await userSection.findElement(By.xpath(".//div[./div/@class='iconCss iconHome']")).getText()).to.equal(`${city}\n${country}`)
  });

  it('initiate payment', async function() {

    await driver.findElement(By.xpath("//button[@id='next_btn']")).click();
    
    let paymentMethod = await driver.findElement(By.xpath("//div[@id='paymentMethod']"))
      await paymentMethod.findElement(By.xpath(".//input[@type='radio' and @name='masterCredit']")).click()

      let cardNumber = await paymentMethod.findElement(By.xpath(".//input[@name='card_number']"))  
      while(!(await cardNumber.getAttribute("class")).includes("dirty")) {
        await cardNumber.sendKeys("1")
        await cardNumber.sendKeys(Key.BACK_SPACE)
      }

      
      let cvvNumber = await paymentMethod.findElement(By.xpath(".//input[@name='cvv_number']"))      
      
      while(!(await cvvNumber.getAttribute("class")).includes("dirty")) {
        await cvvNumber.sendKeys("1")
        await cvvNumber.sendKeys(Key.BACK_SPACE)
      }
      await paymentMethod.click()
      await driver.wait(until.elementLocated(By.xpath("//div[@id='paymentMethod']//input[@name='card_number']/../label[@class='invalid']")))
      await paymentMethod.click()
      await driver.wait(until.elementLocated(By.xpath("//div[@id='paymentMethod']//input[@name='cvv_number']/../label[@class='invalid']")))

      await cardNumber.sendKeys(`123456789123`)
      await cvvNumber.sendKeys(`123${Key.chord(Key.CONTROL, "a")}123`)
      let expMonthSelect = new Select(await paymentMethod.findElement(By.xpath(".//select[@name='mmListbox']")))
      let expYearSelect = new Select(await paymentMethod.findElement(By.xpath(".//select[@name='yyyyListbox']")))

      expMonthSelect.selectByVisibleText("04")
      expYearSelect.selectByVisibleText("2031")

      await paymentMethod.findElement(By.xpath(".//input[@name='cardholder_name']")).sendKeys("proficom")

    await driver.findElement(By.xpath("//button[@id='pay_now_btn_ManualPayment']")).click()

    driver.wait(until.elementIsVisible(await driver.findElement(By.xpath("//div[@class!='ng-hide' and ./div/@id='orderPaymentSuccess']")), 2000))

    TAKE_SCREENSHOT ? await driver.takeScreenshot().then((image) => saveScreenShot(image, "06_sucessfullOrder.png")) : null

    console.log(`Tracking Number: ${await driver.findElement(By.xpath("//label[@id='trackingNumberLabel']")).getText()}`)
    console.log(`Tracking Number: ${await driver.findElement(By.xpath("//label[@id='orderNumberLabel']")).getText()}`)
  })





})


function saveScreenShot(image, fileName) {
  require('fs').writeFileSync(fileName, image, 'base64')
}

function formatDate(date, format) {
  const map = {
      MM: date.getMonth() + 1,
      DD: date.getDate(),
      YY: date.getFullYear().toString().slice(-2),
      YYYY: date.getFullYear(),
      hh: date.getHours(),
      mm: date.getMinutes(),
      ss: date.getSeconds()
  }

  return format.replace(/MM|DD|YY|YYYY|hh|mm|ss/gi, matched => map[matched])
}