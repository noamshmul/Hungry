import Recipes
from log import logger

base = Recipes.MongoDB_Base
func = Recipes.MongoDB_Functions(base)

def get_single_recipe(selected_recipe_name):
    return func.get_recipe_by_name(selected_recipe_name)

def get_all_recipes():
    return func.get_all_recipes()

def hungry(items):
    '''# the big algorithm'''
    existing_items = {str(item["Ingredient_id"]) : item["quantity"] for item in items}

    recipes = []
    i = 0
    while len(recipes) < 3:
        if i > 3:
            raise RuntimeError("Not enough items in your inventory")

        recipes_less_accurate = func.get_closest_recipe_with_ids(existing_items, i)
        if recipes_less_accurate:
            for r in recipes_less_accurate:
                if r not in recipes:
                    recipes.append(r)
        i += 1
    
    recipes = recipes[:3]
    return recipes

