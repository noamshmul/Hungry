import Recipes
import random
from log import logger

base = Recipes.MongoDB_Base
func = Recipes.MongoDB_Functions(base)

def get_single_recipe_name(selected_recipe_name):
    return func.get_recipe_by_name(selected_recipe_name)

def get_all_recipes(fav):
    recipes_list = func.get_all_recipes()
    for recipe in recipes_list:
        if recipe["_id"] in fav:
            recipe["favorite"] = True
        else:
            recipe["favorite"] = False
    return recipes_list

def get_single_recipe_id(selected_recipe_id):
    return func.get_recipe_by_id(selected_recipe_id)

def hungry(items, favorites):
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

    for recipe in recipes:
        recipe["favorite"] = recipe["_id"] in favorites

    # Filtering recipes that are marked as favorite
    favorite_recipes = [recipe for recipe in recipes if recipe["favorite"]]

    # If there are more than 3 favorite recipes, return them
    if len(favorite_recipes) < 3:
        non_favorite_recipes = [recipe for recipe in recipes if not recipe["favorite"]]
        random.shuffle(non_favorite_recipes)  # Shuffle to get random recipes
        favorite_recipes.extend(non_favorite_recipes[:3 - len(favorite_recipes)])
    else:
        random.shuffle(favorite_recipes)  # Shuffle to get random recipes
        favorite_recipes = favorite_recipes[:3]

    recipes = favorite_recipes

    return recipes


def get_favorite_recipes(favorites):
    fav_recipes = func.get_favorite_recipes(favorites)
    for recipe in fav_recipes:
        recipe["favorite"] = True
    if fav_recipes:
        return fav_recipes
    else:
        return []
