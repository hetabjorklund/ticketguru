# Päivitä lasku

Päivittää haetun laskun tiedot.

**URL** : `/invoices/{id}`

**Pyynnön tyyppi** : `PUT`

**Autentikaatio vaadittu** : Kyllä, ADMIN tai USER

**Reunaehdot**

Pyynnössä täytyy lähettää muokattavan laskun id. Laskulla täytyy olla myyjä (TGUser).

**Esimerkkipyyntö** 

```json
{
    "timestamp": "2023-10-01T15:49:24.136522",
    "tguser": {
        "id": 8,
        "userName": "UusiNimi"
    }
}
```

## Onnistumisvastaus

**Ehto** : Laskun päivitys onnistui.

**HTTP-vastauskoodi** : `200 OK`

**Esimerkkivastaus** : Palautetaan päivitetyn laskun tiedot.

```json
{
    "invoiceId": 9,
    "timestamp": "2023-10-01T15:49:24.136522",
    "tguser": {
        "id": 8,
        "userName": "UusiNimi",
        "new": false
    }
}
```

## Virhevastaus

### 1
**Ehto** : Haetulla id:llä ei löydy laskua.

**HTTP-vastauskoodi** : `404 NOT FOUND`

### 2
**Ehto** : Myyjä (TGUser) puuttuu.

**HTTP-vastauskoodi** : `400 BAD REQUEST`

**Esimerkkivastaus**
```json
{
    "timestamp": "2021-10-06T15:43:26.536+00:00",
    "status": 400,
    "error": "Bad Request",
    "trace": [stacktrace],
    "message": "Validation failed for object='invoice'. Error count: 1",
    "errors": [
        {
            "codes": [
                "NotNull.invoice.tguser",
                "NotNull.tguser",
                "NotNull"
            ],
            "arguments": [
                {
                    "codes": [
                        "invoice.tguser",
                        "tguser"
                    ],
                    "arguments": null,
                    "defaultMessage": "tguser",
                    "code": "tguser"
                }
            ],
            "defaultMessage": "must not be null",
            "objectName": "invoice",
            "field": "tguser",
            "rejectedValue": null,
            "bindingFailure": false,
            "code": "NotNull"
        }
    ],
    "path": "/invoices/9"
}
```

### 3
**Ehto** : Yritetään lähettää pyyntö, jossa laskun myyjän id:tä ei ole olemassa.

**HTTP-vastauskoodi** : `404 NOT FOUND`

**Vastaus** : TGUser does not exist

### 4
**Ehto** : Jokin attribuutti on väärän tyyppinen (esim. Boolean kun pitäisi olla Array tai String).

**HTTP-vastauskoodi** : `400 BAD REQUEST`

**Esimerkkivastaus**

```json
{
    "timestamp": "2021-10-01T13:07:54.628+00:00",
    "status": 400,
    "error": "Bad Request",
    "trace": [stacktrace],
    "message": "JSON parse error: Expected array or string.; nested exception is com.fasterxml.jackson.databind.exc.MismatchedInputException: Expected array or string.\n at [Source: (PushbackInputStream); line: 2, column: 18] (through reference chain: fi.paikalla.ticketguru.Entities.Invoice[\"timestamp\"])",
    "path": "/invoices/9"
}
```
