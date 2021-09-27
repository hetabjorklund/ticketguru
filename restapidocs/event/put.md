# Muokkaa tapahtumaa (event)

Muokkaa olemassa olevaa tapahtumaa (event) annetun id:n perusteella tai id:n puuttuessa luo uuden tapahtuman.

**URL** : `/api/events/:pk`

**Pyynnön tyyppi** : `PUT`

**Autentikaatio vaadittu** : Ei

**Reunaehdot**

Toimita kaikki pyydetyt kentät. HUOM! Mikäli kaikkia kenttiä ei anneta, puuttuvat kentät saavat arvon null.

```json
{
    "name": "string",
    "address": "string",
    "maxCapacity": integer,
    "startTime": "string",
    "endTime": "string",
    "endOfPresale": "string",
    "description": "string"
}
```

**Esimerkkipyyntö** 

```json
{
    "name": "Ruisrock",
    "address": "Savonlinnankatu 50",
    "maxCapacity": 600,
    "startTime": "2021-12-03T09:00:00",
    "endTime": "2021-12-03T16:00:00",
    "endOfPresale": "2021-11-27T16:00:00",
    "description": "Artistikattauksessa mm. X ja Y"
}
```

## Onnistumisvastaus

**Ehto** : Mikäli muokkaus onnistuu. HUOM! Mikäli kaikkia kenttiä ei anneta, puuttuvat kentät saavat arvon null.

**HTTP-vastauskoodi** : `200 OK`

**Esimerkkivastaus**

```json
{
    "name": "muokattuRuisrock",
    "address": "Savonlinnankatu 50",
    "maxCapacity": 600,
    "startTime": "2021-12-03T09:00:00",
    "endTime": "2021-12-03T16:00:00",
    "endOfPresale": "2021-11-27T16:00:00",
    "description": "Artistikattauksessa mm. X ja Y"
}
```
### tai

**Ehto** : Mikäli annettua id:tä ei löydy tietokannasta ennestään.

**Http-vastauskoodi** : `201 CREATED`

**Esimerkkivastaus**

```json
{
    "name": "muokattuRuisrock",
    "address": "Savonlinnankatu 50",
    "maxCapacity": 600,
    "startTime": "2021-12-03T09:00:00",
    "endTime": "2021-12-03T16:00:00",
    "endOfPresale": "2021-11-27T16:00:00",
    "description": "Artistikattauksessa mm. X ja Y"
}
```

## Virhevastaus

**Ehto** : Mikäli id:tä ei ole määritelty kutsussa.

**HTTP-vastauskoodi** : `404 NOT FOUND`

**Esimerkkivastaus** :

```json
{
    "timestamp": "2021-09-24T12:20:41.990+00:00",
    "status": 404,
    "error": "Not Found",
    "message": "No message available",
    "path": "/api/events/"
}
```
