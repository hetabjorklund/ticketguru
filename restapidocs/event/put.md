# Muokkaa tapahtumaa (event)

Muokkaa olemassa olevaa tapahtumaa (event) annetun id:n perusteella tai id:n puuttuessa luo uuden tapahtuman.

**URL** : `/api/events/:pk`

**Method** : `PUT`

**Auth required** : YES

**Permissions required** : None

**Data constraints**

Toimita kaikki pyydetyt kentät.

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

**Data example** 

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

**Condition** : Mikäli kaikki kentät löytyvät pyynnöstä ja muokkaus onnistuu. HUOM! Mikäli kaikkia kenttiä ei anneta, puuttuvat kentät saavat arvon null.

**Code** : `200 OK`

**Content example**

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
### Or

**Condition** : Mikäli annettua id:tä ei löydy.

**Code** : `201 CREATED`

**Content example**

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

**Condition** : Mikäli id:tä ei ole määritelty kutsussa.

**Code** : `404 NOT FOUND`

**Content** :

```json
{
    "timestamp": "2021-09-24T12:20:41.990+00:00",
    "status": 404,
    "error": "Not Found",
    "message": "No message available",
    "path": "/api/events/"
}
```
