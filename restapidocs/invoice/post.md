# Luo lasku

Luo uuden laskun.

**URL** : `/invoices`

**Pyynnön tyyppi** : `POST`

**Autentikaatio vaadittu** : EI

**Reunaehdot**

Ei reunaehtoja.

**Esimerkkipyyntö** 

```json
{
    "timestamp" : "2021-12-31T21:00:00",
    "tguser" : null,
    "tickets" : []
}    
```

## Onnistumisvastaus

**Ehto** : Laskun luominen onnistui.

**HTTP-vastauskoodi** : `201 CREATED`

**Esimerkkivastaus** : Palautetaan luodun laskun tiedot.

```json
{
    "invoiceId": 12,
    "timestamp": "2021-12-31T21:00:00",
    "tguser": null,
    "tickets": []
}
```

## Virhevastaus

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
