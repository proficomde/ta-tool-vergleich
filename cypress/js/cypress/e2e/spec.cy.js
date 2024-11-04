
describe('Advantage Online Shopping', () => {

  const username = "pc"
  const mail = "a.b@c.de"
  const password = "Pc12345"
  const firstName = "profi"
  const lastName = "Worker"
  const country = "Germany"
  const city = "Dresden"
  const CCmonth = "04"
  const CCyear = "2031"
  const CCholder = "proficom"
  const CVS = "123"
  const CCnumber = "123456789123"
  
  const screenshot = Cypress.env('screenshot')
  // const screenshot = true


  let beforeBrowserStartTS
  let beforeTestStartTS

  it('AOS', () => {
    Cypress.config('defaultCommandTimeout', 10000)

    beforeBrowserStartTS = Date.now()
    console.log("Hello World")
    // Visit website
    cy.visit("/")
    beforeTestStartTS = Date.now()

    // Check that cart is empty
    cy.xpath('//a[@id="shoppingCartLink"]').click()
    cy.xpath('//div[@id="shoppingCart"]').contains("Your shopping cart is empty")
    if (screenshot) {cy.screenshot({ capture: 'viewport'})}
    cy.xpath('//div[@id="shoppingCart"]//a[@translate="CONTINUE_SHOPPING"]').click()
    
    // Category "Mice"
    cy.xpath('//div[@id="miceImg"]').click() // select category
    cy.xpath('//h4[contains(text(),"SCROLLER TYPE")]').click() // open filter: scroller type
    cy.xpath('//label[./text()="Scroll Ball"]/../input').click() // select scroll ball
    cy.xpath('//label[./text()="Scroll Ring"]/../input').click() // select scroll ring

    cy.xpath('//div[@class="cell categoryRight"]/ul/li').should('have.length', 2)

    if (screenshot) {cy.screenshot({ capture: 'viewport'})}
    cy.xpath('//a[./text()="Kensington Orbit 72352 Trackball"]').click() // select mice
    cy.xpath('//span[@title="RED"]').click() // select color red
    cy.xpath('//button[@name="save_to_cart"]').click() // add to cart
    
    // Back to home-screen
    cy.xpath('//div[@class="logo"]').click()

    // Popular items
    cy.xpath('//article[@id="popular_items"]').scrollIntoView() // scroll to popular items
    if (screenshot) {cy.xpath('//article[@id="popular_items"]').screenshot({ capture: 'fullPage'})}
    cy.xpath('//div[./p/text()="HP ROAR PLUS WIRELESS SPEAKER"]/a').click({force: true}) // select speaker
    cy.xpath('//div[@class="plus"]').click({force: true}) // quantity 2
    cy.xpath('//button[@name="save_to_cart"]').click({force: true}) // add to cart

    // Show shopping cart
    cy.xpath('//a[@id="shoppingCartLink"]').click() // click on shopping cart
    cy.contains('HP ROAR PLUS WIRELESS SPEAKER').should('exist')
    cy.contains('2').should('exist')
    cy.contains('KENSINGTON ORBIT 72352 TRACKBALL').should('exist')
    cy.contains('1').should('exist')
    cy.xpath('//td[./span[1]/text()="TOTAL:"]/span[2]').contains("$399.97")
    if (screenshot) {cy.screenshot({ capture: 'viewport'})}
    cy.xpath('//*[@id="checkOutButton"]').click() // Checkout

    // Create new user
    cy.xpath('//*[contains(@id, "registration_btn")]').click()
    var name = generateUsername(username)
    cy.xpath('//input[@name="usernameRegisterPage"]').type(name)
    if (screenshot) {cy.screenshot({ capture: 'fullPage'})}
    cy.xpath('//input[@name="emailRegisterPage"]').click({force: true}).type(mail)
    cy.xpath('//input[@name="passwordRegisterPage"]').type(password)
    cy.xpath('//input[@name="confirm_passwordRegisterPage"]').click({force: true}).type(password)
    cy.xpath('//input[@name="first_nameRegisterPage"]').type(firstName)
    cy.xpath('//input[@name="last_nameRegisterPage"]').click({force: true}).type(lastName)
    cy.xpath('//select[@name="countryListboxRegisterPage"]').select(country)
    cy.xpath('//input[@name="cityRegisterPage"]').type(city)
    cy.xpath('//input[@name="allowOffersPromotion"]').click() // no advertisement
    cy.xpath('//input[@name="i_agree"]').click() // agree conditions
    cy.xpath('//*[contains(@id, "register_btn")]').click() // Register

    // Check shipping details
    cy.xpath('//div[./img/@alt="user"]/label').should('be.visible').contains(firstName + " " + lastName)
    cy.xpath('//div[./div/@class="iconCss iconHome"]').contains(city)
    cy.xpath('//div[./div/@class="iconCss iconHome"]').contains(country)
    cy.xpath('//*[@id="next_btn"]').click({multiple: true, force: true}) // click Next

    // Payment Master Credit
    
    cy.xpath('//input[@name="masterCredit"]').click()
    cy.xpath('//input[@id="creditCard"]').type(CCnumber,{force:true})
    cy.xpath('//input[@name="cvv_number"]').type(CVS,{force:true})
    cy.xpath('//select[@name="mmListbox"]').select(CCmonth)
    cy.xpath('//select[@name="yyyyListbox"]').select(CCyear)
    cy.xpath('//input[@name="cardholder_name"]').type(CCholder)

    cy.xpath('//input[@id="creditCard"]').clear()
    cy.xpath("//div[@id='paymentMethod']").click()
    cy.xpath("//div[@id='paymentMethod']//input[@name='card_number']/../label[@class='invalid']").should("be.visible")
    cy.xpath('//input[@name="cvv_number"]').clear()
    cy.xpath("//div[@id='paymentMethod']").click()
    cy.xpath("//div[@id='paymentMethod']//input[@name='cvv_number']/../label[@class='invalid']").should("be.visible")

    cy.xpath('//input[@id="creditCard"]').type(CCnumber,{force:true})
    cy.xpath('//input[@name="cvv_number"]').clear().type(CVS,{force:true})


    cy.xpath('//button[@id="pay_now_btn_ManualPayment"]').click() // click Pay Now

    // Tracking number and Order number
    //cy.xpath("//div[@class!='ng-hide' and ./div/@id='orderPaymentSuccess']").should('be.visible')
    let trackingNumber=""
    let orderNumber=""
    cy.xpath('//*[@id="trackingNumberLabel"]').should('be.visible').invoke('text').then(($value) => {
      trackingNumber = $value
    }).then(cy.log)
    cy.xpath('//*[@id="orderNumberLabel"]').should('be.visible').invoke('text').then(($value) => {
      orderNumber = $value
    }).then(cy.log)

    
    console.log(`Tracking Number: ${trackingNumber}`)
    console.log(`Order Number: ${orderNumber}`)

  })

  
  after('Clean up', async function() {
  
    let testStoppedTS = Date.now()
    let testRunTimeWithBrowser = testStoppedTS - beforeBrowserStartTS
    let testRunTime = testStoppedTS - beforeTestStartTS

    //write times to text-file for later processing
    console.log(`${testRunTimeWithBrowser}\t${testRunTime}`)
    cy.writeFile('timings.csv', `${testRunTimeWithBrowser}\t${testRunTime}\n`, { flag: 'a+' })
  });


})

function generateUsername(){
  const now = new Date()
  var username = "pc"
  var hours = now.getHours()
  var minute = (now.getMinutes() < 10 ? '0' : '') + now.getMinutes()
  var sec = (now.getSeconds() < 10 ? '0' : '') + now.getSeconds()
  var date = now.getDate()
  var month = ((now.getMonth() + 1) < 10 ? '0' : '') + (now.getMonth() + 1)
  return username.concat(month,date,hours,minute,sec)

}

