# Poista tapahtumastatus

Poista tietokannasta status id:n perusteella.

**URL** : `/status/:pk`

**Pyynnön tyyppi** : `DELETE`

**Autentikaatio vaadittu** : ei

**Reunaehdot**

Toimita URIn mukana statuken id. Statuksella ei saa olla liitettyjä tapahtumia.

## Onnistumisvastaus

**Ehto** : Mikäli URIn mukana toimitetaan haluttu id ja poistaminen onnistuu.

**HTTP-vastauskoodi** : `204 NO CONTENT`

## Virhevastaus

**Ehto** : Mikäli statukseen on liitetty tapahtumia.

**HTTP-vastauskoodi** : `403 FORBIDDEN`

**Esimerkkivastaus** :

```json
{
    "message": "Status has associated events, deletion forbidden"
}

```

**Ehto** : Mikäli pyydettyä id:tä ei löydy tietokannasta.

**HTTP-vastauskoodi** : `404 NOT FOUND`

**Esimerkkivastaus** :

```json
{
    "message": "Status not found"
}
```