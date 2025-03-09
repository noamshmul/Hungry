#!/bin/bash

# Databases variables
MONGO_CONTAINER="mongodb"
MONGO_USERNAME="admin"
MONGO_PASSWORD="password123"
MONGO_DATABASE="hungrydb"
MONGO_COLLECTION="recipes"
MONGO_JSON_FILE="data.json"

# MySQL variables
MYSQL_CONTAINER="mysql"
MYSQL_USER="root"
MYSQL_PASSWORD="root"
MYSQL_DATABASE="hungrydb"
MYSQL_SQL_FILE="data.sql"

if [ ! -f "$MONGO_JSON_FILE" ]; then
    echo "Error: $MONGO_JSON_FILE not found in the current directory."
    echo "MongoDB import will be skipped."
    SKIP_MONGO=true
else
    SKIP_MONGO=false
fi

if [ ! -f "$MYSQL_SQL_FILE" ]; then
    echo "Error: $MYSQL_SQL_FILE not found in the current directory."
    echo "MySQL import will be skipped."
    SKIP_MYSQL=true
else
    SKIP_MYSQL=false
fi

if [ "$SKIP_MONGO" = true ] && [ "$SKIP_MYSQL" = true ]; then
    echo "Error: Both data files are missing. Nothing to import."
    exit 1
fi

if [ "$SKIP_MONGO" = false ]; then
    if [ ! "$(docker ps -q -f name=^/${MONGO_CONTAINER}$)$" ]; then
        echo "Error: MongoDB container is not running."
        echo "Please make sure the container named '$MONGO_CONTAINER' is up and running."
        SKIP_MONGO=true
    else
        echo "========== MongoDB Import =========="
        echo "Importing data from $MONGO_JSON_FILE into MongoDB..."

        echo "Copying $MONGO_JSON_FILE to the container..."
        docker cp "$MONGO_JSON_FILE" "$MONGO_CONTAINER:/$MONGO_JSON_FILE"

        echo "Importing data into MongoDB..."
        docker exec "$MONGO_CONTAINER" mongoimport \
            --username "$MONGO_USERNAME" \
            --password "$MONGO_PASSWORD" \
            --authenticationDatabase "admin" \
            --db "$MONGO_DATABASE" \
            --collection "$MONGO_COLLECTION" \
            --file "$MONGO_JSON_FILE" \
            --jsonArray

        if [ $? -eq 0 ]; then
            echo "MongoDB data import completed successfully!"
            echo "Your data has been imported into the '$MONGO_DATABASE' database, collection '$MONGO_COLLECTION'."

            # docker exec "$MONGO_CONTAINER" rm "$MONGO_JSON_FILE"
        else
            echo "MongoDB data import failed."
            echo "Please check your JSON file format and MongoDB connection settings."
        fi
    fi
fi

if [ "$SKIP_MYSQL" = false ]; then
    if [ ! "$(docker ps -q -f name=^/${MYSQL_CONTAINER}$)" ]; then
        echo "Error: MySQL container is not running."
        echo "Please make sure the container named '$MYSQL_CONTAINER' is up and running."
        SKIP_MYSQL=true
    else
        echo "========== MySQL Import =========="
        echo "Importing data from $MYSQL_SQL_FILE into MySQL..."

        echo "Copying $MYSQL_SQL_FILE to the container..."
        docker cp "$MYSQL_SQL_FILE" "$MYSQL_CONTAINER:/tmp/$MYSQL_SQL_FILE"

        echo "Importing data into MySQL..."
        docker exec "$MYSQL_CONTAINER" mysql \
            -u"$MYSQL_USER" \
            -p"$MYSQL_PASSWORD" \
            "$MYSQL_DATABASE" \
            -e "source /tmp/$MYSQL_SQL_FILE"

        if [ $? -eq 0 ]; then
            echo "MySQL data import completed successfully!"
            echo "Your data has been imported into the '$MYSQL_DATABASE' database."

            docker exec "$MYSQL_CONTAINER" rm "/tmp/$MYSQL_SQL_FILE"
        else
            echo "MySQL data import failed."
            echo "Please check your SQL file format and MySQL connection settings."
        fi
    fi
fi

echo "Import operations completed."