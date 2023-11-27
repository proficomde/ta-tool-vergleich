## Selenium (JavaScript)

## Developer Notes

* Bisheriger Standard für Frontend-Automatisierung in Browsern
* Breite Unterstützung div. Browser-Technologien
    * Chrome, Edge, Firefox, IE, Safary
* Kleine, aber gute API (z.B. kein integriertes Mocking)
    * Dokumentation für JavaScript teilweise noch lückenhaft
* implizite waits bei der identifizierung von Elementen von 0
    * Waits wurden im script aif 10000ms festgelegt
* Ausführung in RemoteBrowsern out of the Box durch SeleniumGrid oder Ausfürhung des Browsers in [testcontainer](https://node.testcontainers.org/modules/selenium/)
* assertions durch zugrundeliegendes Testing-Framework notwendig (hier mocha/chai)
* Screenshots zeigen nur den Inhalt im aktuellen ViewPort (Browser-Fenster) an => für größere Bereiche ist dann ein Scrollen notwendig.
