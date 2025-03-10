# Hungry!
We are hungry for the _win_ :D

## Server
Hungry's server is built on python 3.12.7, using FastAPI as the API gateway, MongoDB as the database of the recipes and MySQL for Inventory and Ingredients.

### Setup
1. Open your terminal.
2. Clone the repo.
3. Navigate to `Server` folder.
4. Create virtual environment: `python -m venv venv`
5. Enable the virtual environment: `.\venv\Scripts\activate`
6. Download all python libraries: `pip install -r requirements.txt`

### Running the Databases
1. Launch Docker.
2. Open your terminal.
3. Navigate to `Server\Databases` folder.
4. Run `.\Startup.sh`

Now, switch back to the docker window or run `docker ps` in the terminal.
If you don't see any running containers, please contact `@noamshmul`.

5. Run `.\Insert-data.sh`

You should have a MongoDB running with data inside of it that was loaded from `data.json` and an empty SQL database. On the first startup of the server the SQL database will fill up with data.

### Running the Server
**Note: You must [run the DB](#running-the-databases) first.**

1. Open your terminal.
2. Navigate to `Server` folder.
3. Enable the virtual environment: `.\venv\Scripts\activate` (if not enabled yet)
4. Run `fastapi dev`

If you see: `[FastAPI] - Starting development server 🚀` that's great! You're running the server!

If you encounter any issues with the setup/running the server, please contact `@SavvaS412`.

### Stopping the Server
All you have to do to stop the fastapi gateway is press `CTRL + C`, once or twice, until you see `Stopping FastAPI Service`.

In order to stop the Databases, simply go to open Docker Desktop app and stop all running containers under the `Server` section. After that you can turn off Docker Engine and quit Docker.

To run the databases again, just turn on Docker and start the containers. There's **no** need in running the startup script again.

### Wiping the Databases
If you wish to delete the Databases and all its data:
1. Open your terminal.
2. Navigate to `Server\Databases` folder.
3. Run `.\Cleanup.sh`

This script should remove all of your containers and volumes.

**Note: It won't delete your images. You will have to do that manually if you wish to.**
