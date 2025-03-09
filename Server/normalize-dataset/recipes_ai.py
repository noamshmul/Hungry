from openai import OpenAI
import json

client = OpenAI(
  base_url="https://openrouter.ai/api/v1",
  api_key="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",
)

def clean_ingredients(ingredients):
    prompt = f"""
    You are an AI assistant that helps normalize and standardize ingredient names. Below are ingredient names, each with possible variations such as quantities, units, or misspellings. Your task is to return the normalized version of each ingredient name in lowercase. Please provide the output as a JSON object with the key "name" and the normalized ingredient name as the value.
    
    For example:
    Input: "1 cup of Milk"
    Output: {{"1 cup of Milk": "milk"}}
    
    Input: "2 Bananas"
    Output: {{"2 Bananas": "banana"}}
    
    Now, here are the ingredient names you need to normalize:
    {json.dumps(ingredients)}

    Return the normalized names in the following format without any ```json```, UTF-8 chars or other decorators, just as plain text:
    [
        {{ "original_name": "normalized_ingredient_name" }},
        {{ "another_original_name": "another_normalized_ingredient" }},
        ...
    ]
    """
    print("Sending prompt to R1")
    completion = client.chat.completions.create(
    extra_body={},
    # model="deepseek/deepseek-r1:free",
    model="deepseek/deepseek-chat:free",
    messages=[
        {
        "role": "user",
        "content": prompt
        }
    ]
    )
    print("Got back from R1:" + completion.choices[0].message.content)
    return completion.choices[0].message.content

def get_quanitities(ingredients):
    prompt = f"""
    You are an AI assistant specialized in extracting and approximating ingredient quantities from recipe ingredient strings. Each string may include quantities, units, and ingredient names. Your task is to compute the approximate quantity for each ingredient in grams (for solids) or milliliters (for liquids) using common packaging sizes and rounded, "beautiful" numbers.

    Conversion guidelines:
    - For example, 1 cup of Milk is about 240 ml. However, since milk is typically sold in 500 ml packages, round 240 ml to 250 ml (i.e., half a package).
    - For other ingredients, use your best judgement to convert and then round the number to a "nice" or common packaging value.
    - For instance, if 2 Bananas typically weigh around 200 grams, return 200.

    Examples:
    Input: "1 cup of Milk"
    Output: {{"1 cup of Milk": 250}}

    Input: "2 Bananas"
    Output: {{"2 Bananas": 200}}

    Now, process the following ingredient strings:
    {json.dumps(ingredients)}

    Return the result as a JSON array with each element in the format:
    [
        {{ "original_name": amount in gr/ml }},
        {{ "another_original_name": amount in gr/ml }},
        ...
    ]
    Ensure the output is plain text with no markdown or extra formatting.
    """
    print("Sending prompt to R1")
    completion = client.chat.completions.create(
        extra_body={},
        model="deepseek/deepseek-chat:free",
        messages=[
            {
                "role": "user",
                "content": prompt
            }
        ]
    )
    print("Got back from R1:" + completion.choices[0].message.content)
    return completion.choices[0].message.content

