import docker
import time

from docker.errors import NotFound
from pymongo.errors import ServerSelectionTimeoutError
import json
import os
import settings
from Server.MongoDB.MongoDB_Base import MongoDB_Base


class MongoDB_Setup:
    def __init__(self):
        self.client = docker.from_env()

    def check_container_exists(self):
        try:
            self.client.containers.get(settings.CONTAINER_NAME)
            return True
        except docker.errors.NotFound:
            return False

    def create_mongodb_container(self):
        if self.check_container_exists():
            print(f"Container '{settings.CONTAINER_NAME}' already exists")
            self.client.containers.get(settings.CONTAINER_NAME).start()
            return True
        try:
            self.client.containers.run(
                'mongo:latest',
                name=settings.CONTAINER_NAME,
                environment=[
                    f"MONGO_INITDB_ROOT_USERNAME={settings.MONGODB_USER}",
                    f"MONGO_INITDB_ROOT_PASSWORD={settings.MONGODB_PASSWORD}"
                ],
                ports={'27017/tcp': settings.MONGODB_PORT},
                volumes={
                    'mongo': {'bind': '/data/db', 'mode': 'rw'}
                },
                detach=True
            )
            print(f"Container '{settings.CONTAINER_NAME}' created successfully")
            return True
        except docker.errors.APIError as e:
            print(f"Error creating container: {e}")
            raise

    def wait_for_mongodb(self, mongodb_base):
        for attempt in range(settings.MAX_RETRIES):
            try:
                mongodb_base.connect_to_mongodb()
                settings.CONNECTION.close()
                print("MongoDB is ready!")
                return True
            except ServerSelectionTimeoutError:
                print(f"Waiting for MongoDB to be ready... (Attempt {attempt + 1}/{settings.MAX_RETRIES})")
                time.sleep(settings.RETRY_DELAY)

        raise Exception("MongoDB failed to become ready in time")

    def setup_database(self, mongodb_base):

        try:
            mongodb_base.connect_to_mongodb()

            db_list = settings.CONNECTION.list_database_names()
            if settings.DATABASE_NAME in db_list:
                print(f"Database '{settings.DATABASE_NAME}' already exists")
            else:
                print(f"Creating database '{settings.DATABASE_NAME}'")

            db = settings.CONNECTION[settings.DATABASE_NAME]
            collection = db[settings.COLLECTION_NAME]

            if settings.DATA_FILE and os.path.exists(settings.DATA_FILE):
                with open(settings.DATA_FILE, 'r') as file:
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
        status = {
            "container": "Not to remove",
            "image": "Not to remove",
            "volume": "Not to remove"
        }

        try:
            if settings.DELETE_CONTAINER:
                try:
                    container = self.client.containers.get(settings.CONTAINER_NAME)
                    container.stop()
                    container.remove()
                    status["container"] = "Removed"
                    print(f"Container '{settings.CONTAINER_NAME}' removed successfully")
                except NotFound:
                    status["container"] = "Not found"
                    print(f"Container '{settings.CONTAINER_NAME}' not found")

            if settings.DELETE_IMAGE:
                try:
                    self.client.images.get("mongo:latest")
                    self.client.images.remove("mongo:latest", force=True)
                    status["image"] = "Removed"
                    print("MongoDB image removed successfully")
                except NotFound:
                    status["image"] = "Not found"
                    print("MongoDB image not found")

            if settings.DELETE_VOLUME:
                try:
                    self.client.volumes.get(settings.DOCKER_VOLUME_NAME).remove(force=True)
                    status["volume"] = "Removed"
                    print(f"Volume '{settings.DOCKER_VOLUME_NAME}' removed successfully")
                except NotFound:
                    status["volume"] = "Not found"
                    print(f"Volume '{settings.DOCKER_VOLUME_NAME}' not found")

            return status

        except Exception as e:
            print(f"An error occurred during cleanup: {e}")
            return status
        finally:
            self.client.close()


def main():

    try:

        mongo_base = MongoDB_Base()
        mongo_setup = MongoDB_Setup()
        mongo_setup.create_mongodb_container()
        mongo_setup.wait_for_mongodb(mongo_base)
        mongo_setup.setup_database(mongo_base)

        print("MongoDB setup completed successfully!")

        #print(json.dumps(mongo_base.get_recipe(settings.TEST_RECIPE), indent=1)) # For test
        settings.CONNECTION.close()
        mongo_setup.cleanup_docker()

    except Exception as e:
        print(f"An error occurred during setup: {e}")


if __name__ == "__main__":
    main()