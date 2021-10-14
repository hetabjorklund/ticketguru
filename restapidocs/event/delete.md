# Poista tapahtuma

Poistaa tapahtuman tunnisteen perusteella.

**URL** : `/events/{id}`

**Pyynnön tyyppi** : `DELETE`

**Autentikaatio vaadittu** : Kyllä, ADMIN

**Reunaehdot**

Pyynnössä täytyy lähettää haetun tapahtuman id. Tapahtumaan ei saa liittyä myytyjä lippuja. 

**Esimerkkipyyntö** 

Poista tapahtuma, jonka id on 2: lähetä DELETE-tyypin pyyntö polkuun "/events/2". Pyynnön vartalo on tyhjä.

## Onnistumisvastaus

**Ehto** : Tapahtuman id on olemassa, tapahtumaan ei liity myytyjä lippuja ja poisto onnistui.

**HTTP-vastauskoodi** : `204 NO CONTENT`

## Virhevastaus

### 1

**Ehto** : Haetun tapahtuman id:tä ei ole olemassa.

**HTTP-vastauskoodi** : `404 NOT FOUND`

### 2

**Ehto** : Tapahtumaan liittyy myytyjä lippuja.

**Vastaus** : Event has associated tickets, deletion forbidden

**HTTP-vastauskoodi** : `403 FORBIDDEN`