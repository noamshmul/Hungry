import Recipes
from log import logger

base = Recipes.MongoDB_Base
func = Recipes.MongoDB_Functions(base)

def get_single_recipe(selected_recipe_name):
    return func.get_recipe_by_name(selected_recipe_name)

def get_all_recipes(fav):
    recipes_list = func.get_all_recipes()
    for recipe in recipes_list:
        if recipe["_id"] in fav:
            recipe["favorite"] = True
        else:
            recipe["favorite"] = False
    return recipes_list

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


def get_favorite_recipes(favorites):
    fav_recipes = func.get_favorite_recipes(favorites)
    for recipe in fav_recipes:
        recipe["favorite"] = True
    if fav_recipes:
        return fav_recipes
    else:
        return []
