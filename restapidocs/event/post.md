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

**Esimerkkivastaus** : Palautetaan jo olemassaolevan tapahtuman tiedot.

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

### 2

**Ehto** : Tapahtumalta puuttuu name-attribuutti.

**HTTP-vastauskoodi** : `400 BAD REQUEST`

**Esimerkkivastaus**

```json
{
    "id": null,
    "name": null,
    "address": "Muumimaailma",
    "maxCapacity": 20,
    "startTime": "2021-12-31T21:00:00",
    "endTime": "2022-01-01T12:00:00",
    "endOfPresale": "2021-12-30T00:00:00",
    "status": null,
    "description": "Pihoo!",
    "new": true
}
```

### 3
**Ehto** : Tapahtuman alkuaika, loppuaika tai lippujen ennakkomyynnin loppuaika on menneisyydessä.

**HTTP-vastauskoodi** : `400 BAD REQUEST`

**Esimerkkivastaus**
```json
{
    "id": null,
    "name": "Mörkörock",
    "address": "Yksinäiset vuoret",
    "maxCapacity": 1,
    "startTime": "2000-12-31T21:00:00",
    "endTime": "2022-01-01T12:00:00",
    "endOfPresale": "2021-12-30T00:00:00",
    "status": null,
    "description": "Hngngnggggg",
    "new": true
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
