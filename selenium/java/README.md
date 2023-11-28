# Selenium (Java)

## Developer Notes

- Im Vergleich zu Playwright müssen in Selenium einige Funktionen selbst gebaut oder "umständlich" gebaut werden, Beispiel beim Speichern von Screenshots, explizites Warten auf das Erscheinen von 2 Elementen (siehe `new ExpectedCondition<Boolean>()`) oder Select von Dropdown Menüs
- Recording von Testausführung
  - Kein natives Recording von Selenium bereitgestellt
  - Recording von Testausführung entweder extern, z.B. über Selenium Grid, oder über externe Bibliotheken (in diesen Fall jedoch nicht im headless mode)
  - Alternativ über Jenkins + Xvfb Plugin (nicht implementiert)
  - Alternativ mittels [video-recorder-java von SergeyPirgov](https://github.com/SergeyPirogov/video-recorder-java), der bei gescheiterter Testausführung Aufnahme anfertigt (nicht implementiert)
- Ansonsten sehr ähnlicher Aufbau und ähnliches Vorgehen bei Testfallerstellung zu Playwright; Testfallanpassung von Playwright zu Selenium intuitiv
- Screenshots von bestimmten Elementen müssen erst in den Viewport gebracht werden, bspw. durch Scrolling
  - Wenn `.getScreenshotAs()` auf ein `WebElement` angewendet wird, das außerhalb des aktuellen Viewports liegt, ist dieses Element nicht im Screenshot sichtbar; man muss das Element erst in den Viewport bringen