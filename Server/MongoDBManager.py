

from pymongo import MongoClient
from pymongo.errors import OperationFailure
from bson import son

def connect_to_mongodb(host='localhost', port=27017, db_name='database'):
    try:
        # Create a connection using MongoClient
        client = MongoClient(host, port)

        # Access the specified database
        db = client[db_name]

        # Verify the connection by listing database names
        client.list_database_names()
        print(f"Successfully connected to MongoDB at {host}:{port}")
        print(f"Available databases: {client.list_database_names()}")

        return db

    except ConnectionError as e:
        print(f"Failed to connect to MongoDB: {e}")
        return None
    except OperationFailure as e:
        print(f"Authentication failed: {e}")
        return None



if __name__ == "__main__":
    # Connect to MongoDB
    db = connect_to_mongodb()


    print(db)



def main():


    print("main func")
