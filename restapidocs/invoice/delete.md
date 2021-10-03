# Poista lasku

## Poista kaikki laskut

**URL** : `/invoices`

**Pyynnön tyyppi** : `DELETE`

**Autentikaatio vaadittu** : EI

**Reunaehdot** : Ei ehtoja.

### Onnistumisvastaus

**Ehto** : Laskujen poistaminen onnistui.

**HTTP-vastauskoodi** : `200 OK`

### Virhevastaus

### 1
**Ehto** : Mitään laskuja ei ole, eli ei ole poistettavaa.

**HTTP-vastauskoodi** : `204 NO CONTENT`

### 2
**Ehto** : Tapahtui jokin virhe, jonka takia poistaminen ei onnistunut.

**HTTP-vastauskoodi** : `500 INTERNAL SERVICE ERROR`

## Poista tietty lasku

**URL** : `/invoices/{id}`

**Pyynnön tyyppi** : `DELETE`

**Autentikaatio vaadittu** : EI

**Reunaehdot** : Pyynnössä täytyy lähettää haetun laskun id.

### Onnistumisvastaus

**Ehto** : Haettu lasku löytyy ja sen poistaminen onnistui.

**HTTP-vastauskoodi** : `200 OK`

**Esimerkkivastaus** : Palautetaan poistetun laskun tiedot.

```json
{
    "invoiceId": 9,
    "timestamp": "2021-10-01T11:08:54.311649",
    "tguser": {
        "id": 8,
        "userName": "MaiMe",
        "new": false
    }
}
```

### Virhevastaus

### 1
**Ehto** : Haetulla id:llä ei löydy laskua.

**HTTP-vastauskoodi** : `404 NOT FOUND`

### 2
**Ehto** : Tapahtui jokin virhe, jonka takia poistaminen ei onnistunut.

**HTTP-vastauskoodi** : `500 INTERNAL SERVICE ERROR`