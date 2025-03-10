import Recipes
from log import logger

base = Recipes.MongoDB_Base
func = Recipes.MongoDB_Functions(base)
        
def hungry(items):
    # the big algorithm
    #logger.info("Inventory items - " +str(items))
    existing_items = {str(item["Ingredient_id"]) : item["quantity"] for item in items}
    #logger.info("Inventory items - " +str(existing_items))

    ids = ["54a40a326529d92b2c003c00","54a40a1f19925f464b37396a","54a40a1c19925f464b373961"]
    for recipe_id in ids:
        recipe = func.get_recipe_by_id(recipe_id)
        ingredients= recipe.get('ingredients')

        #logger.info("Ingredients items - " + str(type(ingredients)))
        for ingredient_id,quantity_dict in ingredients.items():
            if str(ingredient_id) in existing_items.keys():
                logger.info("Match " + ingredient_id)
                if quantity_dict['quantity'] <= existing_items[str(ingredient_id)]:
                    logger.info("In the recipe: %s which is %s | In the DB %s", quantity_dict['original_quantity'], quantity_dict['quantity'], existing_items[str(ingredient_id)])
                    logger.info("In the limit!!! " + ingredient_id)

    #placeholder
    recipes = [func.get_recipe_by_id("54a40a326529d92b2c003c00"), func.get_recipe_by_id("54a40a1f19925f464b37396a") , func.get_recipe_by_id("54a40a1c19925f464b373961")]
    return recipes

