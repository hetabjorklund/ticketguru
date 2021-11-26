# Muokkaa tapahtuman statuksen nimeä

Muokkaa tietokannasta löytyvän statuksen nimeä.

**URL** : `/status/:pk`

**Pyynnön tyyppi** : `PATCH`

**Autentikaatio vaadittu** : kyllä, `ADMIN`

**Reunaehdot**

Toimita operaation tyyppi ja kenttä jota haluat muokata.

**Esimerkkipyyntö** 

Content-Type: application/json-patch+json

```json
[
    {"op": "replace", "path": "/statusName", "value": "EDITcanceled"}
]
```

## Onnistumisvastaus

**Ehto** : Statuksen muokkaus onnistui

**HTTP-vastauskoodi** : `200 OK`

**Esimerkkivastaus**

```json
{
    "id": 1,
    "statusName": "EDITcanceled"
}
```

## Virhevastaus

**Ehto** : Mikäli annettua statusta ei löydy tietokannasta

**HTTP-vastauskoodi** : `404 NOT FOUND`

**Esimerkkivastaus** :

```json
{
    "message" : "Status not found"
}
```

### tai

**Ehto** : Mikäli pyynnön bodyssa ei toimiteta tietoa

**HTTP-vastauskoodi** : `400 BAD REQUEST`

**Esimerkkivastaus** :

```json
{
    "timestamp": "2021-10-03T19:04:52.245+00:00",
    "status": 400,
    "error": "Bad Request",
    "trace": "org.springframework.http.converter.HttpMessageNotReadableException: Required request body is missing:...",
    "message": "Required request body is missing: ...",
    "path": "/status/1"
}
```