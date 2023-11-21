// import chromedriver so that selenium can by itself open a chrome driver
const { SeleniumContainer } = require("@testcontainers/selenium");


// import this class from selenium
const { Builder } = require("selenium-webdriver");

(async function openChromeTest() {
  // open chrome browser
  console.log("Pulling image")
  const container = await new SeleniumContainer("selenium/standalone-chrome:112.0")
  .withRecording()
  .start();

  console.log("attach selenium to image")
  const driver = await new Builder()
    .forBrowser('chrome')
    .usingServer(container.getServerUrl())
    .build();

  console.log("run test")
  try {
    // go to example website
    await driver.get("https://example.com/");
  } finally {
    // close the chrome browser
    await driver.quit();
  }

  console.log("save recording")
  const stoppedContainer = await container.stop();
  await stoppedContainer.saveRecording("recording.mp4");
})();