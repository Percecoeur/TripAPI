# TripAPI

This is a set of API aimed at notifying a user based on his trip details.

## Prerequisites

* Jdk11
* A local mongoDB instance running on mongodb://localhost:27017 (this can be configured in application.yaml)
* Postman as this project contains a postman collection for easy setup

## Usage

Compile with </p>
`
mvn package
`

Run with </p>
`
java -jar target\trip-1.0-SNAPSHOT.jar
`

At first launch, a default initialization is provided that will create data in mongoDB :
* 2 roles (ROLE_USER, ROLE_ADMIN)
* a new user: name=user,  password=password, role=ROLE_USER
* a new user: name=admin, password=password, role=ROLE_ADMIN
```TEXT
 : --- First Initialization ---
 name=user, password=password, role=ROLE_USER
 name=admin, password=password, role=ROLE_ADMIN
 : ----------------------------
```
(This is very secured... )

## APIs

After startup, you can review the API swagger documentation by accessing </p>
http://localhost:8080/swagger-ui.html

NOTES : All APIs can be used with different authentication mechanism 
* Basic auth : This is available if you pull the main branch of the repository
* Access Token (JWT) : This authentication mechanism is available in the feature/jwt branch of the repository

### Administration APIs

#### List Users 

**SECURED by authentication and role = ROLE_ADMIN** </p>
Allows to list all users declared in the system (from DB) </p>
http://localhost:8080/admin/list/users

Succes example
```JSON
[
    {
        "username": "user",
        "email": null,
        "roles": [
            {
                "name": "ROLE_USER"
            }
        ]
    },
    {
        "username": "admin",
        "email": null,
        "roles": [
            {
                "name": "ROLE_ADMIN"
            }
        ]
    },
    {
        "username": "John",
        "email": "john@deuf.com",
        "roles": [
            {
                "name": "ROLE_USER"
            }
        ]
    }
]
```
Fail example
```JSON
{
    "timestamp": "2022-09-08T08:11:01.855+00:00",
    "status": 401,
    "error": "Unauthorized",
    "message": "Unauthorized",
    "path": "/admin/list/users"
}
```

#### List roles

**SECURED by authentication and role = ROLE_ADMIN** </p>
Allows to list all roles declared in the system (from DB) </p>
http://localhost:8080/admin/list/roles

Success example
```JSON
[
    {
      "name": "ROLE_ADMIN"
    },
    {
      "name": "ROLE_USER"
    }
]
```
Fail example
```JSON
{
    "timestamp": "2022-09-08T08:15:14.625+00:00",
    "status": 401,
    "error": "Unauthorized",
    "message": "Unauthorized",
    "path": "/admin/list/roles"
}
``` 

#### Register a user

**PUBLIC** </p>
Allows to create a user with a role </p>
http://localhost:8080/admin/register

Input example 
```JSON
{
        "username": "bob",
        "password": "password",
        "email": "mon@email",
        "roles": [
            {
                "name": "ROLE_ADMIN"
            }
        ]
    }
```
Success response example
```TEXT
User has been created successfully.
```
Fail response example
```TEXT
User already exists : bob
```
```TEXT
Following role is not found ROLE_GOD
```

In case of an input failure you will receive a validation failed message </p>
This is triggered when an input is missing a field (username/password/email)
```JSON
{
    "timestamp": "2022-09-08T08:22:30.158+00:00",
    "status": 400,
    "error": "Bad Request",
    "message": "Validation failed for object='userDTO'. Error count: 1"
    
}
```
### Notification APIs

#### Trip notification
**SECURED by authentication and role = ROLE_USER/ROLE_ADMIN** </p>

Allows to get a notification for an input trip for the authenticated user.

The API allows to use 2 different technical implementations activated by a query parameter </p>
Functionality is unchanged for both technical implementations.

http://localhost:8080/notif/trip </p>
This API will add notifications for a trip using Spring Task Scheduler

http://localhost:8080/notif/trip?quartz=on </p>
This API will add notifications for a trip using Quartz Scheduler and mongoDB persistence

Example input (can accept multiple bounds)
```JSON
{
    "bounds": [
         {
            "origin": "Nice",
            "destination": "Paris CDG",
            "departureTime": "2022-09-06 22:08",
            "arrivalTime": "2022-09-06 22:55"
        },
        {
            "origin": "Paris CDG",
            "destination": "Nice",
            "departureTime": "2022-09-06 23:10",
            "arrivalTime": "2022-09-07 23:55"
        }     
    ]

}


   
```
Response
```TEXT
Your trip was correctly registered for notification
```

The previous JSON will create 3 notifications:
* At creation time 
* 3 Hours before the departure where departure is the departureTime of the first bound
* At arrival time where arrivalTime is the arrivalTime of the last bound.

So taking this example we will have following results in the console </p>
For Spring scheduler :
* com.amadeus.trip.job.SpringTask : We have received your next trip to Nice. We'll accompany you along the way!
* com.amadeus.trip.job.SpringTask : Your flight leaves today at 2022-09-08 06:10. Would you like to order a taxi ?
* com.amadeus.trip.job.SpringTask : You have landed at 2022-09-08 11:10 in Nice ! Welcome home ! Would you like to order a taxi ?

For Quartz :
* com.amadeus.trip.job.QrtzJob : We have received your next trip to Nice. We'll accompany you along the way!
* com.amadeus.trip.job.QrtzJob : Your flight leaves today at 2022-09-08 06:10. Would you like to order a taxi ?
* com.amadeus.trip.job.QrtzJob : You have landed at 2022-09-08 11:10 in Nice ! Welcome home ! Would you like to order a taxi ?


## Technical choices

### Authentication
#### Basic authentication

#### JWT

### Schedulers

2 Implementations are available for the schedulers. The Pros and cons are available below.
#### Spring Task Scheduler
* Pros - Easy setup : 
* Cons - Lack of Scalability : In a cloud environment where multiple instances of the same application exists, the triggers could be executed multiple times
* Cons - Lack of Persistence : No persistence OOTB... we would need a custom implementation
* Cons - Lack of automatic recovery : In case of the application crashes, since all triggers and jobs are in memory, they will need to be recreated


#### Quartz scheduler
Quartz would be obviously a better choice for production environments as it allows : </p>
* Pros - Scalability : In a cloud environment where multiple instances of the same application exists, Quartz is able to adapt so the triggers are only performed once
* Pros - Persistence : We will be able to persist our jobs and triggers in DB thus allowing reporting for instance
* Pros - Automatic recovery : In case of the application crashes, since all triggers and jobs are stored in DB, they will be automatically recovered and triggered.
* Cons - A bit more complex to setup

