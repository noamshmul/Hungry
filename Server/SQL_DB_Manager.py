from log import logger

from sqlalchemy import create_engine, text
from sqlalchemy.orm import sessionmaker, Session, joinedload
from tables import Inventory, Ingredient, Items, Base
import pymysql
import json


config = {"host": "localhost", "user": "root", "password": "root", "port": "3306"}
DB_NAME = "HungryDB"
SQL_DB_URL = f"mysql+pymysql://{config['user']}:{config['password']}@{config['host']}:{config['port']}/{DB_NAME}"

class DB_Manager:
    
    def __init__(self):
        self.create_DB()
        self.engine = create_engine(SQL_DB_URL)
        self.LocalSession = sessionmaker(autocommit=False, autoflush=False, bind=self.engine)
        self.create_Tables()

        self.init_ingredients_table()
        
    def get_db(self):
        db = self.LocalSession()
        try:
            yield db
        finally:
            db.close()

    def add(self, db: Session, obj):
        db.add(obj)
        db.commit()
        db.refresh(obj)       
    
    
    def create_Tables(self): 
        Base.metadata.create_all(bind=self.engine)

    def init_ingredients_table(self):
        db_session = self.LocalSession()
        if db_session.query(Ingredient).first() is None:
            f = open("Databases\\ingredients.json")
            data = json.load(f)
            for ing in data['ingredients']:
                ingredient = Ingredient(id=ing['id'], name=ing["name"], unit_size=ing["unit_size"])  
                self.add(db_session, ingredient)
            f.close()
        db_session.close()

    def create_DB(self):
        temp_url = f"mysql+pymysql://{config['user']}:{config['password']}@{config['host']}:{config['port']}/"
        temp_engine = create_engine(temp_url)
        with temp_engine.connect() as conn:
            conn.execute(text(f"CREATE DATABASE IF NOT EXISTS {DB_NAME};"))
        temp_engine.dispose()

    def get_obj_by_id(self, db: Session, obj, obj_id: int):
        return db.query(obj).filter(obj.id == obj_id).first()

    
    
    def get_ingredient_id_by_name(self, db: Session, ing_name):
        return db.query(Ingredient).filter(Ingredient.name == ing_name).first().id
    



    def increase_inv_item_amount(self, db: Session, inv_id, item_id, amount):
        item = db.query(Items).filter(Items.Inventory_id == inv_id, Items.id == item_id).first()
        if item:
            item.quantity += amount
            db.commit()
            db.refresh(item)
            return item
        return None


    def decrease_inv_item_amount(self, db: Session, inv_id, item_id, amount):
        item = db.query(Items).filter(Items.Inventory_id == inv_id, Items.id == item_id).first()
        if item:
            item.quantity -= amount
            db.commit()
            db.refresh(item)
            return item.quantity
        return 0


    def check_if_inventory_has_item(self, db: Session, inv_id, ing_name):
        item = db.query(Items).join(Ingredient).filter(Items.Inventory_id == inv_id, Ingredient.name == ing_name).first()
        if item:
            return item
        return None


    def delete_Ingredient(self, db: Session, ing_id):
        ingredient = self.get_obj_by_id(db, Ingredient, ing_id)
        db.delete(ingredient)
        db.commit()

    def delete_Item(self, db: Session, item_id):
        item = self.get_obj_by_id(db, Items, item_id)
        db.delete(item)
        db.commit()

    def delete_Inventory(self, db: Session, inv_id):
        Inv = self.get_obj_by_id(db, Inventory, inv_id)
        db.delete(Inv)
        db.commit()
    
    

    def get_password(self, db: Session, inv_id):
        return db.query(Inventory.password).filter(Inventory.id == inv_id).first() 
       
    def get_username(self, db: Session, inv_id):
         return db.query(Inventory.username).filter(Inventory.id == inv_id).first()

    def get_inventory_by_username(self, db: Session, username):
         return db.query(Inventory).filter(Inventory.username == username).first()
    
    def get_inventory_items(self, db: Session, inv_id):
        items = (db.query(Items).filter(Items.Inventory_id == inv_id).options(joinedload(Items.ingredient))).all()
        return [{"id": item.id, "Ingredient_id": item.Ingredient_id, "Inventory_id": item.Inventory_id , "ingredient_name": item.ingredient.name, "quantity": item.quantity} for item in items]
    
    def get_custom_recipes(self, db: Session, inv_id):
        return db.query(Inventory.custom_recipes).filter(Inventory.id == inv_id).all()
    
    def update_inventory_recipes(self, inv_id, recipe):
        pass

    def get_all_ingredients(self, db: Session):
        ingredients = db.query(Ingredient).all()
        return [{"id": ing.id, "name": ing.name, "unit_size": ing.unit_size} for ing in ingredients]

    def get_ingredient_by_name(self, db: Session, name: str):
        ingredient = db.query(Ingredient).filter(Ingredient.name == name).first()
        if ingredient:
            return {"id": ingredient.id, "name": ingredient.name, "unit_size": ingredient.unit_size}
        return None

    def get_ingredient_by_id(self, db: Session, ingredient_id: int):
        ingredient = db.query(Ingredient).filter(Ingredient.id == ingredient_id).first()
        if ingredient:
            return {"id": ingredient.id, "name": ingredient.name, "unit_size": ingredient.unit_size}
        return None

    def add_ingredient(self, db: Session, name: str, unit_size: str):
        # Check if ingredient with same name already exists
        existing = db.query(Ingredient).filter(Ingredient.name == name).first()
        if existing:
            return None
            
        # Get the highest current ID to assign next ID
        max_id = db.query(Ingredient).order_by(Ingredient.id.desc()).first()
        next_id = 1 if not max_id else max_id.id + 1
        
        # Create and add new ingredient
        new_ingredient = Ingredient(id=next_id, name=name, unit_size=unit_size)
        self.add(db, new_ingredient)
        return {"id": new_ingredient.id, "name": new_ingredient.name, "unit_size": new_ingredient.unit_size}


db_instance = DB_Manager()

