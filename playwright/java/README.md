# Playwright (Java)

### Developer Notes
- Verständliche und gut lesbare [Dokumentation](https://playwright.dev/java/docs/intro)
- Ähnliche Methodik bei Testfallerstellung wie Selenium (Vorerfahrung in Nutzung von Selenium definitiv von Vorteil)
- Einfache Integrierung von Screenshot- und Videoaufnahmen während Testfallausführung
- Im Gegensatz zu Selenium ist die Standardausführung in Playwright im [headless mode](https://playwright.dev/java/docs/debug#headed-mode)
- Bei erstmaliger Testausführung werden automatisch die 3 Webdriver heruntergeladen (Chromium, Firefox, WebKit), auch wenn Chromium Driver für Testausführung spezifiziert wurde
- [Integrierung mit Selenium Grid](https://playwright.dev/java/docs/selenium-grid#introduction) möglich
- Interne Auto-Waits standardmäßig 30 Sekunden lang, wartend bis das Element [sichtbar](https://playwright.dev/java/docs/actionability#visible) wird
- Verglichen mit Selenium wirkt die Ausführung im "Headed Mode" mit Playwright subjektiv performanter/schneller
- Integrierte [Assertions](https://playwright.dev/java/docs/test-assertions)

### Selenium vs Playwright

- Externe Gegenüberstellung beider Tools: https://research.aimultiple.com/playwright-vs-selenium/ ![](https://images.surferseo.art/8ae3af47-c68e-4426-942e-82ad6d6b65d4.png)