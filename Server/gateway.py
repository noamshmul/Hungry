from fastapi import Depends, APIRouter

from auth import authentication
from SQL_DB_Manager import DB_Manager
import inventory_manager
from log import logger

import Databases.Recipes as DBR

router = APIRouter()
db_instance = DB_Manager()


@router.get("/test-connection")
def test_connection(inventory_id = Depends(authentication)):
    '''Used to test if server is alive and if auth is valid'''
    return {"Hello": "World", "inventory_id": inventory_id}

@router.get("/inventory")
def get_inventory(inventory_id = Depends(authentication), db = Depends(db_instance.get_db)):
    items = inventory_manager.get_inventory(inventory_id, db_instance, db)

    return {"items" : items}

@router.post("/inventory")
def add_item(name : str, amount : int, inventory_id = Depends(authentication), db = Depends(db_instance.get_db)):
    inventory_manager.add_item(name, amount, inventory_id, db_instance, db)
    return {"status": "ok"}

@router.delete("/inventory")
def remove_item(name : str, amount : int, inventory_id = Depends(authentication), db = Depends(db_instance.get_db)):
    inventory_manager.remove_item(name, amount, inventory_id, db_instance, db)
    return {"status": "ok"}

@router.get("/custom-recipes")
def get_custom_recipes(inventory_id = Depends(authentication), db = Depends(db_instance.get_db)):
    custom_recipes = inventory_manager.get_all_custom_recipes(inventory_id, db_instance, db)
    return custom_recipes


@router.post("/custom-recipes")
def add_custom_recipes(name : str, instructions : list, approx_time : int, ingredients : list, size : int, inventory_id = Depends(authentication), db = Depends(db_instance.get_db)):
    inventory_manager.add_custom_recipe(name, instructions, approx_time, ingredients, size, inventory_id, db_instance, db)
    return {"status": "ok"}


@router.post("/signup")
def signup(inventory_id: str, password: str):
    return {"status": "ok"}

@router.get("/recipes")
def get_all_recipes():
    base = DBR.MongoDB_Base
    mdb_functions = DBR.MongoDB_Functions(base)
    recipes = mdb_functions.get_all_recipes()
    return recipes   
