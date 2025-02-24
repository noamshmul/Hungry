class recipe_manager:
    def __init__(self):
        self.recipies = {"sandwich" : [{'name': 'tomato', 'amount':1}, {'name': 'bread', 'amount':1}]}

    def get_recipe_ingredients_by_name(self, name : str):
        if name in self.recipies:
            return self.recipies[name]
        else:
            return [{"ERR" : "No such recipe"}]