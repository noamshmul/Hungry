from sqlalchemy import Column, Integer, String, Float, JSON, ForeignKey
from sqlalchemy.orm import relationship
from sqlalchemy.ext.declarative import declarative_base

Base = declarative_base()



class Ingredient(Base):
    __tablename__ = "ingredients"

    id = Column(Integer, primary_key=True, index=True)
    name = Column(String(100), nullable=False, unique=True)
    unit_size = Column(Float, nullable=False)

    items = relationship("Items", back_populates="ingredient")

class Inventory(Base):
    __tablename__ = "inventories"

    id = Column(Integer, primary_key=True, index=True)
    username = Column(String(100), nullable=False, unique=True)
    password = Column(String(100), nullable=False)
    custom_recipes = Column(JSON, nullable=False)
    items = relationship("Items", back_populates="inventory_owner", cascade="all, delete")

class Items(Base):
    __tablename__ = "items"
    id = Column(Integer, primary_key=True, index=True)
    Inventory_id = Column(Integer, ForeignKey("inventories.id", ondelete="CASCADE"))
    Ingredient_id = Column(Integer, ForeignKey("ingredients.id", ondelete="CASCADE"))
    quantity = Column(Integer, nullable=False)

    inventory_owner = relationship("Inventory", back_populates="items")
    ingredient = relationship("Ingredient", back_populates="items")
