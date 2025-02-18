from pymongo import MongoClient
import settings
import json

def is_connected():
    try:
        if settings.CONNECTION is not None:
            settings.CONNECTION.server_info()
            return True
        return False
    except Exception as e:
        return False

def connect_to_mongodb():
    if not is_connected():
        settings.CONNECTION = MongoClient(f"mongodb://{settings.MONGODB_USER}:{settings.MONGODB_PASSWORD}@localhost:{settings.MONGODB_PORT}/")


def get(query, projection = None, database_name = settings.DATABASE_NAME, collection_name = settings.COLLECTION_NAME):
    try:
        connect_to_mongodb()
        return settings.CONNECTION.get_database(database_name).get_collection(collection_name).find_one(query, projection)
    except Exception as e:
        print(f"An error occurred: {e}")
        return None


def set(query, new_data, database_name=settings.DATABASE_NAME, collection_name=settings.COLLECTION_NAME, upsert=True):
    try:
        connect_to_mongodb()
        collection = settings.CONNECTION.get_database(database_name).get_collection(collection_name)
        result = collection.update_one(
            query,
            {'$set': new_data},
            upsert=upsert
        )
        return result.upserted_id
    except Exception as e:
        print(f"An error occurred: {e}")
        return None


def delete(query, database_name=settings.DATABASE_NAME, collection_name=settings.COLLECTION_NAME):
    try:
        connect_to_mongodb()
        collection = MongoClient(settings.CONNECTION).get_database(database_name).get_collection(collection_name)
        result = collection.delete_one(query)
        return result.deleted_count
    except Exception as e:
        print(f"An error occurred: {e}")
        return None














## TESTINGGGG


def main():


    noam_item = {
        "_id": 11,
        "name": "noam_item",
        "instructions": "1. noam 2. noamnoam",
        "approx_time": 99,
        "ingridients": {
            "123123": {
                "quantity": 456,
                "original_quantity": "3 potatoes"
            },
            "321321": {
                "quantity": 345,
                "original_quantity": "1L vegetable broth"
            },
            "32123": {
                "quantity": 654,
                "original_quantity": "400g mixed vegetables"
            }
        },
        "size": 3
    }

    # 1. Set the document
    print("Step 1: Creating/updating the document")
    query = {"noam_item": {"$exists": True}}
    result = set(query, noam_item)
    print("Set/Update result:", result)

    # 2. Verify the document was created using get
    print("\nStep 2: Verifying the document exists")
    get_result = get(query, {"noam_item": 1, "_id": 11})
    print("Get result:", get_result)

    print("\nStep 00000000000000000000000: Entire data base")
    final_get = get({ })
    print("Final get result:", final_get)

    # 3. Delete the document
    print("\nStep 3: Deleting the document")
    delete_result = delete({"noam_item": {"$exists": True, '$regex': 'noam_item'}})
    print("Delete result:", delete_result)

    # 4. Verify the document was deleted using get
    print("\nStep 4: Verifying the document was deleted")
    final_get = get(query)
    print("Final get result:", final_get)

    print("\nStep 00000000000000000000000: Entire data base")
    final_get = get({ })
    print("Final get result:", final_get)


    #obj = get({"noam_item": {"$exists": True}}, {"noam_item": 1, "_id": 0})
    #print(json.dumps(obj, indent=1))

if __name__ == "__main__":
    main()