# Lipun merkitseminen käytetyksi

Merkitsee käyttämättömän lipun käytetyksi lipun koodin perusteella

URL : /tickets/{code}/used

Metodi : PATCH

Vaatiiko autorisoinnin : Kyllä, ADMIN tai USER

## Onnistuneen pyynnön vastaus

Ehto: Tietojen päivittäminen onnistuu

Koodi: 200 OK

Vastauksen sisältö:
```json
{
    "message": "Ticket is valid"
}
```

## Virheellisen pyynnön vastaus

Ehto: Annettu lippu on jo merkitty käytetyksi

Koodi: 400 BAD REQUEST

Vastauksen sisältö:
```json
{
    "message": "Ticket has already been used. Ticket is not valid"
}
```

TAI

Ehto: Annetulla koodilla ei ole lippua tietokannassa

Koodi: 404 NOT FOUND

Vastauksen sisältö:
```json
{
    "message": "Ticket not found"
}
```