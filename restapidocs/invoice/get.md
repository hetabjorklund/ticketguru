# Hae lasku

## Hae kaikki laskut

**URL** : `/invoices`

**Pyynnön tyyppi** : `GET`

**Autentikaatio vaadittu** : Kyllä, ADMIN tai USER

**Reunaehdot** : Ei ehtoja.

### Onnistumisvastaus

**Ehto** : Laskujen hakeminen onnistui.

**HTTP-vastauskoodi** : `200 OK`

**Esimerkkivastaus** : Palautetaan listana kaikki laskut.
```json
[
    {
        "invoiceId": 9,
        "timestamp": "2021-09-29T15:12:33.944192",
        "tguser": {
            "id": 8,
            "userName": "MaiMe",
            "new": false
        }
    }
]
```

## Hae tietty lasku

**URL** : `/invoices/{id}`

**Pyynnön tyyppi** : `GET`

**Autentikaatio vaadittu** : Kyllä, ADMIN tai USER

**Reunaehdot** : Pyynnössä täytyy lähettää haetun laskun id.

### Onnistumisvastaus

**Ehto** : Haettu lasku löytyy.

**HTTP-vastauskoodi** : `200 OK`

**Esimerkkivastaus** : Palautetaan haetun laskun tiedot.

```json
{
    "invoiceId": 9,
    "timestamp": "2021-09-29T15:12:33.944192",
    "tguser": {
        "id": 8,
        "userName": "MaiMe",
        "new": false
    }
}
```

### Virhevastaus

**Ehto** : Haetulla id:llä ei löydy laskua.

**HTTP-vastauskoodi** : `404 NOT FOUND`



## Hae tietyn laskun kaikki liput

**URL** : `/invoices/{id}/tickets`

**Pyynnön tyyppi** : `GET`

**Autentikaatio vaadittu** : Kyllä, ADMIN tai USER

**Reunaehdot** : Pyynnössä täytyy lähettää haetun laskun id.

### Onnistumisvastaus

**Ehto** : Haettu lasku löytyy.

**HTTP-vastauskoodi** : `200 OK`

**Esimerkkivastaus** : Palautetaan listana haetulla laskulla olevat liput.

```json
[
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
            "timestamp": "2021-09-29T16:02:47.123182",
            "tguser": {
                "id": 8,
                "userName": "MaiMe",
                "new": false
            }
        },
        "new": false
    },
    {
        "id": 11,
        "price": 30,
        "used": false,
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
            "price": 30,
            "new": false
        },
        "invoice": {
            "invoiceId": 9,
            "timestamp": "2021-09-29T16:02:47.123182",
            "tguser": {
                "id": 8,
                "userName": "MaiMe",
                "new": false
            }
        },
        "new": false
    }
]
```

### Virhevastaus

**Ehto** : Haetulla id:llä ei löydy laskua.

**HTTP-vastauskoodi** : `404 NOT FOUND`
