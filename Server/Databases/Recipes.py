import logging

import docker
import time
import json
import os
from pymongo import MongoClient
from docker.errors import NotFound
from pymongo.errors import ServerSelectionTimeoutError
from logging import log
logger = logging.getLogger(__name__)

# Database Settings
MONGODB_HOST = "localhost"
MONGODB_PORT = 27017
MONGODB_USER = "admin"
MONGODB_PASSWORD = "password123"

# Database and Collection Names
DATABASE_NAME = "hungrydb"
COLLECTION_NAME = "recipes"
# Database test search name
TEST_RECIPE = "Vegetable Soup"

# Connection Settings
CONNECTION = None  # Global connection. DO NOT CHANGE!!!

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
        if not self.is_connected(self):
            CONNECTION = MongoClient(f"mongodb://{MONGODB_USER}:{MONGODB_PASSWORD}@localhost:{MONGODB_PORT}/")

    def get(self, query, projection=None, database_name=DATABASE_NAME, collection_name=COLLECTION_NAME):
        try:
            self.connect_to_mongodb(self)
            return CONNECTION.get_database(database_name).get_collection(collection_name).find_one(query, projection)
        except Exception as e:
            logger.error("Failed with query: " + query)
            return None
        
    def get_all(self, query, projection=None, database_name=DATABASE_NAME, collection_name=COLLECTION_NAME):
        try:
            self.connect_to_mongodb(self)
            return (CONNECTION[database_name])[collection_name].find(query, projection)
        except Exception as e:
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
            logger.error("Failed with query: " + query)
            return None

    def delete(self, query, database_name=DATABASE_NAME, collection_name=COLLECTION_NAME):
        try:
            self.connect_to_mongodb()
            collection = CONNECTION.get_database(database_name).get_collection(collection_name)
            result = collection.delete_one(query)
            return result.deleted_count
        except Exception as e:
            logger.error("Failed with query: " + query)
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
            return None

    def get_recipe_by_id(self, recipe_id):
        try:
            recipe = self.mongodb_base.get(self.mongodb_base ,{"_id": recipe_id})
            if recipe:
                return recipe
            else:
                print(f"Recipe '{recipe_id}' not found")
                return None
        except Exception as e:
            return None
    def get_all_recipes(self):
        try:
            recipes = self.mongodb_base.get_all(self.mongodb_base, {}, {"name": 1, "image": 1})
            if recipes:
                return recipes
            else:
                print(f"No Recipes found")
                return None
        except Exception as e:
            return None


def main():
    base = MongoDB_Base
    func = MongoDB_Functions(base)
    print("Hello from Recipes")
    print(func.get_recipe_by_id("54a40a396529d92b2c003c20").get('name'))
    recipes = func.get_all_recipes()
    for recipe in recipes:
        print(recipe)
    

if __name__ == "__main__":
    main()