# Poista tapahtuma

Poistaa tapahtuman tunnisteen perusteella.

**URL** : `/events/{id}`

**Pyynnön tyyppi** : `DELETE`

**Autentikaatio vaadittu** : Kyllä

**Reunaehdot**

Tapahtuman id tulisi olla tunnettu.  

**Esimerkkipyyntö** 

Poista tapahtuma, jonka id on 2.

Tyhjä DELETE-tyyppin pyyntö kohteeseen "/events/2"

## Onnistumisvastaus

**Ehto** : Tapahtuman id on olemassa ja poisto onnistui.

**HTTP-vastauskoodi** : `200 OK`

**Esimerkkivastaus** : json: deleted: true.

```json
{
    deleted: true
}
```

## Virhevastaus

### 1

**Ehto** : Tapahtuman tunnistetta ei ole olemassa.

**HTTP-vastauskoodi** : `404 NOT FOUND`

**Esimerkkivastaus** : json {deleted: false}

```json
{
    deleted: false
}
```

