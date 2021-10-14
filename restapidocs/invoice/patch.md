# Päivitä lasku

Päivittää haetun laskun tiedot.

**URL** : `/invoices/{id}`

**Pyynnön tyyppi** : `PATCH`

**Autentikaatio vaadittu** : Kyllä, ADMIN tai USER

**Reunaehdot**

Pyynnössä täytyy lähettää muokattavan laskun id.

**Esimerkkipyyntö** 

Content-Type: application/json-patch+json
```json
[
    {"op":"replace","path":"/timestamp","value":"2000-10-07T18:36:25.3401761"}
]
```

## Onnistumisvastaus

**Ehto** : Laskun päivitys onnistui.

**HTTP-vastauskoodi** : `200 OK`

**Esimerkkivastaus** : Palautetaan päivitetyn laskun tiedot.

```json
{
    "invoiceId": 12,
    "timestamp": [
        2000,
        10,
        7,
        18,
        36,
        25,
        340176100
    ],
    "tguser": {
        "id": 8,
        "userName": "MaiMe",
        "new": false
    },
    "tickets": []
}
```

## Virhevastaus

### 1
**Ehto** : Haetulla id:llä ei löydy laskua.

**HTTP-vastauskoodi** : `404 NOT FOUND`

### 2
**Ehto** : Yritetään lähettää pyyntö, jossa laskun myyjän id:tä ei ole olemassa.

**HTTP-vastauskoodi** : `404 NOT FOUND`

### 3
**Ehto** : Content-type ei ole application/json-patch+json vaan esimerkiksi application/json.

**HTTP-vastauskoodi** : `415 UNSUPPORTED MEDIA TYPE`

### 4
**Ehto** : Jokin attribuutti on väärän tyyppinen (esim. Boolean kun pitäisi olla Array tai String).

**HTTP-vastauskoodi** : `400 BAD REQUEST`