import logging
from datetime import datetime
import os

now = datetime.now()
formatted_now = now.strftime("%Y-%m-%d-%H-%M-%S")

SERVER_FOLDER = "Server"
LOGS_FOLDER = os.path.join(SERVER_FOLDER,"logs")
LOGS_PATH = os.path.join(LOGS_FOLDER,formatted_now+'.log')

if not os.path.exists(LOGS_FOLDER):
    os.makedirs(LOGS_FOLDER)

logging.basicConfig(filename=LOGS_PATH, encoding='utf-8', level=logging.DEBUG, format='%(asctime)s - %(name)s - %(levelname)s >\t %(message)s', datefmt='%Y-%m-%d %I:%M:%S')
logging.getLogger().addHandler(logging.StreamHandler())

