from fastapi import FastAPI
import logging

logger = logging.getLogger(__name__)

app = FastAPI()

@app.get("/")
def read_root():
    return {"Hello": "World"}

@app.get("/users")
def read_root(id : int | None):
    return {"Hello": "World"}

if __name__ == '__main__':
    import log
    logger.info("Running Gateway Standalone")