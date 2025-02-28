from SQL_DB_Manager import DB_Manager
from tables import Ingredient, Inventory, Items

db_instance = DB_Manager()


def test_DB():
    with next(db_instance.get_db()) as db:
        ingredient = Ingredient(name="Tomato", unit_size=100)
        #db_instance.add(db, ingredient)
        print(f"Added Ingredient: {db_instance.get_obj_by_id(db, Ingredient, 2).name}, ID: {ingredient.id}")
        db_instance.delete_Ingredient(db, 2)




if __name__ == "__main__":
    test_DB()