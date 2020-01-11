Aktuelle Probleme:

- Welche Fehlermeldung/seltsames Verhalten gibt es?

Im Moment treten folgende Probleme auf:

1. Bestimmte Wörter werden von Alexa nicht erkannt, bspw. one oder two. Diese Wörter haben wir momentan beim UserIntent durch onne und twwo ersetzt, so werden sie von Alexa erkannt.

2. Bei uns sind keine Antworten mit mehr als einem Wort möglich, besteht eine Antwort aus mehreren Worten, also z.B. mother in law, gibt Alexa eine Fehlermeldung aus. Im RegExpTester werden die Wörter aber korrekt zurückgegeben.



- Was muss man tun, um den Fehler zu reproduzieren (also z.B. welche Eingabe)?

1. Ersetzt man im UserIntent und bei den Patterns z.B. onne und twwo durch one und two, erkennt Alexa zu Beginn der Ausführung den User-Input nicht. Wenn Alexa am Anfang also fragt, ob ein oder zwei Spieler spielen, erkennt das System die Antworten one und two nicht, und man kann nicht weiter navigieren.

2. Antwortet man auf eine Frage statt nur mit der korrekten Antwort beispielsweise mit "I think it´s ...", antwortet Alexa mit "Hey, are you still there?" (repromptSpeech after 8 seconds).

- Wie sehen das AlexaSkillLog bzw. das catalina-Log aus?

Wir erhalten keine vollständigen Log-Infos.

- Tritt das Problem bei allen aus dem Team auf?

Ja.
