from fastapi import Depends, HTTPException, status
from fastapi.security import HTTPBasic, HTTPBasicCredentials

from log import logger

security = HTTPBasic()

# User Authentication Function
def authentication(creds: HTTPBasicCredentials = Depends(security)):
    id = creds.username
    password = creds.password

    # TODO: add authentication check
    if id and password: 
        logger.info("%s has authenticated", id)
        return True
    else:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Incorrect id or password",
            headers={"WWW-Authenticate": "Basic"},
        )

