# Databases Setup Guide
This repository contains all the necessary setup files, settings, and documentation for our databases.

***

## Prerequisites

* Python version 3.12.7
* Docker Desktop - Download and install from [Docker's official website](https://www.docker.com/products/docker-desktop/), you will need to sign in too.
* Install the pymongo package: `python3 -m pip install pymongo`
* **make sure there is no sql server running on your pc**.

## Initial Setup

* Run the `Startup.sh` file to initialize your database 
* Run the `Insert-data.sh` file to insert data to your database

Once completed, you can start running queries against the databases

* Run the `Cleanup.sh` file to delete all the docker data. (WILL DELETE ANY DATABASE DATA)
***

## Configuration

All configuration options are available in each .sh files at the start.

***

## Important Notes

1. The `data.json` & `data.sql` files contains the initial data that will be loaded into the databases.
2. You only need to run `Startup.sh` once to initialize the databases
3. Resource Management:
   * Stop the docker compose when not in use to reduce system resource usage
   * Alternatively, you can delete the docker compose (WARNING: This will delete all database data)

***

## Support
For assistance, please contact Noam.