#!/bin/bash

curl -i -c cookies -X POST -H "Content-Type: application/json" "https://credible-nation-130012.appspot.com/login" -d '{"username":"admin", "password":"admin"}'
