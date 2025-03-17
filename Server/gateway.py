from fastapi import Depends, APIRouter, HTTPException, status
from fastapi.responses import FileResponse
import os

from auth import authentication
from SQL_DB_Manager import DB_Manager
import inventory_manager, recipe_manager
from tables import Ingredient, Inventory, Items
from log import logger

IMAGES_PATH = 'images'

router = APIRouter()
db_instance = DB_Manager()


@router.get("/test-connection")
def test_connection(inventory_id = Depends(authentication)):
    '''Used to test if server is alive and if auth is valid'''
    return {"Hello": "World", "inventory_id": inventory_id}

@router.get("/images/{image_id}.jpg")
def get_image(image_id ,inventory_id = Depends(authentication)):
    path = os.path.join(IMAGES_PATH, image_id + '.jpg')

    if not os.path.exists(path):
        logger.error("File Not Exists")
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Image not found - wrong image_id"
        )
        
    return FileResponse(path,media_type="image/jpeg")

@router.get("/inventory")
def get_inventory(inventory_id = Depends(authentication), db = Depends(db_instance.get_db)):
    items = inventory_manager.get_inventory(inventory_id, db_instance, db)

    return {"status": "ok", "items": items}

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

@router.get("/hungry")
def get_hungry(inventory_id = Depends(authentication), db = Depends(db_instance.get_db)):
    items = inventory_manager.get_inventory(inventory_id, db_instance, db)
    try:
        recipes = recipe_manager.hungry(items)
    except RuntimeError as err:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail=str(err)
        )

    return {"status": "ok", "items": recipes}

@router.post("/signup")
def signup(username: str, password: str, db=Depends(db_instance.get_db)):
    inventory = Inventory(username=username, password=password, custom_recipes="{}")
    db_instance.add(db, inventory)
    id = inventory.id

    return {"status": "ok", "id": id}
