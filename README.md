# About

CO2 sensor data collector service.

# Author

Kevin Ratnasekera

# Assumptions

Time received on the client requests are unreliable. Hence, server recorded times are stored in the Database.
Once sensor status is turned to 'WARN', if the next measurement received is below the co2 threshold 2000, 
then the sensor status is turned back to 'OK'.

# Tech Stack Used

* Java 8
* Apache Maven 3.9.6
* File backed H2 database - ./co2sensor/db Directory

# Building/Running Instructions

* The spring boot application is dockerized and added to the maven build process. 
  So it s not required to docker build manually. Make sure docker is up and running locally before executing maven build command.
  Notice the docker image name and tag used in the below example.
* ```mvn clean install```
* ```docker run -p 8080:8080 co2-sensor-data-service:0.0.1 ```
* The spring boot service will be running at ```localhost:8080```

# API

## Save sensor measurements

POST /api/v1/sensors/{uuid}/measurements

```
{
  "co2" : 2000,
  "time" : "2019-02-01T18:55:47+00:00"
}
```

Returned Status Codes

```
400 - Bad request
200 - Successful
500 - Internal server error
```

## Get Sensor status

GET /api/v1/sensors/{uuid}

```
{
  "status" : "OK" // Possible status OK,WARN,ALERT
}
```

Returned Status Codes

```
400 - Bad request
404 - Sensor id not found
500 - Internal server error
```

## Get Sensor metrics

GET /api/v1/sensors/{uuid}/metrics

```
{
  "maxLast30Days" : 1200,
  "avgLast30Days" : 900
}
```

Returned Status Codes
```
400 - Bad request
404 - Sensor id not found
500 - Internal server error
```