# Muokkaa tapahtumaa osittain

Muokkaa tietokannasta löytyvän tapahtumasta annetut tiedot.

**URL** : `/events/:pk`

**Pyynnön tyyppi** : `PATCH`

**Autentikaatio vaadittu** : Kyllä, ADMIN

**Reunaehdot**

Toimita kentät, joita haluat muokata.

```json
[
    {"op": "replace", "path": "/:key", "value": "string"}
]
```

**Esimerkkipyyntö** 

```json
[
    {"op": "replace", "path": "/name", "value": "EriRuisrock"},
    {"op": "replace", "path": "/address", "value": "Turunlinnankatu 50"},
    {"op": "replace", "path": "/description", "value": "Artistikattauksessa mm. X, Y ja Z"}
]
```

## Onnistumisvastaus

**HTTP-vastauskoodi** : `200 OK`

**Esimerkkivastaus**

```json
{
    "id": 2,
    "name": "EriRuisrock",
    "address": "Turunlinnankatu 50",
    "maxCapacity": 600,
    "startTime": "2021-12-03@09:00:00",
    "endTime": [
        2021,
        12,
        3,
        16,
        0
    ],
    "endOfPresale": [
        2021,
        11,
        27,
        16,
        0
    ],
    "status": null,
    "description": "Artistikattauksessa mm. X, Y ja Z",
    "new": false
}
```

## Virhevastaus

**Ehto** : Mikäli haettua tapahtuman id:tä ei löydy tietokannasta

**HTTP-vastauskoodi** : `404 NOT FOUND`

**Esimerkkivastaus** :

```json
{
    "message": "Event not found"
}
```

### tai

**Ehto** : Mikäli pyynnön bodyssa ei toimiteta tietoa

**HTTP-vastauskoodi** : `400 BAD REQUEST`

**Esimerkkivastaus** :

```json
{
    "timestamp": "2021-10-03T19:04:52.245+00:00",
    "status": 400,
    "error": "Bad Request",
    "trace": "org.springframework.http.converter.HttpMessageNotReadableException: Required request body is missing:...",
    "message": "Required request body is missing: ...",
    "path": "/events/2"
}
```