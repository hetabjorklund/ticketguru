# Listaa kaikki tapahtumien statukset
Listaa kaikki tietokantaan lisätyt tapahtumien statukset

**URL** : `/status`

**Pyynnön tyyppi** : `GET`

**Autentikaatio vaadittu** : ei

## Onnistumisvastaus

**Ehto** : Mikäli tietokannasta löytyy status tapahtumille

**HTTP-vastauskoodi** : `200 OK`

**Esimerkkivastaus**

```json
[
    {
        "id": 1,
        "statusName": "upcoming",
        "new": false
    }
]
```
## Virhevastaus

**Ehto** : Mikäli tietokannasta ei löydy statusta tapahtumille

**HTTP-vastauskoodi** : `200 OK`

**Esimerkkivastaus**

```json
[]
```