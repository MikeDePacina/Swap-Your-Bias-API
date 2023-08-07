# Swap-Your-Bias-API
API for photocard trading site

Endpoints:
Photocards Base URL: /api/photocards

GET(no authentication required): Returns list of photocards posted
Response Status: 200 OK
```json
{
    "photocards": [
        {
            "id": 1,
            "artist": "tunay",
            "group": null,
            "userID": 4,
            "imgPath": "pleasures/allmine",
            "datePosted": "2023-07-05T14:29:42.870693",
            "dateUpdated": null
        },
        {
            "id": 2,
            "artist": "Claire-buche",
            "group": null,
            "userID": 7,
            "imgPath": "don;t/lnow/me",
            "datePosted": "2023-08-06T11:58:44.859785",
            "dateUpdated": "2023-08-06T23:07:55.360475"
        }
    ],
    "pageNo": 0,
    "pageSize": 10,
    "totalElements": 2,
    "totalPages": 1,
    "last": true
}
```

GET/{photoCardID} (no authentication required): Returns photocard with specified id
Response Status: 200 OK
```json
{
    "id": 2,
    "artist": "Claire-buche",
    "group": null,
    "userID": 7,
    "imgPath": "don;t/lnow/me",
    "datePosted": "2023-08-06T11:58:44.859785",
    "dateUpdated": "2023-08-06T23:07:55.360475"
}
```
POST(token required and photocardDTO(artist, imgPath, userID are required) in request body): Returns photocard posted
Response Status: 201 Created
```json
  {
    "id": 3,
    "artist": "Grimes",
    "group": null,
    "userID": 7,
    "imgPath": "Kill/V/Maim",
    "datePosted": "2023-08-06T23:29:20.5239998",
    "dateUpdated": null
}
```
PUT/{userID}/{pcID}(token, and updated photocardDTO required): Returns updated photocard
Response Status: 200 Ok
```json
{
    "id": 3,
    "artist": "Grimes",
    "group": null,
    "userID": 7,
    "imgPath": "I/want/to/be/software",
    "datePosted": "2023-08-06T23:29:20.524",
    "dateUpdated": "2023-08-06T23:31:24.0205062"
}
```
DELETE/pcID(token required): Returns deleted upon successful deletion
Response Status: 200 Ok
```json
Deleted
```




