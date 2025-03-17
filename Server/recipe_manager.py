import Recipes
from log import logger

base = Recipes.MongoDB_Base
func = Recipes.MongoDB_Functions(base)
        
def hungry(items):
    '''# the big algorithm'''
    existing_items = {str(item["Ingredient_id"]) : item["quantity"] for item in items}
    existing_items_ids = [str(item["Ingredient_id"]) for item in items]

    recipes = []
    i = 0
    while len(recipes) < 3:
        if i > 3:
            raise RuntimeError("Not enough items in your inventory")

        recipes_less_accurate = func.get_closest_recipe_with_ids(existing_items_ids, i)
        for r in recipes_less_accurate:
            if r not in recipes:
                recipes.append(r)
        i += 1
    
    recipes = recipes[:3]
    return recipes

