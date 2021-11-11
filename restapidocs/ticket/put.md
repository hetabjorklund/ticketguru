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
    "ticketType": 6,
    "invoice": 9
}
```

## Onnistuneen pyynnön vastaus

Ehto: Tietojen päivittäminen onnistuu

Koodi: 200 OK

Vastauksen sisältö:
```json
{
    "price": 0.0,
    "used": true,
    "ticketType": 6,
    "invoice": 9
}
```

## Virheellisen pyynnön vastaus

Ehto: Syötetyllä tunnisteella ei löydy lippua tietokannasta

Koodi: 404 NOT FOUND

Vastauksen sisältö:
```json
{
    "price": 0.0,
    "used": true,
    "ticketType": 6,
    "invoice": 9
}
```

TAI

Ehto: Lipputyypille tai tilaukselle annetulla tunnisteella ei löydy tietoa kannasta

Koodi: 400 BAD REQUEST

Vastauksen sisältö:
```json
{
    "price": 0.0,
    "used": true,
    "ticketType": 1000,
    "invoice": 9
}
```

TAI

Ehto: Lipputyyppi, johon lipun tietoja yritetään muuttaa, kuuluu tapahtumalle, joka on jo loppuunmyyty

Koodi: 400 BAD REQUEST

Vastauksen sisältö:
```json
{
    "message": "Event is already sold out",
    "status": "400"
}
```