from fastapi import Depends, APIRouter

from auth import authentication
import inventory_manager
from log import logger

router = APIRouter()

@router.get("/test-connection")
def test_connection(inventory_id = Depends(authentication)):
    '''Used to test if server is alive and if auth is valid'''
    return {"Hello": "World", "inventory_id" :inventory_id}

@router.get("/inventory")
def get_inventory(inventory_id = Depends(authentication)):
    items = inventory_manager.get_inventory(inventory_id)

    return {"items" : items}

@router.post("/inventory")
def add_item(name : str, amount : int, inventory_id = Depends(authentication)):
    inventory_manager.add_item(name, amount, inventory_id)
    return {"status": "ok"}

@router.delete("/inventory")
def remove_item(name : str, amount : int, inventory_id = Depends(authentication)):
    inventory_manager.remove_item(name, amount, inventory_id)
    return {"status": "ok"}
