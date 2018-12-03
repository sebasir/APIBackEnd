# APIBackEnd
###Creación de los contenedores
Descargar la imagen oficial de mongoDB:

    docker pull mongo:latest

Descargar la imagen oficial de Jboss Wildfly, solo que en esta oportunidad, será la versión 14.0.0.Final:

    docker pull jboss/wildfly:14.0.0.Final

Verificar las descargas:

    docker images

Crear un volumen de datos:

    docker volume create apiDBVolume

Crear una carpeta para la ejecución de scripts y darle permisos:

    mkdir /var/scripts
    chmod -R 777 /var/scripts/

Crear una carpeta para la colocación de war o apps y darle permisos:

    mkdir /var/wildfly/standalone
    chmod -R 777 /var/wildfly/standalone

Crear una red para la comunicación entre contenedores:

    docker network create apiServers

Crear un contenedor para la base de datos:

    docker run --name apiMongo -p 1224:27017 -d -v /var/scripts/:/var/scripts/ -v apiDBVolume:/data/db --network apiServers mongo:latest
* Se llama apiMongo: `--name`
* Corre en background: `-d`
* Realiza enlace del puerto 27017 del contenedor al puerto 27017 del host: `-p 1224:27017`
* Enlaza la carpeta /var/scripts/ del contenedor a la carpeta /var/scripts/ del host: `-v /var/scripts/:/var/scripts/`
* Utiliza el volumen apiDBVolume para guardar los datos de la carpeta `/data/db` del contenedor, que es donde la base de datos guarda todo: `-v apiDBVolume:/data/db`
* Replica la imagen de mongo descargada previamente: `mongo:latest`
* Conecta los servidores dentro de la red apiServers: `--network apiServers`


###Crear un contenedor para el servidor de aplicaciones Wildfly:

    docker run --name apiWildfly -d -p 80:8080 -v /var/wildfly/standalone:/opt/jboss/wildfly/standalone/deployments --network apiServers jboss/wildfly:14.0.1.Final
* Se llama apiWildfly: `--name`
* Corre en background: `-d`
* Realiza enlace del puerto 8080 del contenedor al puerto 80 del host: `-p 80:8080`
* Enlaza la carpeta `/opt/jboss/wildfly/standalone/deployments del contenedor a la carpeta /var/wildfly/standalone/ del host: -v /var/wildfly/standalone:/opt/jboss/wildfly/standalone/deployments`
* Replica la imagen de mongo descargada previamente: `jboss/wildfly:14.0.0.Final`
* Conecta los servidores dentro de la red apiServers: `--network apiServers`

###Verificar el estado de las instancias:

    docker ps -a

Detener o iniciar el contenedor

    docker stop apiMongo
    docker start apiMongo

###Ejecución de comandos dentro del contenedor
Para ingresar a la línea de comandos del contenedor:

    docker exec -ti apiMongo bash

Para ejecutar mongo dentro del contenedor, y realizar alguna operación de base de datos:

    mongo

En este punto es posible ejecutar cualquier query.

Importación de Scripts para la base de datos
Copiar cualquier script a ejecutar dentro de la carpeta `/var/scripts` del host

Verificar el enlace de la carpeta dentro del contenedor

Ejecutar el script `./main.sh`

* Los scripts están disponibles en

      src/main/resources/scripts
* Esta ruta debe enlazarse con la ruta que se señale en el parametro `-v`

###Asegurar la base de datos con contraseña
Eliminar el contenedor, y volverlo a crear, añadiendo el parámetro `--auth`:

    docker stop apiMongo
    docker rm apiMongo
    docker run --name apiMongo -p 1224:27017 -d -v /var/scripts/:/var/scripts/ -v apiDBVolume:/data/db --network apiServers mongo:latest --auth

El parámetro --auth permite activar la seguridad de la base de datos, lo cual indica que esta asegurada con un password y contraseña. Para la base de datos de api, se tienen las siguientes credenciales:

* user: `api`
* password: `4P1D4t4b4s3.`

Permitir que los contenedor reinicien con el sistema
Los contenedores por defecto no se levantan con un reinicio del sistema, para hacerlo, se necesita especificar este comportamiento:

    docker update --restart=always apiMongo
    docker update --restart=always apiWildfly

Conectarse a la base de datos y hacer una comprobación de la base de datos creada
Debido a que la base de datos está actualmente asegurada, es necesario conectarse a línea de comandos:

    docker exec -ti apiMongo bash

Debemos autenticarse usando las anteriores credenciales:

    db.auth("api", "4P1D4t4b4s3.");