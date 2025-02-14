
from pymongo import MongoClient
import settings

class MongoDB_Base:

    def connect_to_mongodb(self):
        if not self.is_connected():
            settings.CONNECTION = MongoClient(f"mongodb://{settings.MONGODB_USER}:{settings.MONGODB_PASSWORD}@localhost:{settings.MONGODB_PORT}/")

    def is_connected(self):
        try:
            if settings.CONNECTION is not None:
                settings.CONNECTION.server_info()
                return True
            return False
        except Exception as e:
            return False

    def get_recipe(self, recipe_name):
        try:
            self.connect_to_mongodb()
            collection = settings.CONNECTION[settings.DATABASE_NAME][settings.COLLECTION_NAME]
            recipe = collection.find_one(
                {recipe_name: {"$exists": True}},
                {recipe_name: 1}
            )
            if recipe_name in recipe:
                return recipe[recipe_name]
            else:
                print(f"Recipe '{recipe_name}' not found")
                return None
        except Exception as e:
            print(f"An error occurred: {e}")
            return None





def main():
    print("Not set yet")

if __name__ == "__main__":
    main()