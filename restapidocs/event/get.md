# Get-dokumentaatio

## Show All Events
Show all events that are added into the database

URL : /events

Method : GET

Auth required : YES

Success Responses
Condition : There are no events in the database

Code : 200 OK

Content : {[]}

OR
Condition : There are events in the database.

Code : 200 OK

Content : In this example, the User can see three Events:

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
        "description":"murderdeathkill",
        "ticketPrices":null,"new":false
    },
    {
        "id":3,"name":"Ruisrock2",
        "address":"Savonlinnankatu 50",
        "maxCapacity":700,
        "startTime":"2021-12-03T09:00:00",
        "endTime":"2021-12-03T16:00:00",
        "endOfPresale":"2021-11-27T16:00:00",
        "status":null,
        "description":"murderdeathkillevent",
        "ticketPrices":null,"new":false
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
        "description":"murderdeathkillevent",
        "ticketPrices":null,"new":false}]
```

## Show one event

Show one event by event's id.

Method : GET

Auth required : YES

Success Responses
Condition : An event with the given id does not exist.

Code : 200 OK

Content : null

OR
Condition : An event with the given id does exist.

Code : 200 OK

Content :

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
    "description": "murderdeathkillevent",
    "ticketPrices": null,
    "new": false
}
```