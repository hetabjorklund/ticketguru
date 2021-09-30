# TicketTypes-GET

## Näytä kaikki lipputyypit
Näytä kaikki tietokantaan lisätyt lipputyypit

URL : /types

Metodi : GET

Vaaditaanko autorisointi : kyllä

Onnistuneen pyynnön vastaukset:

Ehto : Tietokannassa ei ole lipputyyppejä

Koodi : 200 OK

Sisältö : []

TAI

Ehto : Tietokannassa on lipputyyppejä

Koodi : 200 OK

Sisältö : Tässä esimerkissä käyttäjä näkee kolme lipputyyppiä tapahtumatietoineen:

```json
[
  {
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
      "description": "fun things",
      "new": false
    },
    "type": "aikuinen",
    "price": 20.9,
    "new": false
  },
  {
    "id": 6,
    "event": {
      "id": 2,
      "name": "Ruisrock",
      "address": "Savonlinnankatu 50",
      "maxCapacity": 600,
      "startTime": "2021-12-03T09:00:00",
      "endTime": "2021-12-03T16:00:00",
      "endOfPresale": "2021-11-27T16:00:00",
      "status": null,
      "description": "fun things2",
      "new": false
    },
    "type": "eläkeläinen",
    "price": 30,
    "new": false
  },
  {
    "id": 7,
    "event": {
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
      "description": "fun thing3",
      "new": false
    },
    "type": "aikuinen",
    "price": 24.75,
    "new": false
  }
]

```

## Näytä tapahtuman lipputyypit

Näytä lipputyypit tapahtumatunnisteen perusteella.

URL : /events/{id}/types

Metodi : GET

Vaatiiko autorisoinnin : kyllä

Tietorajoitteet: Pyyntö ohjattava validiin tapahtumatunnukseen.

##Onnistuneen pyynnön vastaus

Ehto : Annetulla tunnisteella on tapahtuma mutta ei lipputyyppejä.

Koodi : 200 OK

Sisältö : []

TAI

Ehto : Annetulla tunnisteella on tapahtuma ja lipputyyppejä.

Koodi : 200 OK

Sisältö :

```json
[
  {
    "id": 7,
    "event": {
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
      "description": "murderdeathkillevent",
      "new": false
    },
    "type": "aikuinen",
    "price": 24.75,
    "new": false
  }
]
```

##Virheellisen pyynnön vastaus

Ehto : Annetulla tunnisteella ei ole tapahtumaa.

Koodi : 404 NOT FOUND

Sisältö : []


# TicketTypes-POST

## Lisää uusi lipputyyppi

Lisää tietokantaan uuden lipputyypin

URL : /types

Metodi : POST

Vaatiiko autorisoinnin : Kyllä

Tietorajoitteet: Pyynnössä on oltava validi tapahtumatunnus.

Esimerkkipyyntö:

```json
{
	"event": 3,
	"type": "lapsi 6-14",
	"price": 23.6
}
```

###Onnistuneen pyynnön vastaus

Ehto : Annetulla tunnisteella on tapahtuma.

Koodi : 201 CREATED

Esimerkkivastaus:

```json
{
	"event": 3,
	"type": "lapsi 6-14",
	"price": 23.6
}
```
###Virheellisen pyynnön vastaus

Ehto : Annetulla tunnisteella ei ole tapahtumaa.

Koodi : 400 BAD REQUEST

Sisältö : 

```json
{
	"event": 8,
	"type": "lapsi 6-14",
	"price": 23.6
}
```
#TicketTypes-PUT

##Olemassaolevan lipputyypin päivitys
 
 Päivittää tyypin tunnisteen perusteella
 
 URL: /types/{id}
 
 Metodi: PUT
 
 Vaatiiko autorisoinnin : Kyllä

Tietorajoitteet: Pyynnössä on oltava validi tapahtumatunnus ja lipputyypin tunnus.

Esimerkkipyyntö:

```json
{
	"event": 3,
	"type": "lapsi 6-14",
	"price": 23.6
}
```
##Onnistuneen pyynnön vastaus
 
 Ehto: Tietojen päivitys onnistui
 
 Koodi: 200 OK

Vastauksen sisältö:

```json
{
	"event": 3,
	"type": "lapsi 6-14",
	"price": 23.6
}
```

##Virheellisen pyynnön vastaus

Ehto: Lipputyypin tunnistetta ei löydy

Koodi: 404 NOT FOUND

Vastauksen sisältö:

```json
{
	"event": 3,
	"type": "lapsi 6-14",
	"price": 23.6
}
```

TAI

Ehto: Tapahtumatunnistetta ei löydy

Koodi: 400 BAD REQUEST

Vastauksen sisältö:

```json
{
	"event": 3,
	"type": "lapsi 6-14",
	"price": 23.6
}
```

#TicketTypes-DELETE
##Lipputyypin poistaminen

Poistaa lipputyypin tunnisteen perusteella

URL: /types/{id}

Metodi: DELETE

Vaatiiko autorisoinnin : Kyllä

Tietorajoitteet: Pyynnössä on oltava validi lipputyypin tunnus.

##Onnistuneen pyynön vastaus

Ehto: Tyyppi-id on olemassa

Koodi 200 OK

Vastaus:

```json
{
	"deleted": true
}

```

##Virheellisen pyynnön vastaus

Ehto: Tunnistetta ei ole olemassa

Koodi: 404 NOT FOUND

Vastaus:

```json
{
	"deleted": false
}

```
