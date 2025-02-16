from fastapi import Depends, APIRouter

from auth import authentication
from log import logger

router = APIRouter()

global items #for testing
items = [{'name': 'tomato', 'amount':4}, {'name':'cucumber', 'amount':2}, {'name':'onion', 'amount':3}]

@router.get("/test-connection")
def test_connection(Verifcation = Depends(authentication)):
    '''Used to test if server is alive and if auth is valid'''
    return {"Hello": "World"}

@router.get("/users")
def read_users(id : int = 4):
    return {"Hello": "World"}

@router.post("/inventory")
def add_item(name : str, amount : int):
    # TODO: 

    items.append({'name': name, 'amount': amount})
    return {"status": "ok"}

@router.get("/inventory")
def get_inventory():
    # TODO: 

    return {"status": "ok", "items" : items}