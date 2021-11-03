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