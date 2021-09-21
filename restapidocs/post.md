# Luo tapahtuma

Luo tapahtuman mikäli tapahtumaa ei ole jo olemassa.

**URL** : `/api/events/`

**Method** : `POST`

**Auth required** : YES

**Permissions required** : None

**Data constraints**

Provide name of Account to be created.

```json
{
    "name": "[unicode 64 chars max]"
}
```

**Data example** All fields must be sent.

```json
{
    "name": "Build something project dot com"
}
```

## Onnistumisvastaus

**Condition** : If everything is OK and an Account didn't exist for this User.

**Code** : `201 CREATED`

**Content example**

```json
{
    "id": 123,
    "name": "Build something project dot com",
    "url": "http://testserver/api/accounts/123/"
}
```

## Virhevastaus

**Condition** : Jos samanniminen tapahtuma on jo olemassa.

**Code** : `303 SEE OTHER`

**Headers** : `Location: http://testserver/api/events/123/`

**Content** : `{}`

### Or

**Condition** : Kenttiä puuttuu tai niiden sisältämä tieto on väärän tyyppistä (esim. String kun pitäisi olla Boolean).

**Code** : `400 BAD REQUEST`

**Content example**

```json
{
    "name": [
        "This field is required."
    ]
}
```
