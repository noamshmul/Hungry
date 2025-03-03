from SQL_DB_Manager import DB_Manager
from tables import Ingredient, Inventory, Items

db_instance = DB_Manager()


def test_DB():
    with next(db_instance.get_db()) as db:
        # ingredient = Ingredient(name="Tomato", unit_size=100)
        # db_instance.add(db, ingredient)
        # inventory = Inventory(password="1234",custom_recipes="{}")
        # db_instance.add(db, inventory)
        inventory_item = Items(Inventory_id=1, Ingredient_id=1, quantity=2)
        db_instance.add(db, inventory_item)

        inv_item = db_instance.check_if_inventory_has_item(db, 1, "Tomato")
        if inv_item:
            print({inv_item.ingredient.name})
        # for item in inv_items:
        #     print({item.ingredient.name})
        #print(f"Added Inventory: ID: {inventory.id}")

        #db_instance.delete_Ingredient(db, 2)




if __name__ == "__main__":
    test_DB()