from fastapi import Depends, APIRouter

from auth import authentication
import inventory_manager
from log import logger

router = APIRouter()

@router.get("/test-connection")
def test_connection(inventory_id = Depends(authentication)):
    '''Used to test if server is alive and if auth is valid'''
    return {"Hello": "World", "inventory_id" :inventory_id}

# @router.get("/users")
# def read_users(id : int = 4):
#     return {"Hello": "World"}

@router.post("/inventory")
def add_item(name : str, amount : int):
    inventory_manager.add_item(name, amount)
    return {"status": "ok"}

@router.get("/inventory")
def get_inventory():
    # TODO: 

    return {"status": "ok", "items" : items}