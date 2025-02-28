#!/bin/bash

# Database variables
MYSQL_PASSWORD="admin"
MYSQL_DATABASE="hungrydb"
MONGO_USERNAME="admin"
MONGO_PASSWORD="password123"

# Container names
MYSQL_CONTAINER="mysql"
MONGODB_CONTAINER="mongodb"

# Create initialization directories
mkdir -p ./mysql-init
mkdir -p ./mongo-init

# Create MySQL initialization script
cat > ./mysql-init/init.sql << EOF
CREATE DATABASE IF NOT EXISTS \`${MYSQL_DATABASE}\`;
ALTER USER '${MYSQL_USER}'@'%' IDENTIFIED WITH mysql_native_password BY '${MYSQL_PASSWORD}';
GRANT ALL PRIVILEGES ON \`${MYSQL_DATABASE}\`.* TO '${MYSQL_USER}'@'%';
FLUSH PRIVILEGES;
EOF

# Create MongoDB initialization script
cat > ./mongo-init/init.js << EOF
db = db.getSiblingDB('admin');
db.auth('${MONGO_USERNAME}', '${MONGO_PASSWORD}');
db = db.getSiblingDB('${MYSQL_DATABASE}');
db.createUser({
  user: '${MYSQL_USER}',
  pwd: '${MYSQL_PASSWORD}',
  roles: [{role: 'readWrite', db: '${MYSQL_DATABASE}'}]
});
EOF

# If containers exist but are stopped or running, remove them
if [ "$(docker ps -a -q -f name=^/${MYSQL_CONTAINER}$)" ]; then
    echo "Removing existing MySQL container"
    docker rm -f ${MYSQL_CONTAINER}
fi

if [ "$(docker ps -a -q -f name=^/${MONGODB_CONTAINER}$)" ]; then
    echo "Removing existing MongoDB container"
    docker rm -f ${MONGODB_CONTAINER}
fi

# Stop and remove any existing compose services
if [ -f "docker-compose.yml" ]; then
    echo "Stopping any existing Docker Compose services"
    docker-compose down -v
fi

# Remove any existing volumes with the same names
docker volume rm mysql-data mongodb-data 2>/dev/null || true

# Start containers
echo "Starting Docker Compose services"
docker-compose up -d

# Wait a bit for containers to initialize
echo "Waiting for containers to initialize..."
sleep 10

# Display connection information
echo "----------------------------------------"
echo "MySQL:"
echo "  - Host: localhost:3306"
echo "  - Root user: ${MYSQL_USER}"
echo "  - Root password: ${MYSQL_PASSWORD}"
echo "  - Database: ${MYSQL_DATABASE}"
echo ""
echo "MongoDB:"
echo "  - Host: localhost:27017"
echo "  - Admin user: ${MONGO_USERNAME}"
echo "  - Admin password: ${MONGO_PASSWORD}"
echo "  - Database: ${MYSQL_DATABASE}"
echo "  - User: ${MYSQL_USER}"
echo "  - Password: ${MYSQL_PASSWORD}"
echo "----------------------------------------"
echo "To connect to MySQL: mysql -h localhost -P 3306 -u ${MYSQL_USER} -p${MYSQL_PASSWORD} ${MYSQL_DATABASE}"
echo "To connect to MongoDB: mongosh mongodb://${MYSQL_USER}:${MYSQL_PASSWORD}@localhost:27017/${MYSQL_DATABASE}"