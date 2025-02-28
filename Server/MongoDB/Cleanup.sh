#!/bin/bash

# Container names
MYSQL_CONTAINER="mysql"
MONGODB_CONTAINER="mongodb"

echo "Starting cleanup process..."

# Stop and remove containers if they exist
if [ "$(docker ps -a -q -f name=^/${MYSQL_CONTAINER}$)" ]; then
    echo "Stopping and removing MySQL container"
    docker stop ${MYSQL_CONTAINER} > /dev/null 2>&1
    docker rm -f ${MYSQL_CONTAINER} > /dev/null 2>&1
    echo "MySQL container removed"
else
    echo "MySQL container not found"
fi

if [ "$(docker ps -a -q -f name=^/${MONGODB_CONTAINER}$)" ]; then
    echo "Stopping and removing MongoDB container"
    docker stop ${MONGODB_CONTAINER} > /dev/null 2>&1
    docker rm -f ${MONGODB_CONTAINER} > /dev/null 2>&1
    echo "MongoDB container removed"
else
    echo "MongoDB container not found"
fi

# Check if docker-compose is available and a docker-compose.yml file exists
if command -v docker-compose > /dev/null && [ -f "docker-compose.yml" ]; then
    echo "Stopping and removing all containers in docker-compose.yml"
    docker-compose down -v > /dev/null 2>&1
    echo "Docker Compose services removed"
fi

# Remove volumes
echo "Removing volumes..."
if [ "$(docker volume ls -q -f name=mysql-data)" ]; then
    docker volume rm mysql-data > /dev/null 2>&1
    echo "MySQL volume removed"
else
    echo "MySQL volume not found"
fi

if [ "$(docker volume ls -q -f name=mongodb-data)" ]; then
    docker volume rm mongodb-data > /dev/null 2>&1
    echo "MongoDB volume removed"
else
    echo "MongoDB volume not found"
fi

# Check if mystack_mysql-data and mystack_mongodb-data exist (compose named volumes)
if [ "$(docker volume ls -q -f name=mystack_mysql-data)" ]; then
    docker volume rm mystack_mysql-data > /dev/null 2>&1
    echo "Stack MySQL volume removed"
fi

if [ "$(docker volume ls -q -f name=mystack_mongodb-data)" ]; then
    docker volume rm mystack_mongodb-data > /dev/null 2>&1
    echo "Stack MongoDB volume removed"
fi

# Remove any dangling volumes (optional)
echo "Removing any dangling volumes..."
docker volume prune -f > /dev/null 2>&1

echo "Cleanup complete!"