# Get-dokumentaatio

## Näytä kaikki tapahtumat
Näytä kaikki tietokantaan lisätyt tapahtumat

URL : /events

Metodi : GET

Vaaditaanko autorisointi : kyllä

Onnistuneen pyynnön vastaukset:
Ehto : Tietokannassa ei ole tapahtumia

Koodi : 200 OK

Sisältö : []

TAI
Ehto : Tietokannassa on tapahtumia

Koodi : 200 OK

Sisältö : Tässä esimerkissä käyttäjä näkee kolme tapahtumaa:

```json
[
    {
        "id":2,
        "name":"Ruisrock",
        "address":"Savonlinnankatu 50",
        "maxCapacity":600,
        "startTime":"2021-12-03T09:00:00",
        "endTime":"2021-12-03T16:00:00",
        "endOfPresale":"2021-11-27T16:00:00",
        "status":null,
        "description":"rock'nroll",
        "ticketPrices":null,
        "new":false
    },
    {
        "id":3,"name":"Ruisrock2",
        "address":"Savonlinnankatu 50",
        "maxCapacity":700,
        "startTime":"2021-12-03T09:00:00",
        "endTime":"2021-12-03T16:00:00",
        "endOfPresale":"2021-11-27T16:00:00",
        "status":null,
        "description":"rock'nrollevent",
        "ticketPrices":null,
        "new":false
    },
    {
        "id":4,"name":"Ruisrock3",
        "address":"Savonlinnankatu 50",
        "maxCapacity":700,
        "startTime":"2021-12-03T09:00:00",
        "endTime":"2021-12-03T16:00:00",
        "endOfPresale":"2021-11-27T16:00:00",
        "status":
            {
                "id":1,"statusName":"upcoming",
                "new":false
            },
        "description":"rock'nrollevent",
        "ticketPrices":null,
        "new":false
    }
]
```

## Näytä yksi tapahtuma

Näytä yksi tapahtuma sen tunnisteen perusteella.

URL : /events/{id}

Metodi : GET

Vaatiiko autorisoinnin : Kyllä

Onnistuneen pyynnön vastaus
Ehto : Annetulla tunnisteella ei ole tapahtumaa.

Koodi : 404 NOT FOUND

Sisältö : null

TAI

Ehto : Annetulla tunnisteella on tapahtuma.

Koodi : 200 OK

Sisältö :

```json
{
    "id": 4,
    "name": "Ruisrock3",
    "address": "Savonlinnankatu 50",
    "maxCapacity": 700,
    "startTime": "2021-12-03T09:00:00",
    "endTime": "2021-12-03T16:00:00",
    "endOfPresale": "2021-11-27T16:00:00",
    "status": {
        "id": 1,
        "statusName": "upcoming",
        "new": false
    },
    "description": "rock'nroll",
    "ticketPrices": null,
    "new": false
}
```