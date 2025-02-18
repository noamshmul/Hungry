from Server.MongoDB import MongoDB_Base
import settings


def get_recipe(recipe_name):
    try:
        MongoDB_Base.connect_to_mongodb()
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
    print("noam")

def __main__():
    main()