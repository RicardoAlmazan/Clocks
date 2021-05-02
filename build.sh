#!/bin/bash
if [ "$1" = "client" ] ; then
  echo "Generando la pieza cliente..."
  cd client
  mvn clean install
  echo "Se ha instalado el cliente!"
elif [ "$1" = "server" ] ; then
  echo "Generando la pieza servidor..."
  cd server
  mvn clean install
  echo "Se ha instalado el servidor!"
else
  echo "Generando ambas piezas..."
  cd client
  mvn clean install
  echo "Se ha instalado el cliente!"

  cd ../server
  mvn clean install
  echo "Se ha instalado el servidor!"
fi

echo "DONE :)"