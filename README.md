# Star Wars - Planet CRUD

Star Wars Planet CRUD &amp; API Integration

## Build Status
[![Build Status](https://github.com/felipe-publio/sw-planet-service/actions/workflows/gradle.yml/badge.svg?branch=master)](https://github.com/felipe-publio/sw-planet-service/actions/workflows/gradle.yml)

## How to run
run `docker-compose up`

## Documentation and examples
- Swagger URL
  [http://localhost:9081/swagger-ui.html#/](http://localhost:9081/swagger-ui.html#/ "http://localhost:9081/swagger-ui.html#/")

- Postman collection

  ![image](https://user-images.githubusercontent.com/14155185/119331527-d49d6f80-bc5d-11eb-93e3-00e2801fe19d.png)

## Features
- Add Planet (Name, Terrain, Climate)
  - Planets cannot have the same name
- Find Planet with film appearances information
  - Find by name
  - Find by id
  - Find all
- Delete a planet

## Stack
- Java 16
- Spring Boot
- Spring Data Mongo
- Spring Cacheable
- Mongo Database
- Feign Client
- Docker

## Integrations
- Star Wars API
  - https://swapi.dev/about
