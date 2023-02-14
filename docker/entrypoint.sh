#!/bin/sh

ENVIRONMENT="$1"
KEYSTORE_PASSWORD="$2"

if [ "$#" -ne 2 ]; then
  echo "Illegal number of parameters (needed 2)"
  exit 1
fi

if [ "$ENVIRONMENT" = "prod" ]
then
  certbot certonly --standalone -d konnector.io -d www.konnector.io --non-interactive --email root.konnector@gmail.com --agree-tos
  cd /etc/letsencrypt/live/konnector.io
  openssl pkcs12 -export -in fullchain.pem -inkey privkey.pem -out keystore.p12 -name konnectorCerts -CAfile chain.pem -caname otherCerts -password "$2"
fi

java -jar /app.jar --network="host" --spring.profiles.active="$ENVIRONMENT"
