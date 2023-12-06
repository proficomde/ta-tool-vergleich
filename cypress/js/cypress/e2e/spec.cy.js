
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
    beforeBrowserStartTS = Date.now()
    console.log("Hello World")
    // Visit website
    cy.visit("/")
    beforeTestStartTS = Date.now()

    // Check that cart is empty
    cy.xpath('//*[@id="menuCart"]').click()
    cy.xpath('//*[@id="shoppingCart"]/div/label').contains("Your shopping cart is empty")
    if (screenshot) {cy.screenshot({ capture: 'viewport'})}
    cy.xpath('//*[@id="shoppingCart"]/div/a').click()
    // Category "Mice"
    cy.xpath('//*[@id="miceImg"]').click() // select category
    cy.xpath("//div[@id='mobileSlide']//ul/li[2]").click() // open filter: scroller type
    cy.xpath("//div[@id='mobileSlide']//ul/li[2]//label[./text()='Scroll Ball']/../input").click({force: true}) // select scroll bar
    cy.xpath("//div[@id='mobileSlide']//ul/li[2]//label[./text()='Scroll Ring']/../input").click({force: true}) // select scroll ring

    cy.xpath('//div[@class="cell categoryRight"]/ul/li').should('have.length', 2)

    if (screenshot) {cy.screenshot({ capture: 'viewport'})}
    cy.xpath('//*[@id="26"]').click() // select mice
    cy.xpath('//*[@id="rabbit"]').click({ multiple: true }) // select color red
    cy.xpath('//*[@id="productProperties"]/div[4]/button').click() // add to cart
    
    // Back to home-screen
    cy.xpath('/html/body/div[3]/nav/a[1]').click()

    // Popular items
    cy.xpath('//*[@id="popular_items"]/h3').scrollIntoView() // scroll to popular items
    if (screenshot) {cy.screenshot({ capture: 'viewport'})}
    cy.xpath('//*[@id="details_21"]').click({force: true}) // select speaker
    cy.xpath('//*[@id="productProperties"]/div[2]/e-sec-plus-minus/div/div[3]').click({force: true}) // quantity 2
    cy.xpath('//*[@id="productProperties"]/div[4]/button').click({force: true}) // add to cart

    // Show shopping cart
    cy.xpath('//*[@id="shoppingCartLink"]').click() // click on shopping cart
    cy.contains('HP ROAR PLUS WIRELESS SPEAKER').should('exist')
    cy.contains('2').should('exist')
    cy.contains('KENSINGTON ORBIT 72352 TRACKBALL').should('exist')
    cy.contains('1').should('exist')
    cy.xpath('//*[@id="shoppingCart"]/table/tfoot/tr[1]/td[2]/span[2]').contains("$399.97")
    if (screenshot) {cy.screenshot({ capture: 'viewport'})}
    cy.xpath('//*[@id="checkOutButton"]').click() // Checkout

    // Create new user
    cy.xpath('//*[contains(@id, "registration_btn")]').click()
    var name = generateUsername(username)
    cy.xpath('//*[@id="formCover"]/div[1]/div[1]/sec-view[1]/div/input').type(name)
    if (screenshot) {cy.xpath('//*[@id="formCover"]/div[4]/span').screenshot()}
    cy.xpath('//*[@id="formCover"]/div[1]/div[1]/sec-view[2]/div/input').click({force: true}).type(mail)
    cy.xpath('//*[@id="formCover"]/div[1]/div[2]/sec-view[1]/div/input').type(password)
    cy.xpath('//*[@id="formCover"]/div[1]/div[2]/sec-view[2]/div/input').click({force: true}).type(password)
    cy.xpath('//*[@id="formCover"]/div[2]/div[1]/sec-view[1]/div/input').type(firstName)
    cy.xpath('//*[@id="formCover"]/div[2]/div[1]/sec-view[2]/div/input').click({force: true}).type(lastName)
    cy.xpath('//*[@id="formCover"]/div[3]/div[1]/sec-view[1]/div/select').select(country)
    cy.xpath('//*[@id="formCover"]/div[3]/div[1]/sec-view[2]/div/input').type(city)
    cy.xpath('//*[@id="formCover"]/div[4]/input').click() // no advertisement
    cy.xpath('//*[@id="formCover"]/sec-view/div/input').click() // agree conditions
    cy.xpath('//*[contains(@id, "register_btn")]').click() // Register

    // Check shipping details
    cy.xpath('//*[@id="userDetails"]/div[1]/label').contains(firstName + " " + lastName)
    cy.xpath('//*[@id="userDetails"]/div[2]/label[2]').contains(city)
    cy.xpath('//*[@id="userDetails"]/div[2]/label[3]').contains(country)
    cy.xpath('//*[@id="next_btn"]').click({multiple: true, force: true}) // click Next

    // Payment Master Credit
    cy.xpath('//*[@id="paymentMethod"]/div/div[1]/div[2]/input').click()
    cy.xpath('//*[@id="paymentMethod"]/div/div[4]/sec-form/div[2]/div/sec-view[1]/div/select').select(CCmonth)
    cy.xpath('//*[@id="paymentMethod"]/div/div[4]/sec-form/div[2]/div/sec-view[2]/div/select').select(CCyear)
    cy.xpath('//*[@id="paymentMethod"]/div/div[4]/sec-form/div[2]/sec-view/div/input').type(CCholder)
    cy.xpath('//*[@id="paymentMethod"]/div/div[4]/sec-form/div[1]/sec-view[2]/div/input').type(CVS,{force:true})
    cy.xpath('//*[@id="creditCard"]').type(CCnumber,{force:true})
    cy.xpath('//*[@id="pay_now_btn_ManualPayment"]').click() // click Pay Now

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
  var date = now.getDate()
  var month = ((now.getMonth() + 1) < 10 ? '0' : '') + (now.getMonth() + 1)
  var year = now.getFullYear()
  return username.concat(year,month,date,hours,minute)

}

