# Lipun tietojen päivittäminen

Päivittää lipun tiedot yksilöivän tunnisteen perusteella

URL : /tickets/{id}

Metodi : PUT

Vaatiiko autorisoinnin : Kyllä

Tietorajoitteet: Tuotavien tietojen Lipputyyppi ja Tilaus on oltava yhdistettävissä tietokannan tietoihin

Esimerkki pyynnöstä:

```json
{
    "price": 10.0,
    "used": true,
    "ticketType": 1,
    "invoice": 2
}
```

## Onnistuneen pyynnön vastaus

Ehto: Tietojen päivittäminen onnistuu

Koodi: 200 OK

Vastauksen sisältö:
```json
{
    "message": "Ticket modified succesfully",
    "status": "200"
}
```

## Virheellisen pyynnön vastaus

Ehto: Syötetyllä tunnisteella ei löydy lippua tietokannasta

Koodi: 404 NOT FOUND

Vastauksen sisältö:
```json
{
    "message": "Ticket with the given Id was not found",
    "status": "404"
}
```

TAI

Ehto: Lipputyypille tai tilaukselle annetulla tunnisteella ei löydy tietoa kannasta

Koodi: 400 BAD REQUEST

Vastauksen sisältö:
```json
{
    "message": "Invalid Tickettype or invoice",
    "status": "400"
}
```

TAI

Ehto: Annettu lasku tai lipputyyppi on pienempi kuin 1

Koodi: 400 BAD REQUEST

Vastauksen sisältö:

```json
{
    "message": "ticketType must be at least 1. invoice should be at least 1.",
    "status": "400"
}
```

# Lipun used-statuksen tarkastaminen ja käytetyksi merkitseminen

Tarkastaa, onko lippu käytetty. Mikäli lippua ei ole vielä käytetty, asetetaan se käytetyksi.

URL: /tickets/{code}/used

Metodi : PUT

Vaatiiko autorisoinnin : Kyllä

Tietorajoitteet: URLissa esitetty koodi on löydyttävä tietokannasta

Esimerkki pyynnöstä: URLissa oltava lipun koodi, erillistä RequestBodya ei tarvitse

## Onnistuneen pyynnön vastaus

Ehto: URLissa esitetty koodi löytyy tietokannasta, eikä lippua ole vielä käytetty

Koodi: 200 OK

Vastauksen sisältö:

```json
{
    "message": "Ticket is valid"
}
```

TAI

Ehto: URLiin syötetty koodi löytyy tietokannasta, mutta lippu on jo käytetty

Koodi: 400 BAD REQUEST

Vastauksen sisältö

```json
{
    "message": "Ticket has already been used. Ticket is not valid"
}
```

## Epäonnistuneen pyynnön vastaus

Ehto: URLissa syötettyä koodia ei löydy tietokannasta

Koodi: 400 BAD REQUEST

Vastauksen sisältö:

```json
{
    "message": "Ticket not found"
}
```

TAI

Ehto: Pyynnön lähettäjä ei ole auktorisoitu

Koodi: 401 Unauthorized

Vastauksen sisältö: ei sisältöä