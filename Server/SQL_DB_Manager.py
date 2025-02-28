from sqlalchemy import create_engine, text
from sqlalchemy.orm import sessionmaker, Session
from tables import Inventory, Ingredient, Items, Base
import pymysql


config = {"host": "localhost", "user": "root", "password": "admin", "port": "2025"}
DB_NAME = "HungryDB"
SQL_DB_URL = f"mysql+pymysql://{config['user']}:{config['password']}@{config['host']}:{config['port']}/{DB_NAME}"


class DB_Manager:
    
    def __init__(self):
        self.create_DB()
        self.engine = create_engine(SQL_DB_URL)
        self.LocalSession = sessionmaker(autocommit=False, autoflush=False, bind=self.engine)
        self.create_Tables()
        
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

    def create_DB(self):
        temp_url = f"mysql+pymysql://{config['user']}:{config['password']}@{config['host']}:{config['port']}/"
        temp_engine = create_engine(temp_url)
        with temp_engine.connect() as conn:
            conn.execute(text(f"CREATE DATABASE IF NOT EXISTS {DB_NAME};"))
        temp_engine.dispose()

    def get_obj_by_id(self, db: Session, obj, obj_id: int):
        return db.query(obj).filter(obj.id == obj_id).first()
    
  

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
    
    def get_inventory_items(self, db: Session, inv_id):
        return db.query(Items).filter(Items.Inventory_id == inv_id).all()
    
    def get_custom_recipes(self, db: Session, inv_id):
        return db.query(Inventory.custom_recipes).filter(Inventory.id == inv_id).all()
    
    def update_inventory_recipes(self, inv_id, recipe):
        pass




    



