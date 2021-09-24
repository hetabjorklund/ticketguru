# Luo tapahtuma

Luo uuden tapahtuman mikäli samannimistä tapahtumaa ei ole jo olemassa.

**URL** : `/events/{id}`

**Pyynnön tyyppi** : `DELETE`

**Autentikaatio vaadittu** : Ei

**Reunaehdot**

Tapahtuman id tulisi olla tunnettu.  

**Esimerkkipyyntö** 

Poista tapahtuma, jonka id on 2.

Tyhjä DELETE-tyyppin pyyntö kohteeseen "/events/2"

## Onnistumisvastaus

**Ehto** : Tapahtuman poisto onnistui.

**HTTP-vastauskoodi** : `200 OK`

**Esimerkkivastaus** : json: deleted: true.

```json
{
    deleted: true
}
```

## Virhevastaus

### 1

**Ehto** : Tapahtuman tunnistetta ei ole olemassa.

**HTTP-vastauskoodi** : `404 NOT FOUND`

**Esimerkkivastaus** : Palautetaan virheen aikakoodi, status, virheen kuvaus, stacktrace, viesti ja polku.

```json
{
    "timestamp": "2021-09-24T17:20:44.752+00:00",
    "status": 404,
    "error": "Not Found",
    "trace": "..... stacktrace",
    "message": "Event not found for this id :: 5",
    "path": "/events/5"
}
```

