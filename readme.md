# Hungry's Server
We are **HUNGRY** for the WIN!!!!!

This project runs Hungry’s backend using **Python 3.12.7** and **FastAPI** as the API gateway. It uses **MongoDB** to store recipes and **MySQL** for inventory and ingredients.

## Prerequisites

- **Python 3.12.7** installed
- **Git** for cloning the repository
- **Docker Desktop** for the databases

---

## Setup

1. **Clone the Repository:**
   ```bash
   git clone <repository-url>
   ```

2. **Navigate to the Server Folder:**
   ```bash
   cd Server
   ```

3. **Create and Activate the Virtual Environment:**
   - Create:
     ```bash
     python -m venv venv
     ```
   - Activate:
     - **Windows:**
       ```bash
       .\venv\Scripts\activate
       ```
     - **macOS/Linux:**
       ```bash
       source venv/bin/activate
       ```

4. **Install Dependencies:**
   ```bash
   pip install -r requirements.txt
   ```

---

## Running the Databases

> **Note:** Make sure Docker Desktop is running before you start.

### First Time Setup
1. **Open a Terminal and Navigate to the Database Folder:**
   ```bash
   cd Server/Databases
   ```

2. **Download & Start the Database Containers:**
   ```bash
   ./Startup.sh
   ```
   - Verify by checking Docker Desktop or running:
     ```bash
     docker ps
     ```
   - If no containers appear, please contact `@noamshmul`.

3. **Insert Initial Data:**
   ```bash
   ./Insert-data.sh
   ```
   This loads data into MongoDB from `data.json` and prepares an empty MySQL database (which will auto-populate when the server starts).

### Subsequent Runs
After the initial setup, simply turn on Docker Desktop and start the containers listed under the **Server** section. There is **no need** to run the startup or insert data scripts again.

---

## Running the Server

> **Important:** The databases must be running before starting the server.

1. **Ensure Your Virtual Environment is Active:**
   ```bash
   .\venv\Scripts\activate
   ```
   *(Repeat the activation step if needed.)*

2. **Launch the FastAPI Server:**
   ```bash
   fastapi dev
   ```
   If you see `[FastAPI] - Starting development server 🚀`, the server is up and running.

3. **Need Assistance?**  
   Contact `@SavvaS412` for any setup or runtime issues.

---

## Stopping the Server

- **Stop the FastAPI Server:**  
  Press `CTRL + C` in your terminal until you see the message `Stopping FastAPI Service`.

- **Stop the Databases:**  
  Open Docker Desktop, then stop the containers listed under the "Server" section. You can then shut down Docker Engine if desired.

---

## Wiping the Databases

To completely remove the databases and their data:

1. **Navigate to the Database Folder:**
   ```bash
   cd Server/Databases
   ```

2. **Run the Cleanup Script:**
   ```bash
   ./Cleanup.sh
   ```
   *This script removes all containers and volumes. Docker images are not deleted automatically and must be removed manually if desired.*
