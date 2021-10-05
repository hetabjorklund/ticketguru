# Lisää tietokantaan tapahtumien status

Luo uusi status tapahtumille

**URL** : `/status`

**Pyynnön tyyppi** : `POST`

**Autentikaatio vaadittu** : ei

**Reunaehdot**

Toimita statukselle nimi.

```json
{
    "statusName": "string"
}
```

**Esimerkkipyyntö** 

```json
{
    "statusName": "canceled"
}
```

## Onnistumisvastaus

**Ehto** : Mikäli pyynnön mukana toimitetaan statuksen nimi 

**HTTP-vastauskoodi** : `200 OK`

**Esimerkkivastaus**

```json
{
    "id": 12,
    "statusName": "canceled",
    "new": false
}
```

## Virhevastaus

**Ehto** : Mikäli annettu status löytyy jo tietokannasta

**HTTP-vastauskoodi** : `409 CONFLICT`

**Esimerkkivastaus** :

```json
{
    "id": 12,
    "statusName": "canceled",
    "new": false
}
```
### tai

Ehto : Pyynnön mukana ei toimitettu bodyä.

**HTTP-vastauskoodi** : `400 BAD REQUEST`

Sisältö :

```json
{
    "timestamp": "2021-10-03T18:53:27.248+00:00",
    "status": 400,
    "error": "Bad Request",
    "trace": "org.springframework.http.converter.HttpMessageNotReadableException:...",
    "message": "Required request body is missing:...",
    "path": "/status/"
}
```