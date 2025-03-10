import json, requests
import time
from datetime import timedelta
from recipes_ai import clean_ingredients, get_quanitities
from thefuzz import fuzz, process
from bs4 import BeautifulSoup
#
from collections import Counter
import re

def find_top_100_words(string_list):
    """
    Finds the top 100 most recurring words in a list of strings.

    Args:
        string_list: A list of strings.

    Returns:
        A list of tuples, where each tuple contains a word and its frequency.
    """
    all_text = ' '.join(string_list)
    words = re.findall(r'\b\w+\b', all_text.lower()) #Find all word characters, and make lowercase.
    word_counts = Counter(words)
    top_words = word_counts.most_common(100)
    return top_words
#
def extract_yield(url):
    """
    Extracts InfoSliceKey and InfoSliceValue from an HTML string.

    Args:
        html_string: The HTML string containing InfoSliceItem div.

    Returns:
        A dictionary containing the InfoSliceKey and InfoSliceValue, or an empty dictionary if not found.
    """
    html = requests.get('https://www.epicurious.com' + url)

    soup = BeautifulSoup(html.text, 'html.parser')
    info_slice_items = soup.find_all('div', class_='InfoSliceItem-nijBm')

    result_dict = {}
    if info_slice_items:
        for item in info_slice_items:
            key_element = item.find('p', class_='InfoSliceKey-gHIvng')
            value_element = item.find('p', class_='InfoSliceValue-tfmqg')

            if key_element and value_element:
                key = key_element.text.strip()
                value = value_element.text.strip()
                result_dict[key] = value

    return result_dict


def get_normalized_ingredients():
    names = []
    quantities = []
    for i in range(0, 10000, 100):
        try:
            with open(f'cleaned_ingredients_temp_{i}_{i+100}.json', 'r', encoding='utf-8') as f:
                names_temp = json.load(f)
            names.extend(names_temp)
        except Exception:
            pass
        try:
            with open(f'quantities{i}_{i+100}.json', 'r', encoding='utf-8') as f:
                quantities_temp = json.load(f)
            quantities.extend(quantities_temp)
        except Exception:
            pass

    merged_names = {}
    for dictionary in names:
        if dictionary != {'': ''}:
            merged_names.update(dictionary)

    merged_quantities = {}
    for dictionary in quantities:
        if dictionary != {'': ''}:
            merged_quantities.update(dictionary)
    
    return merged_names, merged_quantities

def parse_ingredients(recipes):
    ingredients = []
    for recipe in recipes:
        ingredients.extend(recipe['ingredients'])
    return ingredients

def create_record(recipe, quantities, ids):
    if len(recipe['prepSteps']) == 1:
        if '\n' not in recipe['prepSteps'][0]:
                recipe['prepSteps'] = recipe['prepSteps'][0].split('. ')
        else:
            recipe['prepSteps'] = recipe['prepSteps'][0].split('\n') # split by newline if there is a newline.

    ingredients = {}
    for ingredient in recipe['ingredients']:
        ingredient_id = ids[ingredient]
        ingredients[ingredient_id] = { "quantity": quantities[ingredient], "original_quantity": ingredient }

    recipe_info = extract_yield(recipe['url'])
    record = {
        "_id": recipe['id'],
        "name": recipe['hed'],
        "instructions": recipe['prepSteps'],
        "approx_time": recipe_info.get('Total Time'),
        "ingredients": ingredients,
        "size": recipe_info.get('Yield'), 
        "image" : recipe['photoData']['filename']
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

def handle_ingredients(ingredients):
    cleaned_ingredients = []
    for i in range(9900, 10000, 100):
        cleaned_ingredients_temp = clean_ingredients(ingredients[i:i+100])
        with open(f'cleaned_ingredients_temp_{i}_{i+100}.json', 'w', encoding='utf-8') as tempfile:
            tempfile.write(cleaned_ingredients_temp)
        try: 
            cleaned_ingredients.extend(json.loads(cleaned_ingredients_temp))
        except Exception as e:
            print(e)
            continue

    # Convert the list of dictionaries into a single dictionary
    merged_dict = {k: v for d in cleaned_ingredients for k, v in d.items()}
    with open('cleaned_ingredients11.json', 'w', encoding='utf-8') as f:
        json.dump(merged_dict, f)

def handle_ingredients_quantities(ingredients):
    quantities = []
    for i in range(8500, 8700, 100):
        cleaned_ingredients_temp = get_quanitities(ingredients[i:i+100])
        with open(f'quantities{i}_{i+100}.json', 'w', encoding='utf-8') as tempfile:
            tempfile.write(cleaned_ingredients_temp)
        try: 
            quantities.extend(json.loads(cleaned_ingredients_temp))
        except Exception as e:
            print(e)
            continue

    # Convert the list of dictionaries into a single dictionary
    merged_dict = {k: v for d in quantities for k, v in d.items()}
    with open('quantities.json', 'w', encoding='utf-8') as f:
        json.dump(merged_dict, f)

def get_ingredients_from_db():
    '''Creates an ingredient_mapping based on the SQL DB Table `Ingredients`.
    {
        "milk": 1,
        "bananas": 2,
        "salt": 3,
        # etc.
    }
    '''

    return {
        "olive oil": 1, "salt": 2, "pepper": 3, "garlic": 4, "onion": 5, "tomato": 6, "potato": 7, "carrot": 8, "chicken": 9, "beef": 10,
        "egg": 11, "milk": 12, "butter": 13, "flour": 14, "sugar": 15, "rice": 16, "pasta": 17, "bread": 18, "lemon": 19, "lime": 20,
        "soy sauce": 21, "honey": 22, "mustard": 23, "ketchup": 24, "vinegar": 25, "parsley": 26, "basil": 27, "oregano": 28, "thyme": 29, "rosemary": 30,
        "cumin": 31, "paprika": 32, "chili powder": 33, "cinnamon": 34, "nutmeg": 35, "ginger": 36, "cilantro": 37, "bay leaf": 38, "cheddar cheese": 39, "parmesan cheese": 40,
        "yogurt": 41, "apple": 42, "banana": 43, "berry": 44, "orange": 45, "broth": 46, "corn": 47, "bean": 48, "lentil": 49, "yeast": 50,
        "coconut oil": 51, "avocado": 52, "spinach": 53, "broccoli": 54, "cauliflower": 55, "zucchini": 56, "bell pepper": 57, "mushroom": 58, "asparagus": 59, "sweet potato": 60,
        "salmon": 61, "tuna": 62, "shrimp": 63, "pork": 64, "lamb": 65, "turkey": 66, "almond": 67, "walnut": 68, "pecan": 69, "cashew": 70,
        "peanut butter": 71, "oat": 72, "quinoa": 73, "couscous": 74, "maple syrup": 75, "vanilla extract": 76, "baking powder": 77, "baking soda": 78, "cocoa powder": 79, "chocolate": 80,
        "coconut milk": 81, "cream": 82, "sour cream": 83, "ricotta cheese": 84, "mozzarella cheese": 85, "feta cheese": 86, "goat cheese": 87, "jalapeno": 88, "habanero": 89, "scallion": 90,
        "leek": 91, "celery": 92, "radish": 93, "cucumber": 94, "artichoke": 96, "kale": 97, "collard green": 98, "bok choy": 99, "turnip": 100,
        "beet": 101, "pea": 102, "green bean": 103, "mango": 104, "pineapple": 105, "grape": 106, "peach": 107, "plum": 108, "cherry": 109, "watermelon": 110,
        "kiwi": 111, "pomegranate": 112, "date": 113, "fig": 114, "prune": 115, "raisin": 116, "sesame seed": 117, "chia seed": 118, "flax seed": 119, "sunflower seed": 120,
        "apple cider vinegar": 121, "red wine vinegar": 122, "balsamic vinegar": 123, "fish sauce": 124, "worcestershire sauce": 125, "hot sauce": 126, "sesame oil": 127, "peanut oil": 128, "canola oil": 129, "grapeseed oil": 130,
        "cornstarch": 131, "arrowroot powder": 132, "gelatin": 133, "pectin": 134, "agar agar": 135, "dried cranberry": 136, "dried apricot": 137, "crystallized ginger": 138, "miso paste": 139, "tahini": 140,
        "caper": 141, "olive": 142, "sun-dried tomato": 143, "kimchi": 144, "sauerkraut": 145, "pickle": 146, "jalapeno pepper (pickled)": 147, "shallot": 148, "horseradish": 149, "wasabi": 150,
        "anise": 151, "cardamom": 152, "clove": 153, "coriander": 154, "fennel": 155, "saffron": 156, "star anise": 157, "tarragon": 158, "turmeric": 159, "poppy seed": 160,
        "lavender": 161, "orange zest": 162, "lemon zest": 163, "lime zest": 164, "coconut flake": 165, "molasses": 166, "condensed milk": 167, "marshmallow": 168, "graham cracker": 169, "ladyfinger": 170,
        "puff pastry": 171, "phyllo dough": 172, "pizza dough": 173, "tortilla": 174, "wrap": 175, "ramen noodle": 176, "udon noodle": 177, "soba noodle": 178, "rice noodle": 179, "spaghetti squash": 180,
        "heart of palm": 181, "edamame": 182, "water chestnut": 183, "bamboo shoot": 184, "snow pea": 185, "radicchio": 186, "endive": 187, "arugula": 188, "microgreen": 189, "sprout": 190,
        "seaweed": 191, "wakame": 192, "nori": 193, "dulse": 194, "kelp": 195, "smoked paprika": 196, "garam masala": 197, "curry powder": 198, "five-spice powder": 199, "onion powder": 200,
        "prosciutto": 201, "ricotta": 202, "arborio rice": 203, "polenta": 204, "semolina": 205, "farro": 206, "bulgur": 207, "couscous": 208, "panko": 209, "masa": 210,
        "tapioca": 211, "xanthan gum": 212, "guar gum": 213, "gelatin sheet": 214, "liquid smoke": 215, "anchovy": 216, "capocollo": 217, "mortadella": 218, "sardine": 219, "caviar": 220,
        "lime juice": 221, "lemon juice": 222, "orange juice": 223, "apple juice": 224, "cranberry juice": 225, "grape juice": 226, "peach juice": 227,
        "pear juice": 228, "apricot juice": 229, "pomegranate juice": 230, "coconut water": 231, "almond milk": 232, "oat milk": 233, "soy milk": 234, "cashew milk": 235, "rice milk": 236,
        "hazelnut": 237, "macadamia nut": 238, "pistachio": 239, "brazil nut": 240, "pine nut": 241, "date syrup": 242, "agave nectar": 243, "brown sugar": 244, "powdered sugar": 245, "turbinado sugar": 246,
        "maple sugar": 247, "demerara sugar": 248, "malt extract": 249, "barley malt": 250
    }

def match_ingredients_to_ids(ingredients, normalized_names, ingredient_mapping):
    matched_ingredients = {}
    for ingredient in ingredients:
        if ingredient in matched_ingredients.keys() or ingredient not in normalized_names.keys():
            continue
        
        # get some id by thefuzz with ingredient_mapping.keys()
        normalized_name = normalized_names[ingredient]
        if normalized_name == None:
            continue
        ingredient_db_name, score = process.extractOne(normalized_name, ingredient_mapping.keys())

        if score > 85:
            matched_ingredients[ingredient] = ingredient_mapping[ingredient_db_name]
    return matched_ingredients

def handle_ingredients_ids(ingredients, normalized_names):
    ingredient_mapping = get_ingredients_from_db()
    matched_ingredients = match_ingredients_to_ids(ingredients, normalized_names, ingredient_mapping)
    return matched_ingredients


all_ingredients = parse_ingredients(recipes)
# test = find_top_100_words(all_ingredients)
# handle_ingredients(all_ingredients)
# handle_ingredients_quantities(all_ingredients)

normalized_ingredients = get_normalized_ingredients()

normalized_ids = handle_ingredients_ids(all_ingredients, normalized_ingredients[0])

data = []
skipped = []
for recipe in recipes:
    print("Recipe - "+recipe['id'])
    start_time = time.time()
    try:
        data.append(create_record(recipe,normalized_ingredients[1],normalized_ids))
    except Exception as e:
        skipped.append(recipe['id'])
        print("Got exception - added to skipped")
    print("Ended after ", time.time() - start_time, "seconds")

with open('data_10.json', 'w', encoding='utf-8') as f:
    json.dump(data, f)

with open('skipped.csv', 'w', encoding='utf-8') as f:
    f.write(','.join(skipped))
