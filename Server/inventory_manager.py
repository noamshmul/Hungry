

class Inventory_Manager:
    def __init__(self):
        self.items = [{'name': 'tomato', 'amount': 4}, {'name': 'cucumber', 'amount': 2}, {'name': 'onion', 'amount': 3}]
        self.password = "password"
        self.recipes = [{"recipe name": "", "recipe instraction":"", "recipe approx time": "",
                         "recipe ingridients": {}, "recipe size": 1}]
        self.id = 1

    def get_password(self, inventory_id: int):
        return self.password

    def get_inventory(self, inventory_id: int):
        return self.items
    
    def add_item(self, name, amount, inventory_id: int):
        for d in self.items:
            if d["name"] == name:
                d["amount"] += amount

        # if item isn't in the list
        new_item = {"name": name, "amount": amount}
        self.items.append(new_item)

    def remove_item(self, name, amount, inventory_id: int):
        for d in self.items:
            if d["name"] == name:
                if d["amount"] - amount > 0:
                    d["amount"] -= amount
                    return

                elif d["amount"] == amount:
                    self.items.remove(d)
                    return

                else:
                    raise ValueError("invalid argument")
                    return

        # if item isn't in the list
        raise ValueError("invalid argument")

    
    def get_all_custom_recipes(self, inventory_id : int):
        return self.recipes
    
    def add_custom_recipe(self, name, instractions, approx_time, ingridients, size, inventory_id : int):
        new_recipe = {"recipe name": name, "recipe instraction": instractions, "recipe approx time": approx_time,
                         "recipe ingridients": ingridients, "recipe size": size}
        self.recipes.append(new_recipe)
