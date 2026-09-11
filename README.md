# Laboratorio 02 - Docker Compose

Para esta actividad despliego una API hecha en Java con Docker Compose, junto con una base de datos PostgreSQL. La idea es tener 3 copias corriendo de la misma API (todas armadas localmente, no bajadas de internet) más la base de datos.

## Qué usé

- Una API en Java bien simple, hecha con el servidor HTTP que ya trae el JDK (no usé Spring ni nada externo para no complicarme).
- PostgreSQL como base de datos.
- Docker Compose para levantar todo junto.

## Cómo está organizado

lab02-docker-compose/
├── api/
│ ├── Dockerfile
│ └── src/Main.java
├── db-init/
│ └── init.sql
├── docker-compose.yml
├── .env.example
└── .gitignore

## Antes de correrlo

Hay que copiar el archivo de ejemplo de variables de entorno:

```bash
cp .env.example .env
```

Y ahí dentro puedes cambiar el usuario, la contraseña o el nombre de la base si quieres. El `.env` real no se sube al repo (está en el .gitignore) porque ahí van las contraseñas.

## Comandos que usé

Para levantar todo:
```bash
docker compose up -d --build
```

Para ver si los contenedores están corriendo:
```bash
docker compose ps
```

Para probar que cada instancia responde:
```bash
curl http://localhost:3001
curl http://localhost:3002
curl http://localhost:3003
```

Para ver los logs de algún servicio (por ejemplo si algo no prende):
```bash
docker compose logs db
```

Para apagar todo:
```bash
docker compose down
```

## Volúmenes que usé

Usé dos tipos distintos a propósito:

- Un **volumen con nombre** (`db-data`) para que los datos de Postgres no se borren cada vez que apago y prendo los contenedores.
- Un **bind mount** que monta la carpeta `db-init` dentro del contenedor, para que corra el script `init.sql` la primera vez que se crea la base de datos.

## Tipos de red en Docker

Investigando encontré que Docker maneja varios tipos de red:

- **bridge**: es la que usa por defecto, crea una red interna en tu compu para que los contenedores se hablen entre sí. Es la que uso yo en este proyecto.
- **host**: el contenedor usa directamente la red de tu computadora, sin aislarse.
- **overlay**: sirve para conectar contenedores que están en máquinas distintas (se usa más en Docker Swarm).
- **macvlan**: le da al contenedor su propia IP como si fuera un dispositivo más en la red.
- **none**: el contenedor queda sin red, aislado por completo.

## Tipos de volumen en Docker

- **Named volumes (con nombre)**: los administra Docker por su cuenta, son la forma recomendada para guardar datos que necesitan persistir.
- **Bind mounts**: conectan una carpeta de tu compu directamente con una del contenedor. Los uso para el script de inicialización de la BD.
- **tmpfs**: se guarda solo en la memoria RAM, o sea que cuando apagas el contenedor se pierde todo. No lo usé en este proyecto pero también existe.