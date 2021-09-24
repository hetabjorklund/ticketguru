# Luo tapahtuma

Luo uuden tapahtuman mikäli samannimistä tapahtumaa ei ole jo olemassa.

**URL** : `/events`

**Pyynnön tyyppi** : `POST`

**Autentikaatio vaadittu** : EI

**Reunaehdot**

Tapahtumalla on oltava nimi, eli name-attribuutti ei saa puuttua. Kunhan name-attribuutti on, muita ei ole pakko olla. Jos jokin attribuutti puuttuu, se oletetaan nulliksi. Jos pyynnössä lähetetään sellaisia attribuutteja joita oliolla ei ole, ne jätetään huomiotta. 

**Esimerkkipyyntö** 

```json
{
    "name": "Muumirock",
    "address": "Muumimaailma",
    "maxCapacity": 20,
    "startTime": null,
    "endTime": null,
    "endOfPresale": null,
    "status": null,
    "description": "Pihoo!",
    "tickets": null
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
    "startTime": null,
    "endTime": null,
    "endOfPresale": null,
    "status": null,
    "description": "Pihoo!",
    "new": false
}
```

## Virhevastaus

### 1

**Ehto** : Samanniminen tapahtuma on jo olemassa.

**HTTP-vastauskoodi** : `409 CONFLICT`

**Esimerkkivastaus** : Palautetaan jo olemassaolevan samannimisen tapahtuman tiedot.

```json
{
    "id": 12,
    "name": "Muumirock",
    "address": "Muumimaailma",
    "maxCapacity": 20,
    "startTime": null,
    "endTime": null,
    "endOfPresale": null,
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
    "startTime": null,
    "endTime": null,
    "endOfPresale": null,
    "status": null,
    "description": "Pihoo!",
    "new": true
}
```

### 3

**Ehto** : Jokin attribuutti on väärän tyyppinen (esim. Boolean kun pitäisi olla String tai Array).

**HTTP-vastauskoodi** : `400 BAD REQUEST`

**Esimerkkivastaus**

```json
{
    "timestamp": "2021-09-24T09:34:56.617+00:00",
    "status": 400,
    "error": "Bad Request",    
    "message": "JSON parse error: Expected array or string.; nested exception is com.fasterxml.jackson.databind.exc.MismatchedInputException: Expected array or string.\n at [Source: (PushbackInputStream); line: 7, column: 21] (through reference chain: fi.paikalla.ticketguru.Entities.Event[\"endOfPresale\"])",
    "path": "/events"
}
```
