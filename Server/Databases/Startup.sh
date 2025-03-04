#!/bin/bash

# Database variables
MYSQL_PASSWORD="root"
MYSQL_DATABASE="hungrydb"
MYSQL_USER="root"
MONGO_USERNAME="admin"
MONGO_PASSWORD="password123"

# Container names
MYSQL_CONTAINER="mysql"
MONGODB_CONTAINER="mongodb"

if [ "$(docker ps -a -q -f name=^/${MYSQL_CONTAINER}$)" ]; then
    echo "Removing existing MySQL container"
    docker rm -f ${MYSQL_CONTAINER}
fi

if [ "$(docker ps -a -q -f name=^/${MONGODB_CONTAINER}$)" ]; then
    echo "Removing existing MongoDB container"
    docker rm -f ${MONGODB_CONTAINER}
fi

if [ -f "docker-compose.yml" ]; then
    echo "Stopping any existing Docker Compose services"
    docker-compose down -v
fi

docker volume rm mysql-data mongodb-data 2>/dev/null || true

echo "Starting Docker Compose services"
docker-compose up -d


echo "----------------------------------------"
echo "To connect to MySQL: mysql -h localhost -P 3306 -u ${MYSQL_USER} -p${MYSQL_PASSWORD} ${MYSQL_DATABASE}"
echo "Or via any CLI: docker exec -it ${MYSQL_CONTAINER} mysql -u ${MYSQL_USER} -p${MYSQL_PASSWORD} ${MYSQL_DATABASE}"
echo "To connect to MongoDB: mongosh mongodb://${MONGO_USERNAME}:${MONGO_PASSWORD}@localhost:27017/${MYSQL_DATABASE}"