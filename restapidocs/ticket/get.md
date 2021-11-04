# Tickets-dokumentaatio

## Hae lippu lippukoodin perusteella

URL : /tickets/{code}

Metodi : GET

Vaatii valtuutuksen : Kyllä, USER tai ADMIN

### Onnistuneen pyynnön vastaus

Ehto : Syötetyllä koodilla ei löydy lippua tietokannasta.

Koodi : 404 NOT FOUND

Sisältö :

TAI

Ehto: Syötetyllä koodilla löytyy lippu tietokannasta.

Koodi : 200 OK

Sisältö :

```json
{
    "id": 3,
    "price": 120.45,
    "used": false,
    "ticketType": {
        "id": 1,
        "event": {
            "id": 4,
            "name": "testitapahtuma2",
            "address": "osoitekatu 1",
            "maxCapacity": 1000,
            "startTime": [
                2024,
                6,
                22,
                19,
                0
            ],
            "endTime": [
                2023,
                6,
                22,
                19,
                0
            ],
            "endOfPresale": [
                2022,
                6,
                22,
                19,
                0
            ],
            "status": {
                "id": 3,
                "statusName": "järjestetään"
            },
            "description": "tapahtumakuvaus"
        },
        "type": "aikuinen",
        "price": 20.45
    },
    "invoice": {
        "tguser": {
            "id": 1,
            "firstName": "etunimi",
            "lastName": "sukunimi",
            "userName": "user"
        },
        "timestamp": [
            2021,
            10,
            26,
            22,
            21,
            52,
            185369000
        ],
        "invoiceId": 2
    },
    "code": "QqnCXNYNlMBu"
}
```

## Hae kaikkien lippujen tiedot

URL: /tickets

Metodi: GET

Vaatii valtuutuksen: Kyllä, USER tai ADMIN

### Vastaus

Koodi : 200 OK

Sisältö :

```json
[
    {
        "id": 1,
        "price": 120.45,
        "used": false,
        "ticketType": {
            "id": 1,
            "event": {
                "id": 4,
                "name": "testitapahtuma2",
                "address": "osoitekatu 1",
                "maxCapacity": 1000,
                "startTime": [
                    2024,
                    6,
                    22,
                    19,
                    0
                ],
                "endTime": [
                    2023,
                    6,
                    22,
                    19,
                    0
                ],
                "endOfPresale": [
                    2022,
                    6,
                    22,
                    19,
                    0
                ],
                "status": {
                    "id": 3,
                    "statusName": "järjestetään"
                },
                "description": "tapahtumakuvaus"
            },
            "type": "aikuinen",
            "price": 20.45
        },
        "invoice": {
            "tguser": {
                "id": 1,
                "firstName": "etunimi",
                "lastName": "sukunimi",
                "userName": "user"
            },
            "timestamp": [
                2021,
                10,
                26,
                22,
                21,
                52,
                185369000
            ],
            "invoiceId": 2
        },
        "code": null
    },
    {
        "id": 2,
        "price": 120.45,
        "used": false,
        "ticketType": {
            "id": 1,
            "event": {
                "id": 4,
                "name": "testitapahtuma2",
                "address": "osoitekatu 1",
                "maxCapacity": 1000,
                "startTime": [
                    2024,
                    6,
                    22,
                    19,
                    0
                ],
                "endTime": [
                    2023,
                    6,
                    22,
                    19,
                    0
                ],
                "endOfPresale": [
                    2022,
                    6,
                    22,
                    19,
                    0
                ],
                "status": {
                    "id": 3,
                    "statusName": "järjestetään"
                },
                "description": "tapahtumakuvaus"
            },
            "type": "aikuinen",
            "price": 20.45
        },
        "invoice": {
            "tguser": {
                "id": 1,
                "firstName": "etunimi",
                "lastName": "sukunimi",
                "userName": "user"
            },
            "timestamp": [
                2021,
                10,
                26,
                22,
                21,
                52,
                185369000
            ],
            "invoiceId": 2
        },
        "code": "WUvMVNd9ZI7P"
    }
]
```