import json
import time
from datetime import timedelta
from recipes_ai import approx_time

def create_record(recipe):
    if len(recipe['prepSteps']) == 1:
        recipe['prepSteps'] = recipe['prepSteps'][0].split()

    ingredients = {}
    for ingredient in recipe['ingredients']:
        ingredient_id = '123456' # TODO: placeholder
        ingredients[ingredient_id] = { "quantity": 500, "original_quantity": ingredient } # TODO: placeholder

    time = approx_time(recipe['hed'],recipe['prepSteps'])
    time = json.loads(time)['time']

    record = {
        "_id": recipe['id'],
        "name": recipe['hed'],
        "instructions": recipe['prepSteps'],
        "approx_time": time, # TODO: placeholder, available in site
        "ingredients": ingredients,
        "size": 4 # TODO: placeholder, available in site
    }
    return record

with open('epicurious-recipes.json', 'r', encoding='utf-8') as f:
    recipes = f.readlines()

parsed_recipes = [json.loads(line) for line in recipes]

recipes = []
for line in parsed_recipes:
    if 'photoData' in line and 'filename' in line['photoData'] and 'placeholders' not in line['photoData']['filename']:
        if 'ingredients' in line and 'prepSteps' in line:
            recipes.append(line)

print(len(recipes))

data = []
skipped = []
for recipe in recipes:
    print("Recipe - "+recipe['id'])
    start_time = time.time()
    try:
        data.append(create_record(recipe))
    except Exception as e:
        skipped.append(recipe['id'])
        print("Got exception - added to skipped")
    print("Ended after ", time.time() - start_time, "seconds")

with open('data.json', 'w', encoding='utf-8') as f:
    json.dump(data, f)

with open('skipped.csv', 'w', encoding='utf-8') as f:
    f.write(','.join(skipped))

