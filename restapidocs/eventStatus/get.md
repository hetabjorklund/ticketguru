# Listaa kaikki tapahtumien statukset
Listaa kaikki tietokantaan lisätyt tapahtumien statukset

**URL** : `/status`

**Pyynnön tyyppi** : `GET`

**Autentikaatio vaadittu** : ei

## Onnistumisvastaus

**Ehto** : Mikäli tietokannasta löytyy tapahtumastatus tai useampi

**HTTP-vastauskoodi** : `200 OK`

**Esimerkkivastaus**

```json
[
    {
        "id": 1,
        "statusName": "upcoming",
        "new": false
    }
]
```
### TAI

**Ehto** : Mikäli tietokannasta ei löydy yhtään statusta

**HTTP-vastauskoodi** : `200 OK`

**Esimerkkivastaus**

```json
[]
```

# Listaa tapahtumastatuksen id:n perusteella
Listaa yhden tapahtumastatuksen tietyn id:n perusteella

**URL** : `/status/:pk`

**Pyynnön tyyppi** : `GET`

**Autentikaatio vaadittu** : ei

## Onnistumisvastaus

**Ehto** : Mikäli tietokannasta löytyy status kyseisellä id:llä

**HTTP-vastauskoodi** : `200 OK`

**Esimerkkivastaus**

```json
{
    "id": 1,
    "statusName": "upcoming",
    "new": false
}
```
## Virhevastaus

**Ehto** : Mikäli tietokannasta ei löydy statusta kyseisellä id:llä

**HTTP-vastauskoodi** : `404 NOT FOUND`

**Esimerkkivastaus**

```json
{
    "message": "Status not found"
}
```

# Listaa tapahtumastatukseen liitetyt tapahtumat statuksen id:n perusteella
Listaa kaikki yhteen tapahtumastatukseen liitetyt tapahtumat

**URL** : `/status/:pk/events`

**Pyynnön tyyppi** : `GET`

**Autentikaatio vaadittu** : ei

## Onnistumisvastaus

**Ehto** : Mikäli tietokannasta löytyy status kyseisellä id:llä

**HTTP-vastauskoodi** : `200 OK`

**Esimerkkivastaus**

```json
[
    {
        "id": 4,
        "name": "Mörkörock",
        "address": "Yksinäiset vuoret",
        "maxCapacity": 1,
        "startTime": [
            2021,
            12,
            3,
            9,
            0
        ],
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
        "status": {
            "id": 1,
            "statusName": "upcoming",
            "new": false
        },
        "description": "Mörkö narisee yksin",
        "new": false
    },
    {
        "id": 6,
        "name": "Mysteerisoinnut",
        "address": "Jekkukamari",
        "maxCapacity": 300,
        "startTime": [
            2021,
            11,
            3,
            21,
            0
        ],
        "endTime": [
            2021,
            11,
            4,
            2,
            0
        ],
        "endOfPresale": [
            2021,
            10,
            31,
            12,
            0
        ],
        "status": {
            "id": 1,
            "statusName": "upcoming",
            "new": false
        },
        "description": "Yhteislaulutilaisuus",
        "new": false
    }
]
```

### TAI

**Ehto** : Mikäli tietokannasta löytyy status kyseisellä id:llä, mutta statukseen ei ole liitetty tapahtumia

**HTTP-vastauskoodi** : `200 OK`

**Esimerkkivastaus**

```json
{
    "message": "No associated events"
}
```

## Virhevastaus

**Ehto** : Mikäli tietokannasta ei löydy statusta kyseisellä id:llä

**HTTP-vastauskoodi** : `404 NOT FOUND`

**Esimerkkivastaus**

```json
{
    "message": "Status not found"
}
```