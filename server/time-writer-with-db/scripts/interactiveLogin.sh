#!/bin/bash

curl -i -c cookies -X POST -H "Content-Type: application/json" "https://${googleProject}.appspot.com/login" -d '{"username":"admin", "password":"admin"}'
