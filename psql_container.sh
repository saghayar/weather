#!/bin/bash

docker run --rm -m 256m --name weather_psql \
 -p 5435:5432 \
 -e POSTGRES_DB=weather \
 -e POSTGRES_USER=userForLocal \
 -e POSTGRES_PASSWORD=P@ssForloc@l \
 -v $PWD/postgresql_data:/var/lib/postgresql/data \
 -d postgres:10
