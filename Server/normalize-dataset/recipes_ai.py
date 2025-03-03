from openai import OpenAI
import json

client = OpenAI(
  base_url="https://openrouter.ai/api/v1",
  api_key="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",
)

def approx_time(name, ingredients):
    prompt = f"""
    You are an AI assistant that helps normalize and standardize ingredient names. Below are ingredient names, each with possible variations such as quantities, units, or misspellings. Your task is to return the normalized version of each ingredient name in lowercase. Please provide the output as a JSON object with the key "name" and the normalized ingredient name as the value.
    
    For example:
    Input: "1 cup of Milk"
    Output: {{"1 cup of Milk": "milk"}}
    
    Input: "2 Bananas"
    Output: {{"2 Bananas": "bananas"}}
    
    Now, here are the ingredient names you need to normalize:
    {json.dumps(ingredients)}

    Return the normalized names in the following format:
    [
        {{ "original_name": "normalized_ingredient_name" }},
        {{ "another_original_name": "another_normalized_ingredient" }},
        ...
    ]
    """
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
    return completion.choices[0].message.content