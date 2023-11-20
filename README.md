# testautomation-tooling

Dieses Repository stellt den Code für eine Tool-Evaluierung zwischen Selenium, Cypress und Playwright zur Verfügung.
Es wird ein mittel-komplexer Testfall im Advantage Online Shop automatisiert.

	• Prüfen dass Cart leer ist (Meldung "Your shopping cart is empty")
	• Kategorie "Mice" 
		○ Filtern nach Scroller Type: Scroll Ring & Scroll Ball
		○ Auswahl KENSINGTON ORBIT 72352 TRACKBALL
		○ Auswahl Farbe rot
		○ In den Warenkorb
	• Zurück zum Home-Bildschirm
	• Scrollen zu poplular Item
		○ Auswahl HP ROAR PLUS WIRELESS SPEAKER
		○ Anzahl 2
		○ In den Warenkorb
	• Anzeigen Warenkorb
		○ Prüfen der Infos
			▪ Items (beide Items sind vorhanden mit korrekter Menge)
			▪ Gesamtpreis
	• Checkout
		○ Neuen Benutzer Anlegen
			▪ Username: pc<datum><uhrzeit> //e.g. pc231120150440
			▪ Mail: a.b@c.de
			▪ Passwort: Pc12345
			▪ Name: profi Worker
			▪ Country: Germany
			▪ City: Dresden
			▪ Keine Werbung
			▪ Annehmen der AGB
		○ Prüfen der Shipping Details
			▪ Name: profi Worker
			▪ Adresse: Dresden Germany
		○ Next
		○ Payment Master Credit
			▪ Nummer: 123456789123
			▪ CVS: 123
			▪ Gültig bis: 04 2031 
			▪ Kartenhalter: proficom
		○ Next
	• Speichern & Ausgeben der Tracking Number & Order Number
