# MongoDB Setup Guide
This repository contains all the necessary setup files, settings, and documentation for our MongoDB server.

***

## Prerequisites

* Python version 3.12.7
* Docker Desktop - Download and install from [Docker's official website](https://www.docker.com/products/docker-desktop/), you will need to sign in too.
* Install the pymongo package: `python3 -m pip install pymongo`

## Initial Setup

* Run the `MongoDB_Setup` class to initialize your database 

Once completed, you can start running queries against the MongoDB database

***

## Configuration

All configuration options are available in `settings.py`, including:
* Database name
* Password
* Endpoint
* Other connection parameters

I recommend to know the settings file very well to effectively work with the database.
***

## Important Notes

1. The `data.json` file contains the initial data that will be loaded into the database during first setup
2. You only need to run `MongoDB_Setup` once to initialize the MongoDB database
3. Resource Management:
   * Stop the container when not in use to reduce system resource usage
   * Alternatively, you can delete the container (WARNING: This will delete all database data)
4. Current Status:
   * This setup is currently for the development environment (Will not be in production)

***

## Support
For assistance, please contact Noam.