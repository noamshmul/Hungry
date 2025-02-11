import mysql.connector
from mysql.connector import errorcode

TABLES = {'Ingredients_Table': "CREATE TABLE `Ingredients_Table` ("
    "  `id` int(11) NOT NULL AUTO_INCREMENT,"
    "  `Name` varchar(15) NOT NULL,"
    "  `Estimated Expiry` int(11) NOT NULL,"
    "  `Unit Size` varchar(255) NOT NULL,"
    ") ENGINE=InnoDB",
    'Inventories_Table': "CREATE TABLE `Inventories_Table` ("
    "  `id` int(11) NOT NULL AUTO_INCREMENT,"
    "  `items` JSON,"
    "  `password` varchar(255) NOT NULL,"
    "  `custom recipes` JSON NOT NULL,"
    ") ENGINE=InnoDB",
}

config = {"host": "localhost", "user": "admin", "password": "admin"}
DB_NAME = "HungryDB"



class SQL_DB_Manager:
    def __init__(self):
        self.DB = mysql.connector.connect(**config)
        self.DBcursor = self.DB.cursor()
        try:
            self.DBcursor.execute("USE {}".format(DB_NAME))
        except mysql.connector.Error as err:
            if err.errno == errorcode.ER_BAD_DB_ERROR:
                print("DB doesnt exist")
                createDB()
                self.DB.database = DB_NAME
            else:
                print(err.msg) #switch to log
        

    def __del__(self):
        self.DBcursor.close()
        self.DB.close()

    def createDB(self):
        self.DBcursor.execute("CREATE DATABASE HungryDB")
        #call create tables functions

    

    def create_ingredients_table(self):
        table_description = TABLES['Ingredients_Table']
        try:
            #print("Creating table {}: ".format(table_name), end='') to log
            self.DBcursor.execute(table_description)
        except mysql.connector.Error as err:
            print(err.msg) #to log

    def create_inventories_table(self):
        table_description = TABLES['Inventories_Table']
        try:
            #print("Creating table {}: ".format(table_name), end='') to log
            self.DBcursor.execute(table_description)
        except mysql.connector.Error as err:
            print(err.msg) #to log


    def create_Tables(self): #optional
        for table in TABLES:
            table_description = TABLES['Inventories_Table']
            try:
                #print("Creating table {}: ".format(table_name), end='') to log
                self.DBcursor.execute(table_description)
            except mysql.connector.Error as err:
                if err.errno == errorcode.ER_TABLE_EXISTS_ERROR:
                    print("already exists")
                else:
                    print(err.msg) #to log



    def get_ingerdients_by_ids(self, ids ):
        pass

    def get_password(self, inv_id):
        pass
    def get_inventory_items(self, inv_id):
        pass
    def update_inventory_recipes(self, inv_id, recipe):
        pass
    def get_custom_recipes(self, inv_id):
        pass



    



