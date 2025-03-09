from fastapi import FastAPI, Depends
from fastapi.responses import HTMLResponse

from log import logger
from gateway import router
import auth
import readme

ascii_art = '''
 /$$   /$$                                                  
| $$  | $$                                                  
| $$  | $$ /$$   /$$ /$$$$$$$   /$$$$$$   /$$$$$$  /$$   /$$
| $$$$$$$$| $$  | $$| $$__  $$ /$$__  $$ /$$__  $$| $$  | $$
| $$__  $$| $$  | $$| $$  \ $$| $$  \ $$| $$  \__/| $$  | $$
| $$  | $$| $$  | $$| $$  | $$| $$  | $$| $$      | $$  | $$
| $$  | $$|  $$$$$$/| $$  | $$|  $$$$$$$| $$      |  $$$$$$$
|__/  |__/ \______/ |__/  |__/ \____  $$|__/       \____  $$
                               /$$  \ $$           /$$  | $$
                              |  $$$$$$/          |  $$$$$$/
                               \______/            \______/ 
'''

app = FastAPI(dependencies=[Depends(auth.security)])

app.include_router(router)


@app.get("/")
def show_readme():
    html_content = readme.get_readme()
    return HTMLResponse(content=html_content, status_code=200)


if __name__ == '__main__':
    print(ascii_art)
    
    logger.info("Starting Server")
