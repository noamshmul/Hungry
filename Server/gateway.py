import socket
import logging

logger = logging.getLogger(__name__)

SERVER_HOST = "127.0.0.1"
SERVER_PORT = 7777

def bind_and_listen():
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.bind((SERVER_HOST, SERVER_PORT))
    s.listen(5)
    logger.info(f"Listening on {SERVER_HOST}:{SERVER_PORT}")

    try:
        while True:
            conn, addr = s.accept()
    
            logger.info('Connected to :', addr[0], ':', addr[1])
    
            # Start a new thread and return its identifier

    except KeyboardInterrupt as e:
        logger.info("Got an keyboard interrupt- closing the server")
    except Exception as e:
        logger.critical("Closing server with exception:", e)
    s.close()


if __name__ == '__main__':
    import log
    logger.info("Running Gateway Standalone")