# FART - Forensic Application for Reading Text

Mijn moeder heeft mij geholpen met het bedenken van de afkorting.
Veel plezier

Er is een voorgebouwd FART.jar in /out/artifacts/FART. Om problemen te voorkomen bij het runnen in een andere IDE. 

En ik wou heel even ranten over hoe erg java als taal is, in the actionlistener van het explorerList object heb ik een call naar setCaretPosition(0) gedaan...
DIT IS DE ENIGE MANIER OM DE SCROLL BAR OMHOOG TE ZETTEN NA HET INLADEN VAN HET BESTAND. Ik heb minimaal 3 uur besteed aan het lezen van de (inhumaan geschreven) java oracle docs, om een manier te vinden waarmee je een JScrollBar object naar zijn minimale positie can zetten. Maar om een of andere reden is er een grotere prioriteit gegeven aan de positie van de cursor in een NIET EDITABLE JTextArea object ipv HET BOVENLIGGENDE JSCROLLPANE OBJECT.

en waarom dat is? You tell me...
(nog steeds een stuk beter dan tkinter)
