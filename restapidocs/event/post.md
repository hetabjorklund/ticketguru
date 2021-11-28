# Luo tapahtuma

Luo uuden tapahtuman mikäli tapahtumaa ei ole jo olemassa.

**URL** : `/events`

**Pyynnön tyyppi** : `POST`

**Autentikaatio vaadittu** : Kyllä, ADMIN

**Reunaehdot**

Tapahtumalla on oltava nimi, eli name-attribuutti ei saa puuttua. Kunhan name-attribuutti on, muita ei ole pakko olla. Jos jokin attribuutti puuttuu, se oletetaan nulliksi. Jos pyynnössä lähetetään sellaisia attribuutteja joita oliolla ei ole, ne jätetään huomiotta. 

**Esimerkkipyyntö** 

```json
{
    "name": "Muumirock",
    "address": "Muumimaailma",
    "maxCapacity": 20,
    "startTime": "2021-12-31T21:00:00",
    "endTime": "2022-01-01T12:00:00",
    "endOfPresale": "2021-12-30T00:00:00",
    "status": null,
    "description": "Pihoo!",
    "ticketTypes": null
}
```

## Onnistumisvastaus

**Ehto** : Tapahtuman luominen onnistui.

**HTTP-vastauskoodi** : `201 CREATED`

**Esimerkkivastaus** : Palautetaan luodun tapahtuman tiedot.
```json
{
    "id": 12,
    "name": "Muumirock",
    "address": "Muumimaailma",
    "maxCapacity": 20,
    "startTime": "2021-12-31T21:00:00",
    "endTime": "2022-01-01T12:00:00",
    "endOfPresale": "2021-12-30T00:00:00",
    "status": null,
    "description": "Pihoo!",
    "new": false
}
```

## Virhevastaus

### 1

**Ehto** : Tapahtuma on jo olemassa (eli on jo tapahtuma jolla on sama nimi ja alkamisaika).

**HTTP-vastauskoodi** : `409 CONFLICT`

**Esimerkkivastaus** :  
```json
{
    "message": "Event already exists"
}
```

### 2

**Ehto** : Tapahtumalta puuttuu name-attribuutti.

**HTTP-vastauskoodi** : `400 BAD REQUEST`

**Esimerkkivastaus** : 
```json
{
    "message": "must not be empty.",
    "status": "400"
}
```

### 3

**Ehto** : Tapahtuman alkuaika, loppuaika tai lippujen ennakkomyynnin loppuaika on menneisyydessä.

**HTTP-vastauskoodi** : `400 BAD REQUEST`

**Esimerkkivastaus** :
```json
{
    "message": "must be a date in the present or in the future.",
    "status": "400"
}
```

### 4

**Ehto** : Jokin attribuutti on väärän tyyppinen (esim. Boolean kun pitäisi olla String tai Array).

**HTTP-vastauskoodi** : `400 BAD REQUEST`

**Esimerkkivastaus**
```json
{
    "timestamp": "2021-09-24T09:34:56.617+00:00",
    "status": 400,
    "error": "Bad Request",
    "trace": [stacktrace],    
    "message": "JSON parse error: Expected array or string.; nested exception is com.fasterxml.jackson.databind.exc.MismatchedInputException: Expected array or string.\n at [Source: (PushbackInputStream); line: 7, column: 21] (through reference chain: fi.paikalla.ticketguru.Entities.Event[\"endOfPresale\"])",
    "path": "/events"
}
```

# Luo tapahtumalle oviliput ennakkomyynnin päätyttyä

Tarkistaa, onko tapahtuman ennakkomyynti päättynyt ja jos on, luo ennakkomyynnistä ylijääneet liput ovilipuiksi myytäväksi tapahtuman ovella.

**URL** : `/events/{id}/tickets`

**Pyynnön tyyppi** : `POST` (tyhjä pyyntö ilman bodya, lipun hinta pyyntöparametrina)

**Autentikaatio vaadittu** : Kyllä, ADMIN tai USER

**Reunaehdot** : Tapahtuman id:n on oltava olemassa. Lipun hinta täytyy antaa pyynnön parametrina (avain 'price', arvo double-tyyppinen).

**Esimerkkipyyntö** : Tyhjä POST-pyyntö (ilman bodya) polkuun /events/{id}/tickets?price=25.0

## Onnistumisvastaus

**Ehto** : Ovilippujen luominen onnistui.

**HTTP-vastauskoodi** : `201 CREATED`

**Esimerkkivastaus** : Luodut oviliput palautetaan listana.
```json
[
    {
        "id": 36,
        "price": 25,
        "used": false,
        "ticketType": {
            "id": 16,
            "event": {
                "id": 59,
                "name": "Testitapahtuma6Vm1P",
                "address": "Testimaailma",
                "maxCapacity": 10,
                "startTime": [
                    2022,
                    12,
                    1,
                    0,
                    0
                ],
                "endTime": [
                    2022,
                    12,
                    1,
                    0,
                    0
                ],
                "endOfPresale": [
                    2022,
                    12,
                    1,
                    0,
                    0
                ],
                "status": null,
                "description": "Testin luoma tapahtuma"
            },
            "type": "ovilippu",
            "price": 25
        },
        "invoice": {
            "tguser": {
                "id": 2,
                "userName": "admin"
            },
            "timestamp": [
                2021,
                11,
                26,
                16,
                48,
                55,
                944263100
            ],
            "invoiceId": 54
        },
        "code": "14Bqx7tMLoKR"
    },
    {
        "id": 37,
        "price": 25,
        "used": false,
        "ticketType": {
            "id": 16,
            "event": {
                "id": 59,
                "name": "Testitapahtuma6Vm1P",
                "address": "Testimaailma",
                "maxCapacity": 10,
                "startTime": [
                    2022,
                    12,
                    1,
                    0,
                    0
                ],
                "endTime": [
                    2022,
                    12,
                    1,
                    0,
                    0
                ],
                "endOfPresale": [
                    2022,
                    12,
                    1,
                    0,
                    0
                ],
                "status": null,
                "description": "Testin luoma tapahtuma"
            },
            "type": "ovilippu",
            "price": 25
        },
        "invoice": {
            "tguser": {
                "id": 2,
                "userName": "admin"
            },
            "timestamp": [
                2021,
                11,
                26,
                16,
                48,
                55,
                944263100
            ],
            "invoiceId": 54
        },
        "code": "iM1Ut5VBRwIB"
    },
    {
        "id": 38,
        "price": 25,
        "used": false,
        "ticketType": {
            "id": 16,
            "event": {
                "id": 59,
                "name": "Testitapahtuma6Vm1P",
                "address": "Testimaailma",
                "maxCapacity": 10,
                "startTime": [
                    2022,
                    12,
                    1,
                    0,
                    0
                ],
                "endTime": [
                    2022,
                    12,
                    1,
                    0,
                    0
                ],
                "endOfPresale": [
                    2022,
                    12,
                    1,
                    0,
                    0
                ],
                "status": null,
                "description": "Testin luoma tapahtuma"
            },
            "type": "ovilippu",
            "price": 25
        },
        "invoice": {
            "tguser": {
                "id": 2,
                "userName": "admin"
            },
            "timestamp": [
                2021,
                11,
                26,
                16,
                48,
                55,
                944263100
            ],
            "invoiceId": 54
        },
        "code": "wJTlTERZWM9N"
    },
    {
        "id": 39,
        "price": 25,
        "used": false,
        "ticketType": {
            "id": 16,
            "event": {
                "id": 59,
                "name": "Testitapahtuma6Vm1P",
                "address": "Testimaailma",
                "maxCapacity": 10,
                "startTime": [
                    2022,
                    12,
                    1,
                    0,
                    0
                ],
                "endTime": [
                    2022,
                    12,
                    1,
                    0,
                    0
                ],
                "endOfPresale": [
                    2022,
                    12,
                    1,
                    0,
                    0
                ],
                "status": null,
                "description": "Testin luoma tapahtuma"
            },
            "type": "ovilippu",
            "price": 25
        },
        "invoice": {
            "tguser": {
                "id": 2,
                "userName": "admin"
            },
            "timestamp": [
                2021,
                11,
                26,
                16,
                48,
                55,
                944263100
            ],
            "invoiceId": 54
        },
        "code": "0L45O7p0DiAF"
    },
    {
        "id": 40,
        "price": 25,
        "used": false,
        "ticketType": {
            "id": 16,
            "event": {
                "id": 59,
                "name": "Testitapahtuma6Vm1P",
                "address": "Testimaailma",
                "maxCapacity": 10,
                "startTime": [
                    2022,
                    12,
                    1,
                    0,
                    0
                ],
                "endTime": [
                    2022,
                    12,
                    1,
                    0,
                    0
                ],
                "endOfPresale": [
                    2022,
                    12,
                    1,
                    0,
                    0
                ],
                "status": null,
                "description": "Testin luoma tapahtuma"
            },
            "type": "ovilippu",
            "price": 25
        },
        "invoice": {
            "tguser": {
                "id": 2,
                "userName": "admin"
            },
            "timestamp": [
                2021,
                11,
                26,
                16,
                48,
                55,
                944263100
            ],
            "invoiceId": 54
        },
        "code": "pBkdw6hswnur"
    },
    {
        "id": 41,
        "price": 25,
        "used": false,
        "ticketType": {
            "id": 16,
            "event": {
                "id": 59,
                "name": "Testitapahtuma6Vm1P",
                "address": "Testimaailma",
                "maxCapacity": 10,
                "startTime": [
                    2022,
                    12,
                    1,
                    0,
                    0
                ],
                "endTime": [
                    2022,
                    12,
                    1,
                    0,
                    0
                ],
                "endOfPresale": [
                    2022,
                    12,
                    1,
                    0,
                    0
                ],
                "status": null,
                "description": "Testin luoma tapahtuma"
            },
            "type": "ovilippu",
            "price": 25
        },
        "invoice": {
            "tguser": {
                "id": 2,
                "userName": "admin"
            },
            "timestamp": [
                2021,
                11,
                26,
                16,
                48,
                55,
                944263100
            ],
            "invoiceId": 54
        },
        "code": "2YaZSqStKmoO"
    },
    {
        "id": 42,
        "price": 25,
        "used": false,
        "ticketType": {
            "id": 16,
            "event": {
                "id": 59,
                "name": "Testitapahtuma6Vm1P",
                "address": "Testimaailma",
                "maxCapacity": 10,
                "startTime": [
                    2022,
                    12,
                    1,
                    0,
                    0
                ],
                "endTime": [
                    2022,
                    12,
                    1,
                    0,
                    0
                ],
                "endOfPresale": [
                    2022,
                    12,
                    1,
                    0,
                    0
                ],
                "status": null,
                "description": "Testin luoma tapahtuma"
            },
            "type": "ovilippu",
            "price": 25
        },
        "invoice": {
            "tguser": {
                "id": 2,
                "userName": "admin"
            },
            "timestamp": [
                2021,
                11,
                26,
                16,
                48,
                55,
                944263100
            ],
            "invoiceId": 54
        },
        "code": "PtsRSxSWw6LO"
    },
    {
        "id": 43,
        "price": 25,
        "used": false,
        "ticketType": {
            "id": 16,
            "event": {
                "id": 59,
                "name": "Testitapahtuma6Vm1P",
                "address": "Testimaailma",
                "maxCapacity": 10,
                "startTime": [
                    2022,
                    12,
                    1,
                    0,
                    0
                ],
                "endTime": [
                    2022,
                    12,
                    1,
                    0,
                    0
                ],
                "endOfPresale": [
                    2022,
                    12,
                    1,
                    0,
                    0
                ],
                "status": null,
                "description": "Testin luoma tapahtuma"
            },
            "type": "ovilippu",
            "price": 25
        },
        "invoice": {
            "tguser": {
                "id": 2,
                "userName": "admin"
            },
            "timestamp": [
                2021,
                11,
                26,
                16,
                48,
                55,
                944263100
            ],
            "invoiceId": 54
        },
        "code": "1fraQ8RCLiLW"
    },
    {
        "id": 44,
        "price": 25,
        "used": false,
        "ticketType": {
            "id": 16,
            "event": {
                "id": 59,
                "name": "Testitapahtuma6Vm1P",
                "address": "Testimaailma",
                "maxCapacity": 10,
                "startTime": [
                    2022,
                    12,
                    1,
                    0,
                    0
                ],
                "endTime": [
                    2022,
                    12,
                    1,
                    0,
                    0
                ],
                "endOfPresale": [
                    2022,
                    12,
                    1,
                    0,
                    0
                ],
                "status": null,
                "description": "Testin luoma tapahtuma"
            },
            "type": "ovilippu",
            "price": 25
        },
        "invoice": {
            "tguser": {
                "id": 2,
                "userName": "admin"
            },
            "timestamp": [
                2021,
                11,
                26,
                16,
                48,
                55,
                944263100
            ],
            "invoiceId": 54
        },
        "code": "cidtOd4DR0y6"
    },
    {
        "id": 45,
        "price": 25,
        "used": false,
        "ticketType": {
            "id": 16,
            "event": {
                "id": 59,
                "name": "Testitapahtuma6Vm1P",
                "address": "Testimaailma",
                "maxCapacity": 10,
                "startTime": [
                    2022,
                    12,
                    1,
                    0,
                    0
                ],
                "endTime": [
                    2022,
                    12,
                    1,
                    0,
                    0
                ],
                "endOfPresale": [
                    2022,
                    12,
                    1,
                    0,
                    0
                ],
                "status": null,
                "description": "Testin luoma tapahtuma"
            },
            "type": "ovilippu",
            "price": 25
        },
        "invoice": {
            "tguser": {
                "id": 2,
                "userName": "admin"
            },
            "timestamp": [
                2021,
                11,
                26,
                16,
                48,
                55,
                944263100
            ],
            "invoiceId": 54
        },
        "code": "JfnuGcy5l1bH"
    }
]
```

## Virhevastaus

### 1

**Ehto** : Tapahtuman id:llä ei löydy tapahtumaa.

**HTTP-vastauskoodi** : `404 NOT FOUND`

**Esimerkkivastaus** :  
```json
{
    "message": "Event not found"
}
```

### 2

**Ehto** : Tapahtumaan ei ole lippuja jäljellä, vaan tapahtuma on loppuunmyyty jo ennakkomyynnissä.

**HTTP-vastauskoodi** : `400 BAD REQUEST`

**Esimerkkivastaus** :  
```json
{
    "message": "The event is already sold out. There are no tickets left"
}
```

### 3

**Ehto** : Tapahtuman ennakkomyynti ei ole vielä loppunut.

**HTTP-vastauskoodi** : `400 BAD REQUEST`

**Esimerkkivastaus** :  
```json
{
    "message": "The presale has not yet ended"
}
```