# Playwright JavaScript
Dokumentation: [Offizielle Playwright-Dokumentation](https://playwright.dev/docs/intro)

## Was lief gut
* Aufzeichnungen können unkompliziert eingebunden werden via playwright.config.js und werden dann auch automatisch im Report angehängt
* console.log() Aufrufe werden automatisch im Report eingebunden, jedoch gebündelt ganz am Ende unter "Attachments"
* Die Tests sind schnell und es muss nur an wenigen Stellen mit der Synchronisation nachgeholfen werden
* Parallelisierung von Testläufen ist einfach durch die playwright.config.js
* Cross-Browser-Testing lässt sich durch die playwright.config.js schnell konfigurieren und ist in der default Konfigurationsdatei schon enthalten


## Was lief weniger gut
* Screenshots müssen explizit erzeugt und programmatisch an den Report angehängt werden
* Screenshots und Log-Nachrichten werden gebündelt im Report aufgeführt ohne Bezug zum Testablauf (keine Assoziation mit Schritten bzw. Schrittreihenfolge)
* Aufzeichnungen sind bei manchen Browsern zu schnell, um mitkommen zu können - einzelne Schritte oder Zustände sind dadurch kaum erkennbar (hier muss sich auf Screenshots verlassen werden)
* Die Ergebnisse schwanken zwischen den Worker Nodes stark - so lief zunächst im Chromium alles gut während bei Firefox und WebKit weiterhin Fehler auftraten
* An manchen Stellen musste programmatisch gewartet werden, z.B. bei Laden der URL, beim Aufklappen des Filter "Scroller Type"
* Sticky Header/ Navileisten sind auf Full Page Screenshots manchmal unschön verrutscht angezeigt und können so wichtige Felder überlagern
* In der JavaScript-Variante ist es sehr nervig, <code>await</code> überall voransetzen zu müssen
* Der globale default timeout hat mich Zeit gekostet, bis ich bemerkt habe, dass er der Grund für die Fehlschläge war
* Tests mit Firefox waren besonders flaky und langsam