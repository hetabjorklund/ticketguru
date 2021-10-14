# Luo lasku

Luo uuden laskun.

**URL** : `/invoices`

**Pyynnön tyyppi** : `POST`

**Autentikaatio vaadittu** : Kyllä, ADMIN tai USER

**Reunaehdot**

Laskulla täytyy olla myyjä (TGUser).

**Esimerkkipyyntö** 

```json
{
    "tguser": {
            "id": 8,
            "userName": "MaiMe"
        }
}    
```

## Onnistumisvastaus

**Ehto** : Laskun luominen onnistui.

**HTTP-vastauskoodi** : `201 CREATED`

**Esimerkkivastaus** : Palautetaan luodun laskun tiedot.

```json
{
    "invoiceId": 12,
    "timestamp": "2021-10-06T18:31:16.1974145",
    "tguser": {
        "id": 8,
        "userName": "MaiMe",
        "new": false
    }
}
```

## Virhevastaus

### 1
**Ehto** : Myyjä (TGUser) puuttuu.

**HTTP-vastauskoodi** : `400 BAD REQUEST`

### 2
**Ehto** : Yritetään luoda lasku, jossa laskun myyjän id:tä ei ole olemassa.

**HTTP-vastauskoodi** : `404 NOT FOUND`

### 3
**Ehto** : Jokin attribuutti on väärän tyyppinen (esim. Boolean kun pitäisi olla Array tai String).

**HTTP-vastauskoodi** : `400 BAD REQUEST`

**Esimerkkivastaus**

```json
{
    "timestamp": "2021-09-30T13:39:18.736+00:00",
    "status": 400,
    "error": "Bad Request",
    "trace": [stacktrace],
    "message": "JSON parse error: Expected array or string.; nested exception is com.fasterxml.jackson.databind.exc.MismatchedInputException: Expected array or string.\n at [Source: (PushbackInputStream); line: 2, column: 19] (through reference chain: fi.paikalla.ticketguru.Entities.Invoice[\"timestamp\"])",
    "path": "/invoices"
}
```
