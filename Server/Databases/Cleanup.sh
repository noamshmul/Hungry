#!/bin/bash

# Container names
MYSQL_CONTAINER="mysql"
MONGODB_CONTAINER="mongodb"

echo "Starting cleanup process..."

if [ "$(docker ps -a -q -f name=^/${MYSQL_CONTAINER}$)" ]; then
    docker stop ${MYSQL_CONTAINER} > /dev/null 2>&1
    docker rm -f ${MYSQL_CONTAINER} > /dev/null 2>&1
    echo "MySQL container removed"
else
    echo "MySQL container not found"
fi

if [ "$(docker ps -a -q -f name=^/${MONGODB_CONTAINER}$)" ]; then
    docker stop ${MONGODB_CONTAINER} > /dev/null 2>&1
    docker rm -f ${MONGODB_CONTAINER} > /dev/null 2>&1
    echo "MongoDB container removed"
else
    echo "MongoDB container not found"
fi

if command -v docker-compose > /dev/null && [ -f "docker-compose.yml" ]; then
    docker-compose down -v > /dev/null 2>&1
    echo "Docker Compose services removed"
fi

echo "Removing volumes..."
if [ "$(docker volume ls -q -f name=mysql-data)" ]; then
    docker volume rm mysql-data > /dev/null 2>&1
fi

if [ "$(docker volume ls -q -f name=mongodb-data)" ]; then
    docker volume rm mongodb-data > /dev/null 2>&1
fi

if [ "$(docker volume ls -q -f name=mystack_mysql-data)" ]; then
    docker volume rm mystack_mysql-data > /dev/null 2>&1
    echo "Stack MySQL volume removed"
fi

if [ "$(docker volume ls -q -f name=mystack_mongodb-data)" ]; then
    docker volume rm mystack_mongodb-data > /dev/null 2>&1
    echo "Stack MongoDB volume removed"
fi

echo "Removing any dangling volumes..."
docker volume prune -f > /dev/null 2>&1

echo "Cleanup complete!"