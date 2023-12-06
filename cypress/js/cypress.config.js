const { defineConfig } = require("cypress");

module.exports = defineConfig({
  e2e: {
    setupNodeEvents(on, config) {
      // implement node event listeners here
    },
    //baseUrl: 'https://www.advantageonlineshopping.com'
    baseUrl: 'http://172.16.15.213:8080/'
  },
});
