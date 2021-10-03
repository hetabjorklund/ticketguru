# Poista tapahtumam status

Poista tietokannasta status id:n perusteella.

**URL** : `/status/:pk`

**Pyynnön tyyppi** : `DELETE`

**Autentikaatio vaadittu** : ei

**Reunaehdot**

Toimita URIn mukana statuken id.

## Onnistumisvastaus

**Ehto** : Mikäli URIn mukana toimitetaan haluttu id ja poistaminen onnistuu.

**HTTP-vastauskoodi** : `200 OK`

**Esimerkkivastaus**

```json
{
    "deleted": true
}
```

## Virhevastaus

**Ehto** : Mikäli pyydettyä id:tä ei löydy tietokannasta.

**HTTP-vastauskoodi** : `404 NOT FOUND`

**Esimerkkivastaus** :

```json
{
    "deleted": false
}
```