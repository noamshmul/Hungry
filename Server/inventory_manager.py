
ITEMS = [{'name': 'tomato', 'amount': 4}, {'name': 'cucumber', 'amount': 2}, {'name': 'onion', 'amount': 3}]
PASSWORD = "password"
CUSTOM_RECIPES = [{"recipe name": "", "recipe instraction":"", "recipe approx time": "",
                        "recipe ingridients": {}, "recipe size": 1}]
ID = 1

def get_password(inventory_id: int):
    return PASSWORD

def get_inventory(inventory_id: int):
    return ITEMS

def add_item(name, amount, inventory_id: int):
    # On Error TODO
    if name == "error":
        raise ValueError
    
    for d in ITEMS:
        if d["name"] == name:
            d["amount"] += amount

    # if item isn't in the list
    new_item = {"name": name, "amount": amount}
    ITEMS.append(new_item)

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

def add_custom_recipe(name, instractions, approx_time, ingridients, size, inventory_id : int):
    new_recipe = {"recipe name": name, "recipe instraction": instractions, "recipe approx time": approx_time,
                        "recipe ingridients": ingridients, "recipe size": size}
    CUSTOM_RECIPES.append(new_recipe)
