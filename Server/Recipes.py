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
            logger.error("Failed with query: " + str(query))
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
            recipe = self.mongodb_base.get(self.mongodb_base,{"name": recipe_name})
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

    def get_closest_recipe_with_ids(self, ingredients, diff):
        try:
            # Aggregation pipeline to compute the number of extra ingredients
            pipeline = [
                # 1. Convert the ingredients object into an array of keys.
                {
                    "$addFields": {
                        "ingredientKeys": {
                            "$map": {
                                "input": {"$objectToArray": "$ingredients"},
                                "as": "ing",
                                "in": "$$ing.k"
                            }
                        }
                    }
                },
                # 2. Compute the number of extra ingredients (keys not in required_dict).
                {
                    "$addFields": {
                        "extraCount": {
                            "$size": {
                                "$setDifference": ["$ingredientKeys", list(ingredients.keys())]
                            }
                        }
                    }
                },
                # 3. For each ingredient that is in required_dict, check that the quantity is enough.
                {
                    "$addFields": {
                        "quantityCheck": {
                            "$allElementsTrue": {
                                "$map": {
                                    "input": {
                                        "$filter": {
                                            "input": {"$objectToArray": "$ingredients"},
                                            "as": "ing",
                                            "cond": {"$in": ["$$ing.k", list(ingredients.keys())]}
                                        }
                                    },
                                    "as": "ing",
                                    "in": {
                                        "$lte": [
                                            "$$ing.v.quantity",
                                            {
                                                "$function": {
                                                    "body": "function(key, req) { return req[key]; }",
                                                    "args": ["$$ing.k", ingredients],
                                                    "lang": "js"
                                                }
                                            }
                                        ]
                                    }
                                }
                            }
                        }
                    }
                },
                # 4. Only match recipes with at most 1 extra ingredient and that pass the quantity check.
                {
                    "$match": {
                        "extraCount": {"$lte": diff},
                        "quantityCheck": True
                    }
                },
                # 5. Sort by extraCount (lowest first) and limit to the top 3.
                { "$sort": { "extraCount": 1 } }
            ]
            
            recipes = list(self.mongodb_base.aggregate(self.mongodb_base ,pipeline))
            if recipes:
                return recipes
            else:
                logger.error(f"Recipes with the ingredients {ingredients} not found")
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
        
    def get_all_recipes(self):
        try:
            recipes = self.mongodb_base.get_all(self.mongodb_base, {}, {"name": 1, "image": 1})
            if recipes:
                return list(recipes)
            else:
                print(f"No Recipes found")
                return None
        except Exception as e:
            return None

    def get_favorite_recipes(self, favorites):
        try:
            from bson import ObjectId
            query = {"_id": {"$in": favorites}}
            projection = {"name": 1, "image": 1}  # Fetch only name & image
            logger.info("test0 %s", query)

            recipes = self.mongodb_base.get_all(self.mongodb_base,query,projection)
            if recipes:
                return list(recipes)
            else:
                return None
        except Exception as e:
            logger.error(e)
            return None


def main():
    base = MongoDB_Base
    func = MongoDB_Functions(base)
    print("Hello from Recipes")
    print(func.get_recipe_by_id("54a40a396529d92b2c003c20").get('name'))

if __name__ == "__main__":
    main()