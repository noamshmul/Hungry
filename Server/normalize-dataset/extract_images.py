import json

# All we need for this script is that you need to put data.json (of the recipes) inside 'normalized-data' folder

data = {}
with open('data.json', 'r') as file:
    data = json.load(file)

for recipe in data:
    path = recipe.get("image")
    with open('images\\photos\\'+path, 'rb') as original:
        image = original.read()
        with open('images\\images\\'+path, 'wb') as file:
            file.write(image)
