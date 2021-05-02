#!/bin/bash
if [ "$1" = "client" ] ; then
  echo "Inicializando cliente..."
  cd client
  sh run.sh
elif [ "$1" = "server" ] ; then
  echo "Inicializando servidor..."
  cd server
  sh run.sh
  echo "Se ha instalado el servidor!"
else
  echo "Inicializando ambas piezas..."
  cd server
  sh run.sh
  echo "Se ha inicialiado el servidor!"

  cd ../client
  sh run.sh
  echo "Se ha inicialiado el cliente!"
fi

echo "It's alive!"