from pymongo import MongoClient
from log import logger

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
            return CONNECTION.get_database(database_name).get_collection(collection_name).find(query, projection)
        except Exception as e:
            logger.error("Failed with query: " + query)
            return None
        
    def aggregate(self, pipeline, database_name=DATABASE_NAME, collection_name=COLLECTION_NAME):
        try:
            self.connect_to_mongodb(self)  # Ensure the connection is established
            collection = CONNECTION.get_database(database_name).get_collection(collection_name)
            return collection.aggregate(pipeline)
        except Exception as e:
            logger.error("Failed to aggregate with pipeline: " + str(pipeline) + " Error: " + str(e))
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
                logger.error(f"Recipe '{recipe_name}' not found")
                return None
        except Exception as e:
            return None

    def get_recipe_by_id(self, recipe_id):
        try:
            recipe = self.mongodb_base.get(self.mongodb_base ,{"_id": recipe_id})
            if recipe:
                return recipe
            else:
                logger.error(f"Recipe '{recipe_id}' not found")
                return None
        except Exception as e:
            return None

    def get_closest_recipe_with_ids(self, ingredient_ids, diff):
        try:
            # Aggregation pipeline to compute the number of extra ingredients
            pipeline = [
                # Convert ingredients object into an array of keys.
                {
                    "$addFields": {
                        "ingredientKeys": {
                            "$map": {
                                "input": { "$objectToArray": "$ingredients" },
                                "as": "ing",
                                "in": "$$ing.k"
                            }
                        }
                    }
                },
                # Compute how many ingredients are extra (not in provided list)
                {
                    "$addFields": {
                        "extraCount": {
                            "$size": {
                                "$setDifference": [ "$ingredientKeys", ingredient_ids ]
                            }
                        }
                    }
                },
                # Only include recipes with at most 1 extra ingredient.
                {
                    "$match": {
                        "extraCount": { "$lte": diff }
                    }
                },
                # Sort by the extra ingredient count (lowest first)
                {
                    "$sort": { "extraCount": 1 }
                },
                # Limit to the top 3 closest recipes
                {
                    "$limit": 3
                },
                # # Return only the _id field
                # {
                #     "$project": { "_id": 1 }
                # }
            ]
            
            recipes = list(self.mongodb_base.aggregate(self.mongodb_base ,pipeline))
            if recipes:
                return recipes
            else:
                logger.error(f"Recipes with the ingredients {ingredient_ids} not found")
                return None
        except Exception as e:
            return None

    def get_recipe_with_ids(self, ingredient_ids):
        try:
            query = {
                "$expr": {
                    "$setIsSubset": [
                        { "$map": {
                            "input": { "$objectToArray": "$ingredients" },
                            "as": "ing",
                            "in": "$$ing.k"
                        } },
                        ingredient_ids
                    ]
                }
            }
            recipes = self.mongodb_base.get_all(self.mongodb_base ,query)
            if recipes:
                return list(recipes)
            else:
                logger.error(f"Recipes with the ingredients {ingredient_ids} not found")
                return None
        except Exception as e:
            return None

def main():
    base = MongoDB_Base
    func = MongoDB_Functions(base)
    print("Hello from Recipes")
    print(func.get_recipe_by_id("54a40a396529d92b2c003c20").get('name'))

if __name__ == "__main__":
    main()