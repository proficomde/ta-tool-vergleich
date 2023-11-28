# Playwright (Java)

## Developer Notes
- Verständliche und gut lesbare [Dokumentation](https://playwright.dev/java/docs/intro)
- Ähnliche Methodik bei Testfallerstellung wie Selenium (Vorerfahrung in Nutzung von Selenium definitiv von Vorteil)
- Einfache Erstellung von Screenshots während Testfallausführung
  - Screenshots des gesamten DOM möglich, um alle Inhalte zu erfassen, auch Inhalte, die außerhalb des derzeitig sichtbaren Fensters liegen
  - Screenshot von einzelnem Element möglich, wobei der Bildausschnitt nur das Element beinhaltet und somit variabel groß ist 
  - Ansonsten: zum Element scrollen, damit es im Viewport liegt und dann Screenshot von aktuellen Browserfenster machen
- Videoaufzeichnungen von Testausführung nativ unterstützt und auch headless möglich
- Im Gegensatz zu Selenium ist die Standardausführung in Playwright im [headless mode](https://playwright.dev/java/docs/debug#headed-mode)
- Bei erstmaliger Testausführung werden automatisch die 3 Webdriver heruntergeladen (Chromium, Firefox, WebKit), auch wenn Chromium Driver für Testausführung spezifiziert wurde
- [Integrierung mit Selenium Grid](https://playwright.dev/java/docs/selenium-grid#introduction) möglich
- Interne Auto-Waits standardmäßig 30 Sekunden lang, wartend bis das Element [sichtbar](https://playwright.dev/java/docs/actionability#visible) wird
- Verglichen mit Selenium wirkt die Ausführung im "Headed Mode" mit Playwright subjektiv performanter/schneller
- Integrierte [Assertions](https://playwright.dev/java/docs/test-assertions)
- Testfallerstellung sehr intuitiv, wenn Vorerfahrung mit Selenium vorhanden ist (eigene Vorerfahrung: 2-tägiger Workshop mit Selenium)
- Subjektive Präferenz bei Wahlmöglichkeit zwischen Selenium und Playwright: Playwright statt Selenium

## Selenium vs Playwright

- Externe Gegenüberstellung beider Tools: https://research.aimultiple.com/playwright-vs-selenium/ ![](https://images.surferseo.art/8ae3af47-c68e-4426-942e-82ad6d6b65d4.png)