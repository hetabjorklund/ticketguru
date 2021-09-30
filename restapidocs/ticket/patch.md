# Lipun merkitseminen käytetyksi

Merkitsee käyttämättömän lipun käytetyksi yksilöivän tunnisteen perusteella

URL : /tickets/{id}

Metodi : PATCH

Vaatiiko autorisoinnin : Kyllä

## Onnistuneen pyynnön vastaus

Ehto: Tietojen päivittäminen onnistuu

Koodi: 200 OK

Vastauksen sisältö:
```json
{
    "id": 11,
    "price": 30.0,
    "used": true,
    "ticketType": {
        "id": 6,
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
        "type": "eläkeläinen",
        "price": 30.0,
        "new": false
    },
    "invoice": {
        "invoiceId": 9,
        "timestamp": "2021-09-29T22:57:01.362751",
        "tguser": {
            "id": 8,
            "userName": "MaiMe",
            "new": false
        }
    },
    "new": false
}
```

## Virheellisen pyynnön vastaus

Ehto: Annettu lippu on jo merkitty käytetyksi

Koodi: 400 BAD REQUEST

Vastauksen sisältö:
```json
{
    "id": 11,
    "price": 30.0,
    "used": true,
    "ticketType": {
        "id": 6,
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
        "type": "eläkeläinen",
        "price": 30.0,
        "new": false
    },
    "invoice": {
        "invoiceId": 9,
        "timestamp": "2021-09-29T23:06:59.426993",
        "tguser": {
            "id": 8,
            "userName": "MaiMe",
            "new": false
        }
    },
    "new": false
}
```

TAI

Ehto: Annetulla tunnisteella ei ole lippua tietokannassa

Koodi: 404 NOT FOUND

Vastauksen sisältö: null