# Muokkaa tapahtumaa (event)

Muokkaa olemassa olevaa tapahtumaa (event) annetun id:n perusteella tai id:n puuttuessa luo uuden tapahtuman.

**URL** : `/events/:pk`

**Pyynnön tyyppi** : `PUT`

**Autentikaatio vaadittu** : Kyllä, ADMIN

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

**HTTP-vastauskoodi** : `405 METHOD NOT ALLOWED`

**Esimerkkivastaus** :

```json
{
    "timestamp": "2021-09-28T08:25:48.200+00:00",
    "status": 405,
    "error": "Method Not Allowed",
    "trace": "org.springframework.web.HttpRequestMethodNotSupportedException: Request method 'PUT' not supported...",
    "message": "Request method 'PUT' not supported",
    "path": "/events/"
}
```

### tai

**Ehto** : Pyynnössä ei lähetetty tapahtuman nimeä.

**HTTP-vastauskoodi** : `400 BAD REQUEST`

**Esimerkkivastaus**
```json
{
    "timestamp": "2021-10-06T15:55:25.913+00:00",
    "status": 400,
    "error": "Bad Request",
    "trace": [stacktrace],
    "message": "Validation failed for object='event'. Error count: 1",
    "errors": [
        {
            "codes": [
                "NotEmpty.event.name",
                "NotEmpty.name",
                "NotEmpty.java.lang.String",
                "NotEmpty"
            ],
            "arguments": [
                {
                    "codes": [
                        "event.name",
                        "name"
                    ],
                    "arguments": null,
                    "defaultMessage": "name",
                    "code": "name"
                }
            ],
            "defaultMessage": "must not be empty",
            "objectName": "event",
            "field": "name",
            "rejectedValue": null,
            "bindingFailure": false,
            "code": "NotEmpty"
        }
    ],
    "path": "/events/2"
}
```