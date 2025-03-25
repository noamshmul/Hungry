import SQL_DB_Manager

ITEMS = [{'name': 'tomato', 'amount': 4}, {'name': 'cucumber', 'amount': 2}, {'name': 'onion', 'amount': 3}]
PASSWORD = "password"
FAVORITES = []
ID = 1

def get_password(inventory_id: int, db_instance: SQL_DB_Manager.DB_Manager, db_session: SQL_DB_Manager.Session):
    return db_instance.get_password(db_session, inventory_id)

def get_inventory(inventory_id: int, db_instance: SQL_DB_Manager.DB_Manager, db_session: SQL_DB_Manager.Session):
    return db_instance.get_inventory_items(db_session, inventory_id)



def add_item(name, amount, inv_id: int, db_instance: SQL_DB_Manager.DB_Manager, db_session: SQL_DB_Manager.Session):
    # On Error TODO
    if name == "error":
        raise ValueError
    

    Inv_Item = db_instance.check_if_inventory_has_item(db_session, inv_id, name)
    if Inv_Item:
        db_instance.increase_inv_item_amount(db_session, inv_id, Inv_Item.id, amount)
    else:
        # if item isn't in the list
        ing_id = db_instance.get_ingredient_id_by_name(db_session, name)
        new_item = SQL_DB_Manager.Items(Inventory_id=inv_id, Ingredient_id=ing_id, quantity=amount)
        db_instance.add(db_session, new_item)


def remove_item(name, amount, inv_id: int, db_instance: SQL_DB_Manager.DB_Manager, db_session: SQL_DB_Manager.Session):
    Inv_Item = db_instance.check_if_inventory_has_item(db_session, inv_id, name)
    if Inv_Item:
        if Inv_Item.quantity - amount > 0:
            db_instance.decrease_inv_item_amount(db_session, inv_id, Inv_Item.id, amount)
        elif Inv_Item.quantity == amount:
            db_instance.delete_Item(db_session, Inv_Item.id)
    else:
        raise ValueError("invalid argument")


def get_all_favorites(inventory_id: int, db_instance: SQL_DB_Manager.DB_Manager, db_session: SQL_DB_Manager.Session):
    return db_instance.get_favorites(db_session, inventory_id)


def add_favorites(recipe_id, inventory_id: int, db_instance: SQL_DB_Manager.DB_Manager, db_session: SQL_DB_Manager.Session):
    return db_instance.add_favorites(db_session, inventory_id, recipe_id)


def delete_favorites(recipe_id, inventory_id: int, db_instance: SQL_DB_Manager.DB_Manager, db_session: SQL_DB_Manager.Session):
    return db_instance.delete_favorites(db_session, inventory_id, recipe_id)

def remove_ingredients_by_recipe(recipe, inventory_id: int, db_instance: SQL_DB_Manager.DB_Manager, db_session: SQL_DB_Manager.Session):
    ingredients = recipe.get('ingredients')
    for id in ingredients:
        db_instance.decrease_inv_item_amount(db_session, inventory_id, id, ingredients.get(id).get('quantity'))