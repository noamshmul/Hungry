import logging
from datetime import datetime
import os
from json import dump, load


FORMAT = '%(asctime)s - %(name)s - %(levelname)s >\t %(message)s'
DATE_FORMAT = '%Y-%m-%d %I:%M:%S'

def same_run(date1, date2):
    try:
        datetime1 = datetime.strptime(date1, "%Y-%m-%d-%H-%M-%S")
        datetime2 = datetime.strptime(date2, "%Y-%m-%d-%H-%M-%S")
    except ValueError:
        return False
    
    delta = datetime2 - datetime1       # Calculate the time difference (timedelta)
    delta_seconds = abs(delta.total_seconds())
    delta_minutes = delta_seconds / 60  # Convert seconds to minutes
    return delta_minutes < 3

now = datetime.now()
formatted_now = now.strftime("%Y-%m-%d-%H-%M-%S")

LOGS_FOLDER = "..\\logs"
LOGS_META = os.path.join(LOGS_FOLDER,'metadata.json')
LOGS_PATH = os.path.join(LOGS_FOLDER,formatted_now+'.log')

metadata = {'ppid':os.getppid(), 'file':LOGS_PATH, 'date':formatted_now}

if not os.path.exists(LOGS_FOLDER):
    os.makedirs(LOGS_FOLDER)

if os.path.exists(LOGS_META):
    with open(LOGS_META, 'r') as metafile:
        current_metadata = load(metafile)
        if (current_metadata.get('ppid') == metadata.get('ppid') or same_run(current_metadata.get('date'), metadata.get('date'))) and current_metadata.get('file'):
            metadata['file'] = current_metadata['file']
with open(LOGS_META, '+w') as metafile:
    dump(metadata ,metafile)

logger = logging.getLogger('uvicorn.error')
logger_http = logging.getLogger('uvicorn.access')

handler = logging.FileHandler(filename=metadata.get('file'), encoding='utf-8')
handler.setLevel(logging.DEBUG)
handler.setFormatter(logging.Formatter(fmt=FORMAT, datefmt=DATE_FORMAT))

logger.addHandler(handler)
logger_http.addHandler(handler)
