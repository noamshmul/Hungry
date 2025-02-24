import docker
import time
import json
import os
from pymongo import MongoClient
from docker.errors import NotFound
from pymongo.errors import ServerSelectionTimeoutError

# Database Settings
MONGODB_HOST = "localhost"
MONGODB_PORT = 27017
MONGODB_USER = "admin"
MONGODB_PASSWORD = "password123"

# Database and Collection Names
DATABASE_NAME = "database"
COLLECTION_NAME = "recipes"
# Database test search name
TEST_RECIPE = "Vegetable Soup"

# Data Path
DATA_FILE = "data.json"

# Connection Settings
CONNECTION = None  # Global connection. DO NOT CHANGE!!!
MAX_RETRIES = 30
RETRY_DELAY = 1  # seconds

# Docker Settings
DELETE_CONTAINER = False
DELETE_IMAGE = False  # We always use the same image (Recommended: always use False)
DELETE_VOLUME = False
CONTAINER_NAME = "mongo"
DOCKER_VOLUME_NAME = "mongo"


class MongoDB_Base:
    def is_connected(self):
        try:
            if CONNECTION is not None:
                CONNECTION.server_info()
                return True
            return False
        except Exception as e:
            return False

    def connect_to_mongodb(self):
        global CONNECTION
        if not self.is_connected():
            CONNECTION = MongoClient(f"mongodb://{MONGODB_USER}:{MONGODB_PASSWORD}@localhost:{MONGODB_PORT}/")

    def get(self, query, projection=None, database_name=DATABASE_NAME, collection_name=COLLECTION_NAME):
        try:
            self.connect_to_mongodb()
            return CONNECTION.get_database(database_name).get_collection(collection_name).find_one(query, projection)
        except Exception as e:
            print(f"An error occurred: {e}")
            return None

    def set(self, query, new_data, database_name=DATABASE_NAME, collection_name=COLLECTION_NAME, upsert=True):
        try:
            self.connect_to_mongodb()
            collection = CONNECTION.get_database(database_name).get_collection(collection_name)
            result = collection.update_one(
                query,
                {'$set': new_data},
                upsert=upsert
            )
            return result.upserted_id
        except Exception as e:
            print(f"An error occurred: {e}")
            return None

    def delete(self, query, database_name=DATABASE_NAME, collection_name=COLLECTION_NAME):
        try:
            self.connect_to_mongodb()
            collection = CONNECTION.get_database(database_name).get_collection(collection_name)
            result = collection.delete_one(query)
            return result.deleted_count
        except Exception as e:
            print(f"An error occurred: {e}")
            return None

class MongoDB_Functions:
    def __init__(self, mongodb_base):
        self.mongodb_base = mongodb_base

    def get_recipe_by_name(self, recipe_name):
        try:
            recipe = self.mongodb_base.get({"name": recipe_name})
            if recipe:
                return recipe
            else:
                print(f"Recipe '{recipe_name}' not found")
                return None
        except Exception as e:
            print(f"An error occurred: {e}")
            return None

    def get_recipe_by_id(self, recipe_id):
        try:
            recipe = self.mongodb_base.get({"_id": recipe_id})
            if recipe:
                return recipe
            else:
                print(f"Recipe '{recipe_id}' not found")
                return None
        except Exception as e:
            print(f"An error occurred: {e}")
            return None

class MongoDB_Setup:
    def __init__(self):
        self.client = docker.from_env()
        self.mongodb_base = MongoDB_Base()

    def startup(self):

        try:
            self.create_mongodb_container()
            self.wait_for_mongodb()
            self.setup_database()

            print("MongoDB setup completed successfully!")

            # print(json.dumps(mongodb_base.get_recipe(TEST_RECIPE), indent=1))  # For test
            global CONNECTION
            if CONNECTION:
                CONNECTION.close()
            self.cleanup_docker()

        except Exception as e:
            print(f"An error occurred during setup: {e}")

    def check_container_exists(self):
        try:
            self.client.containers.get(CONTAINER_NAME)
            return True
        except docker.errors.NotFound:
            return False

    def create_mongodb_container(self):
        if self.check_container_exists():
            print(f"Container '{CONTAINER_NAME}' already exists")
            self.client.containers.get(CONTAINER_NAME).start()
            return True
        try:
            self.client.containers.run(
                'mongo:latest',
                name=CONTAINER_NAME,
                environment=[
                    f"MONGO_INITDB_ROOT_USERNAME={MONGODB_USER}",
                    f"MONGO_INITDB_ROOT_PASSWORD={MONGODB_PASSWORD}"
                ],
                ports={'27017/tcp': MONGODB_PORT},
                volumes={
                    'mongo': {'bind': '/data/db', 'mode': 'rw'}
                },
                detach=True
            )
            print(f"Container '{CONTAINER_NAME}' created successfully")
            return True
        except docker.errors.APIError as e:
            print(f"Error creating container: {e}")
            raise

    def wait_for_mongodb(self):
        global CONNECTION
        for attempt in range(MAX_RETRIES):
            try:
                self.mongodb_base.connect_to_mongodb()
                CONNECTION.close()
                print("MongoDB is ready!")
                return True
            except ServerSelectionTimeoutError:
                print(f"Waiting for MongoDB to be ready... (Attempt {attempt + 1}/{MAX_RETRIES})")
                time.sleep(RETRY_DELAY)

        raise Exception("MongoDB failed to become ready in time")

    def setup_database(self):
        global CONNECTION
        try:
            self.mongodb_base.connect_to_mongodb()

            db_list = CONNECTION.list_database_names()
            if DATABASE_NAME in db_list:
                print(f"Database '{DATABASE_NAME}' already exists")
            else:
                print(f"Creating database '{DATABASE_NAME}'")

            db = CONNECTION[DATABASE_NAME]
            collection = db[COLLECTION_NAME]

            if DATA_FILE and os.path.exists(DATA_FILE):
                with open(DATA_FILE, 'r') as file:
                    data = json.load(file)
                if isinstance(data, list):
                    result = collection.insert_many(data)
                    print(f"Inserted {len(result.inserted_ids)} documents")
                else:
                    result = collection.insert_one(data)
                    print(f"Inserted document with id {result.inserted_id}")

        except Exception as e:
            print(f"Error setting up database: {e}")
            raise

    def cleanup_docker(self):

        try:
            if DELETE_CONTAINER:
                try:
                    container = self.client.containers.get(CONTAINER_NAME)
                    container.stop()
                    container.remove()
                    print(f"Container '{CONTAINER_NAME}' removed successfully")
                except NotFound:
                    print(f"Container '{CONTAINER_NAME}' not found")

            if DELETE_VOLUME:
                try:
                    self.client.volumes.get(DOCKER_VOLUME_NAME).remove(force=True)
                    print(f"Volume '{DOCKER_VOLUME_NAME}' removed successfully")
                except NotFound:
                    print(f"Volume '{DOCKER_VOLUME_NAME}' not found")

        except Exception as e:
            print(f"An error occurred during cleanup: {e}")
        finally:
            self.client.close()

def main():
    mongodb_base = MongoDB_Base()
    mongodb_functions = MongoDB_Functions(mongodb_base)
    print(mongodb_functions.get_recipe_by_name(TEST_RECIPE))
    print(mongodb_functions.get_recipe_by_id(8))
    #mongodb_setup = MongoDB_Setup()
    #mongodb_setup.startup()
    #mongodb_setup.cleanup_docker()

if __name__ == "__main__":
    main()