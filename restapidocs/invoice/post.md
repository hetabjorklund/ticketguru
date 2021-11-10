# Luo lasku

Luo uuden laskun.

**URL** : `/invoices`

**Pyynnön tyyppi** : `POST`

**Autentikaatio vaadittu** : Kyllä, ADMIN tai USER

**Reunaehdot**

Käyttäjän tulee olla tunnistettu

**Esimerkkipyyntö** 

Pyyntö ei vaadi lähetettävää bodyä.

## Onnistumisvastaus

**Ehto** : Laskun luominen onnistui.

**HTTP-vastauskoodi** : `201 CREATED`

**Esimerkkivastaus** : Palautetaan luodun laskun tiedot.

```json
{
    "invoiceId": 12,
    "timestamp": "2021-10-06T18:31:16.1974145",
    "tguser": {
        "id": 8,
        "userName": "MaiMe",
        "new": false
    }
}
```

## Virhevastaus

### 1
**Ehto** : Lähettäjä ei ole tunnistautunut

**HTTP-vastauskoodi** : `401 UNAUTHORIZED`

**Vastaus** : tyhjä vastaus
