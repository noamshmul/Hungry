from fastapi import Depends, HTTPException, status
from fastapi.security import HTTPBasic, HTTPBasicCredentials

from log import logger

import inventory_manager
from SQL_DB_Manager import DB_Manager
from tables import Ingredient, Inventory, Items

from SQL_DB_Manager import db_instance


security = HTTPBasic()


# User Authentication Function
def authentication(creds: HTTPBasicCredentials = Depends(security), db=Depends(db_instance.get_db)):
    username = creds.username
    password = creds.password

    if username and password:
        logger.info("%s has authenticated", username)

        inventory = db_instance.get_inventory_by_username(db,username)
        if inventory and inventory.password == password:
            return inventory.id

    raise HTTPException(
        status_code=status.HTTP_401_UNAUTHORIZED,
        detail="Incorrect id or password",
        headers={"WWW-Authenticate": "Basic"},
    )

