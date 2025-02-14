import uvicorn
from fastapi import FastAPI, Depends, HTTPException, status
from fastapi.security import HTTPBasic, HTTPBasicCredentials

import logging

logger = logging.getLogger(__name__)

security = HTTPBasic()
app = FastAPI(dependencies=[Depends(security)])

# User Authentication Function
def verification(creds: HTTPBasicCredentials = Depends(security)):
    id = creds.username
    password = creds.password

    # TODO: add authentication check
    if id and password: 
        print("User Validated")
        return True
    else:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Incorrect id or password",
            headers={"WWW-Authenticate": "Basic"},
        )

global items #for testing
items = [{'name': 'tomato', 'amount':4}, {'name':'cucumber', 'amount':2}, {'name':'onion', 'amount':3}]

@app.get("/")
def read_root():
    return {"Hello": "World"}

@app.get("/test-connection")
def test_connection(Verifcation = Depends(verification)):
    '''Used to test if server is alive and if auth is valid'''
    return {"Hello": "World"}

@app.get("/users")
def read_users(id : int = 4):
    return {"Hello": "World"}

@app.post("/inventory")
def add_item(name : str, amount : int):
    # TODO: 

    items.append({'name': name, 'amount': amount})
    return {"status": "ok"}

@app.get("/inventory")
def get_inventory():
    # TODO: 

    return {"status": "ok", "items" : items}

def run():
    uvicorn.run(app, host="0.0.0.0", port=8000)

if __name__ == '__main__':
    import log
    logger.info("Running Gateway Standalone")
    run()