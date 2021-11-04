# Lipun poistaminen tietokannasta

Poistaa lipun sen yksilöivän tunnisteen perusteella tietokannasta

URL : /tickets/{id}

Metodi : DELETE

Vaatiiko autorisoinnin : Kyllä

Data : {}

## Onnistuneen pyynnön vastaus

Ehto: syötetyllä tunnisteella on lippu tietokannassa

Koodi : 204 NO CONTENT

Sisältö : Ei palauta responsebodya takaisin (vastauksen tyyppi no content)

## Virheellisen pyynnön vastaus

Ehto: Syötetyllä tunnisteella ei ole lippua tietokannassa

Koodi: 404 NOT FOUND

Sisältö: 
```json
{
    "message": "Ticket with the given Id was not found",
    "status": "404"
}
```