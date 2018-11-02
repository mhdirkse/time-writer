#!/bin/bash

curl -i -c cookies -X POST -H "Content-Type: application/json" "localhost:8080/login" -d '{"username":"username", "password":"password"}'
