import SQL_DB_Manager



ITEMS = [{'name': 'tomato', 'amount': 4}, {'name': 'cucumber', 'amount': 2}, {'name': 'onion', 'amount': 3}]
PASSWORD = "password"
CUSTOM_RECIPES = [{"recipe name": "", "recipe instraction":"", "recipe approx time": "",
                        "recipe ingridients": {}, "recipe size": 1}]
ID = 1

def get_password(inventory_id: int, db_instance: SQL_DB_Manager.DB_Manager, db_session: SQL_DB_Manager.Session):
    return db_instance.get_password(db_session, inventory_id)

def get_inventory(inventory_id: int, db_instance: SQL_DB_Manager.DB_Manager, db_session: SQL_DB_Manager.Session):
    return db_instance.get_inventory_items(db_session, inventory_id)

def add_item(name, amount, inv_id: int, db_instance: SQL_DB_Manager.DB_Manager, db_session: SQL_DB_Manager.Session):
    # On Error TODO
    if name == "error":
        raise ValueError
    
    for d in ITEMS:
        if d["name"] == name:
            d["amount"] += amount

    # if item isn't in the list
    ing_id = db_instance.get_ingredient_id_by_name(db_session, name)
    new_item = SQL_DB_Manager.Items(Inventory_id=inv_id, ingredient_id=ing_id)
    db_instance.add(db_session, new_item)

def remove_item(name, amount, inventory_id: int):
    for d in ITEMS:
        if d["name"] == name:
            if d["amount"] - amount > 0:
                d["amount"] -= amount
                return

            elif d["amount"] == amount:
                ITEMS.remove(d)
                return

            else:
                raise ValueError("invalid argument")

    # if item isn't in the list
    raise ValueError("invalid argument")


def get_all_custom_recipes(inventory_id : int):
    return CUSTOM_RECIPES

def add_custom_recipe(name, instructions, approx_time, ingredients, size, inventory_id : int):
    new_recipe = {"recipe name": name, "recipe instraction": instructions, "recipe approx time": approx_time,
                        "recipe ingredients": ingredients, "recipe size": size}
    CUSTOM_RECIPES.append(new_recipe)
