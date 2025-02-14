import gateway
import log
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

security = HTTPBasic()
app = FastAPI(dependencies=[Depends(security)])

# app.include_router(gateway)

@app.get("/")
def show_readme():
    html_content = readme.get_readme()
    return HTMLResponse(content=html_content, status_code=200)

if __name__ == '__main__':
    print(ascii_art)
    
    logger = log.logging.getLogger(__name__)
    logger.info("Starting Server")

    gateway.run()
