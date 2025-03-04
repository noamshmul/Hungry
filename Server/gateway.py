from fastapi import Depends, APIRouter

from auth import authentication
import inventory_manager
from log import logger
import logging

router = APIRouter()

@router.get("/test-connection")
def test_connection(inventory_id = Depends(authentication)):
    '''Used to test if server is alive and if auth is valid'''
    return {"Hello": "World", "inventory_id": inventory_id}

@router.get("/inventory")
def get_inventory(inventory_id=Depends(authentication)):
    logger.info(f"Iventory id: {inventory_id}")
    items = inventory_manager.get_inventory(inventory_id)
    return {"items": items}

@router.post("/inventory")
def add_item(name : str, amount : int, inventory_id = Depends(authentication)):
    inventory_manager.add_item(name, amount, inventory_id)
    return {"status": "ok"}

@router.delete("/inventory")
def remove_item(name : str, amount : int, inventory_id = Depends(authentication)):
    inventory_manager.remove_item(name, amount, inventory_id)
    return {"status": "ok"}

@router.get("/custom-recipes")
def get_custom_recipes(inventory_id = Depends(authentication)):
    custom_recipes = inventory_manager.get_all_custom_recipes(inventory_id)
    return custom_recipes


@router.post("/custom-recipes")
def add_custom_recipes(name : str, instructions : list, approx_time : int, ingredients : list, size : int, inventory_id = Depends(authentication)):
    inventory_manager.add_custom_recipe(name, instructions, approx_time, ingredients, size,inventory_id)
    return {"status": "ok"}


@router.post("/signup")
def signup(inventory_id: str, password: str):
    return {"status": "ok"}
