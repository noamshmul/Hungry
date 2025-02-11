import socket
import concurrent.futures
import logging

logger = logging.getLogger(__name__)

SERVER_HOST = "127.0.0.1"
SERVER_PORT = 7777

SERVER_MAX_PARALLEL_CONNECTIONS = 5

MAX_WORKERS = 10

def process_request(raw_request_data):
    logger.debug(f"request_data : {raw_request_data}")
    # we need to parse dad 
    pass

def handle_request(client_socket, client_address): # Task for thread pool
    try:
        # Receive request data from client_socket
        raw_request_data = client_socket.recv(1024)
        if not raw_request_data:
            return # Client disconnected prematurely

        response_data = process_request(raw_request_data)

        # Send response back to client
        client_socket.sendall(response_data)

    except Exception as e:
        logger.error(f"Error handling client {client_address}: {e}")
    finally:
        client_socket.close()

def bind_and_listen():
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.bind((SERVER_HOST, SERVER_PORT))
    s.listen(SERVER_MAX_PARALLEL_CONNECTIONS)
    logger.info(f"Listening on {SERVER_HOST}:{SERVER_PORT}")

    executor = concurrent.futures.ThreadPoolExecutor(max_workers=MAX_WORKERS)

    try:
        while True:
            client_socket, client_address = s.accept()
    
            logger.info(f'Connected to {client_address[0]}:{client_address[1]}')
            
            executor.submit(handle_request, client_socket, client_address) # Submit task

    except KeyboardInterrupt as e:
        logger.info("Got an keyboard interrupt- closing the server")
    except Exception as e:
        logger.critical("Closing server with exception:", e)
    finally:
        logger.info("Shutting down executor...")
        executor.shutdown(wait=True) # Wait for all tasks to complete
        logger.info("Gateway server socket closed.")
        s.close()
        logger.info("Gateway shutdown complete.")

if __name__ == '__main__':
    import log
    logger.info("Running Gateway Standalone")