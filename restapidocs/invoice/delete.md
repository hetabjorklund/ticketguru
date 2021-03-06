# Poista lasku

## Poista kaikki laskut

**URL** : `/invoices`

**Pyynnön tyyppi** : `DELETE`

**Autentikaatio vaadittu** : Kyllä, ADMIN tai USER

**Reunaehdot** : Laskuun ei saa liittyä lippuja.

### Onnistumisvastaus

**Ehto** : Laskujen poistaminen onnistui.

**HTTP-vastauskoodi** : `204 NO CONTENT`

### Virhevastaus

### 1
**Ehto** : Mitään laskuja ei ole, eli ei ole poistettavaa.

**HTTP-vastauskoodi** : `404 NOT FOUND`

**Vastaus** :
```json
{
    "message": "There are no invoices to delete"
}
```

### 2
**Ehto** : Yhdessä tai useammassa laskussa on lippuja.

**HTTP-vastauskoodi** : `403 FORBIDDEN`

**Vastaus** : 
```json
{
    "message": "One or more invoices have associated tickets, deletion forbidden"
}
```

## Poista tietty lasku

**URL** : `/invoices/{id}`

**Pyynnön tyyppi** : `DELETE`

**Autentikaatio vaadittu** : Kyllä, ADMIN tai USER

**Reunaehdot** : Pyynnössä täytyy lähettää haetun laskun id. Laskuun ei saa liittyä lippuja.

### Onnistumisvastaus

**Ehto** : Haettu lasku löytyy ja sen poistaminen onnistui.

**HTTP-vastauskoodi** : `204 NO CONTENT`

### Virhevastaus

### 1
**Ehto** : Haetulla id:llä ei löydy laskua.

**HTTP-vastauskoodi** : `404 NOT FOUND`

### 2
**Ehto** : Haettu lasku löytyy, mutta laskussa on lippuja.

**HTTP-vastauskoodi** : `403 FORBIDDEN`

**Vastaus** : 
```json
{
    "message": "Invoice has associated tickets, deletion forbidden"
}
```
