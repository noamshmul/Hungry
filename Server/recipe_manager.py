import Recipes

base = Recipes.MongoDB_Base
func = Recipes.MongoDB_Functions(base)
        
def hungry():
    # the big algorithm
    
    #placeholder
    recipes = [func.get_recipe_by_id("54a40a326529d92b2c003c00"), func.get_recipe_by_id("54a40a1f19925f464b37396a") , func.get_recipe_by_id("54a40a1c19925f464b373961")]
    return recipes