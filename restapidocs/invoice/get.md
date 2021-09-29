# Hae lasku

## Hae kaikki laskut

**URL** : `/invoices`

**Pyynnön tyyppi** : `GET`

**Autentikaatio vaadittu** : EI

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

## Hae yksittäinen lasku

**URL** : `/invoices/{id}`

**Pyynnön tyyppi** : `GET`

**Autentikaatio vaadittu** : EI

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
