# Ticket - POST-dokumentaatio

## Lisää uusi lippu tietokantaan

URL : /tickets/

Metodi : POST

Vaatiiko valtuutuksen : Kyllä

Tietorajoitteet: Pyynnössä on oltava sekä lipputyypin(TicketType) että laskun(Invoice) yksilöivä tunniste.

Esimerkkipyyntö:
```json
{
    "price": 20.85,
    "used": false,
    "ticketType": 5,
    "invoice": 9
}
```

### Onnistuneen pyynnön vastaus

Ehto : Pyynnön sisältö on oikeellinen ja lippu saadaan sidottua kannassa olevaan lipputyyppiin ja laskuun.

Koodi : 201 CREATED

Esimerkki vastauksen sisällöstä:
```json
{
    "price": 20.85,
    "used": false,
    "ticketType": 5,
    "invoice": 9
}
```

### Virheellisen pyynnön vastaus

Ehto: Luotua lippua ei saada sidottua tietokannassa olevaan lipputyyppiin tai tilaukseen.

Koodi: 400 BAD REQUEST

Sisältö:
```json
{
    "price": 20.85,
    "used": false,
    "ticketType": 500,
    "invoice": 9
}
```