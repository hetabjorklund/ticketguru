# Tickets-dokumentaatio

## Hae lippu yksilöivän tunnisteen perusteella

URL : /tickets/{id}

Metodi : GET

Vaatii valtuutuksen : Kyllä

### Onnistuneen pyynnön vastaus

Ehto : Syötetyllä tunnisteella ei löydy lippua tietokannasta.

Koodi : 404 NOT FOUND

Sisältö : null

TAI

Ehto: Syötetyllä tunnisteella löytyy lippu tietokannasta.

Koodi : 200 OK

Sisältö :

```json
{
    "id": 10,
    "price": 20.9,
    "used": false,
    "ticketType": {
        "id": 5,
        "event": {
            "id": 2,
            "name": "Ruisrock",
            "address": "Savonlinnankatu 50",
            "maxCapacity": 600,
            "startTime": "2021-12-03T09:00:00",
            "endTime": "2021-12-03T16:00:00",
            "endOfPresale": "2021-11-27T16:00:00",
            "status": null,
            "description": "murderdeathkill",
            "new": false
        },
        "type": "aikuinen",
        "price": 20.9,
        "new": false
    },
    "invoice": {
        "invoiceId": 9,
        "timestamp": "2021-09-29T21:51:43.561781",
        "tguser": {
            "id": 8,
            "userName": "MaiMe",
            "new": false
        }
    },
    "new": false
}
```

## Tarkasta lipun tunnisteen perusteella, onko se käytetty

URL : /tickets/{id}/used

Metodi : GET

Vaatii valtuutuksen : Kyllä

### Onnistuneen pyynnön vastaus

Ehto : Syötetyllä tunnisteella ei löydy lippua tietokannasta.

Koodi : 404 NOT FOUND

Sisältö :

```json
{
    "ticketIdFound": false
}
```
TAI

Ehto : Syötetyllä tunnisteella löytyy lippu tietokannasta.

Koodi : 200 OK

Sisältö :
```json
{
    "used": false
}
```