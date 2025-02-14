# Database Settings
MONGODB_HOST = "localhost"
MONGODB_PORT = 27017
MONGODB_USER = "admin"
MONGODB_PASSWORD = "password123"

# Database and Collection Names
DATABASE_NAME = "database"
COLLECTION_NAME = "recipes"
# Database test search name
TEST_RECIPE = "Vegetable Soup"

# Data Path
DATA_FILE = "data.json"

# Connection Settings
CONNECTION = None # Global connection. DO NOT CHANGE!!!
MAX_RETRIES = 30
RETRY_DELAY = 1  # seconds

# Docker Settings
DELETE_CONTAINER = False
DELETE_IMAGE = False # We always use the same image (Recommended: always use False)
DELETE_VOLUME = False
CONTAINER_NAME = "mongo"
DOCKER_VOLUME_NAME = "mongo"