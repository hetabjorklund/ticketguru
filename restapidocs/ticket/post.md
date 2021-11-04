# Ticket - POST-dokumentaatio

## Lisää uusi lippu tietokantaan

URL : /tickets/

Metodi : POST

Vaatiiko valtuutuksen : Kyllä

Tietorajoitteet: Pyynnössä on oltava sekä lipputyypin(TicketType) että laskun(Invoice) yksilöivä tunniste.

Esimerkkipyyntö:
```json
{
    "price": 20.45,
    "used": false,
    "ticketType": 1,
    "invoice": 2
}
```

### Onnistuneen pyynnön vastaus

Ehto : Pyynnön sisältö on oikeellinen ja lippu saadaan sidottua kannassa olevaan lipputyyppiin ja laskuun.

Koodi : 201 CREATED

Esimerkki vastauksen sisällöstä:
```json
{
    "message": "Ticket succesfully created",
    "status": "201"
}
```

### Virheellisen pyynnön vastaus

Ehto: Lipputyypiksi ja/tai laskun numeroksi on merkitty pienempi kuin 1.

Koodi: 400 BAD REQUEST

Sisältö:
```json
{
    "message": "invoice should be at least 1. ticketType must be at least 1.",
    "status": "400"
}
```

TAI

Ehto:  Lipputyyppiä tai laskua ei löytynyt kannasta.

Koodi: 400 BAD REQUEST

Sisältö:
```json
{
    "message": "Either ticket type or invoice was not found",
    "status": "400"
}
```

TAI

Ehto: Tapahtuma on jo myyty loppuun

Koodi: 400 BAD REQUEST

Sisältö:
```json
{
    "message": "The event is already sold out",
    "status": "400"
}
```