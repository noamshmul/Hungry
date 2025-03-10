
recipes = [{"sandwich" : [{'name': 'tomato', 'amount':1}, {'name': 'bread', 'amount':1}]}]

def get_recipe_ingredients_by_name(name : str):
    
    if name in recipes:
        return recipes[name]
    else:
        return [{"ERR" : "No such recipe"}]
        
def hungry():
    # the big algorithm
    pass