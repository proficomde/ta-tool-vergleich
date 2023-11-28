# Cypress (JavaScript)
## Developer notes
### Browser
- Cypress erkennt auf Betriebssystem verfügbare Browser
- Zusätzlich Electron-Browser in Cypress integriert 
- Bei Ausführung von `cypress run` standardmäßig mit Electron und headless
### Screenshots
- Wird `.screenshot()` auf ein Element außerhalb des Viewports angewendet, ist dieses im Screenshot nicht sichtbar
### XPath
- Ehemals offizielle Plugins von Cypress [cypress-xpath](https://www.npmjs.com/package/cypress-xpath) und [@cypress/xpath](https://www.npmjs.com/package/@cypress/xpath) sind seit Juni 2023 deprecated
- Für Projekt konnte cypress-xpath problemlos verwendet werden: `npm install -D cypress-xpath`
### Recording
- [Cypress Studio](https://docs.cypress.io/guides/references/cypress-studio) ist eine experimentelle Funktion und kann in Cypress-Konfiguration aktiviert werden
- Bei Anwendung traten keine Probleme auf
### Command Line
- Screenshots ein und ausschalten: `npx cypress run --env screenshot=true`
- Basis-URL abändern: `npx cypress run --config baseUrl=https://example.com/my-app`
