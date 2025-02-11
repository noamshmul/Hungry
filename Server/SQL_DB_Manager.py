import mysql.connector
from mysql.connector import errorcode

TABLES = {'Ingredients_Table': "CREATE TABLE `Ingredients_Table` ("
    "  `id` int(11) NOT NULL AUTO_INCREMENT,"
    "  `Name` varchar(255) NOT NULL,"
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



class SQL_DB_Manager:



    def __init__(self):

        self.DB = mysql.connector.connect(**config)
        self.DBcursor = self.DB.cursor()


    def __del__(self):
        self.DBcursor.close()
        self.DB.close()

    def createDB(self):
        self.DBcursor.execute("CREATE DATABASE HungryDB")

    

    def create_ingredients_table(self):
        table_description = TABLES['Ingredients_Table']
        try:
            #print("Creating table {}: ".format(table_name), end='') to log
            self.DBcursor.execute(table_description)
        except mysql.connector.Error as err:
            if err.errno == errorcode.ER_TABLE_EXISTS_ERROR:
                print("already exists.") #to log
            else:
                print(err.msg) #to log

    def create_inventories_table(self):
        table_description = TABLES['Inventories_Table']
        try:
            #print("Creating table {}: ".format(table_name), end='') to log
            self.DBcursor.execute(table_description)
        except mysql.connector.Error as err:
            if err.errno == errorcode.ER_TABLE_EXISTS_ERROR:
                print("already exists.") #to log
            else:
                print(err.msg) #to log
    

    
        

    

    def response(self):
        return self.request_dic["response"]




dbCursor = 

def ConnectDb()

def CreateDB(DBcursor):

    DBcursor.execute("Create DATA")


