# Get-dokumentaatio

## Näytä kaikki tapahtumat
Näytä kaikki tietokantaan lisätyt tapahtumat

URL : /events

Metodi : GET

Vaaditaanko autorisointi : Kyllä, ADMIN tai USER

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
## Hae tapahtumia päivämäärien perusteella

URL : `/events?start={ISODate}&end={ISODate}`

Metodi : GET

Vaaditaanko autorisointi : Kyllä, ADMIN tai USER

Pyyntöesimerkki: 

URL : `/events?start=2021-11-3&end=2021-12-12`
  
Parametrit ovat vapaaehtoisia. `start` hakee tapahtumalle merkitys aloitusajan perusteella kaikki ne tapahtumat, 
joiden aloitusaika on parametrin arvon keskiyön jälkeen 
(esim. haettaessa arvolla `start=2021-03-12` tulokset haetaan arvon `2021-03-12-00-00` perusteella)

`end` hakee vastaavasti kaikki tapahtumat, joiden aloitusaika on ennen annetun päivämäärän loppua
(esim `end=2021-03-12` tulokset haetaan arvon `2021-03-12-59-59` perusteella).  

Annettaessa molemmat parametrit, tulokset palautetaan päivämäärien väliltä samoin periaattein kuin yllä. 

Onnistuneen pyynnön vastaukset:

Ehto: päivämäärät ovat ISO Date muodossa, eli `2021-03-21` 

Ks yllä. 

Epäonnistuneen pyynnön vastaus: 

Ehto: päivämäärät eivät ole ISO Date muodossa, esim `2021-3-21`.

Koodi : `400 BAD REQUEST`

Sisältö : Check dates

## Näytä yksi tapahtuma

Näytä yksi tapahtuma sen tunnisteen perusteella.

URL : /events/{id}

Metodi : GET

Vaatiiko autorisoinnin : Kyllä, ADMIN tai USER

Onnistuneen pyynnön vastaus:

Ehto : Annetulla tunnisteella ei ole tapahtumaa.

Koodi : 404 NOT FOUND

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

## Näytä yhden tapahtuman liput

Näytä yhden tapahtuman liput sen tunnisteen perusteella.

URL : /events/{id}/tickets

Metodi : GET

Vaatiiko autorisoinnin : Kyllä, ADMIN tai USER

Onnistuneen pyynnön vastaus:

Ehto : Annetulla tunnisteella ei ole tapahtumaa.

Koodi : 404 NOT FOUND

TAI

Ehto : Annetulla tunnisteella on tapahtuma.

Koodi : 200 OK

Sisältö :

```json
[
    {
        "id": 10,
        "price": 20.9,
        "used": false,
        "ticketType": {
            "id": 5,
            "event": {
                "id": 2,
                "name": "Ruisrock",
                "address": "Savonlinnankatu 50",
                "maxCapacity": 600,
                "startTime": "2021-12-03T09:00:00",
                "endTime": "2021-12-03T16:00:00",
                "endOfPresale": "2021-11-27T16:00:00",
                "status": null,
                "description": "Ruissalossa rokataan",
                "new": false
            },
            "type": "aikuinen",
            "price": 20.9,
            "new": false
        },
        "invoice": {
            "invoiceId": 9,
            "timestamp": "2021-10-06T19:19:47.204019",
            "tguser": {
                "id": 8,
                "userName": "MaiMe",
                "new": false
            }
        },
        "new": false
    }
]
```

TAI

Ehto : Annetulla tunnisteella on tapahtuma, mutta ei lippuja.

Koodi : 200 OK

Sisältö : []