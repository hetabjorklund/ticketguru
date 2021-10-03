# Muokkaa tapahtuman statuksen nimeä

Muokkaa tietokannasta löytyvän statuksen nimeä.

**URL** : `/status/:pk`

**Pyynnön tyyppi** : `PATCH`

**Autentikaatio vaadittu** : ei

**Reunaehdot**

Toimita statukselle muokattu nimi.

```json
{
    "statusName": "string"
}
```

**Esimerkkipyyntö** 

```json
{
    "statusName": "EDITcanceled"
}
```

## Onnistumisvastaus

**Ehto** : Mikäli pyynnön mukana toimitetaan statuksen nimi 

**HTTP-vastauskoodi** : `200 OK`

**Esimerkkivastaus**

```json
{
    "id": 1,
    "statusName": "EDITEDupcoming",
    "new": false
}
```

## Virhevastaus

**Ehto** : Mikäli annettua statusta lei löydy tietokannasta

**HTTP-vastauskoodi** : `404 NOT FOUND`

**Esimerkkivastaus** :

```json
{
    "id": 12,
    "statusName": "canceled",
    "new": false
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