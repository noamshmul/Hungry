from SQL_DB_Manager import DB_Manager
from tables import Ingredient, Inventory, Items
import json

db_instance = DB_Manager()


def init_ingredients():

    f = open("C:\\Users\Epsilon\Desktop\Hungry\Server\message.json")
    data = json.load(f)
    with next(db_instance.get_db()) as db:
    
        for ing in data['ingredients']:
            ingredient = Ingredient(id=ing['id'], name=ing["name"], unit_size=ing["unit_size"])  
            db_instance.add(db, ingredient)

    f.close()


def test_DB():
    with next(db_instance.get_db()) as db:
        inventory = Inventory(username="yosi123",password="1234", favorites="[]")
        db_instance.add(db, inventory)
        for i in range(1,5):
            inventory_item = Items(Inventory_id=1, Ingredient_id=i, quantity=2)
            db_instance.add(db, inventory_item)

        inv_items = db_instance.get_inventory_items(db, 1)

        inv_item = db_instance.check_if_inventory_has_item(db, 1, "salt")
        if inv_item:
            print(inv_item.ingredient.name, inv_item.quantity*inv_item.ingredient.unit_size, "gr")
        # for item in inv_items:
        #     print({item.ingredient.name})
        #print(f"Added Inventory: ID: {inventory.id}")

        #db_instance.delete_Ingredient(db, 2)




if __name__ == "__main__":
    test_DB()
