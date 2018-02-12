# TabletScanner

##### TabletScanner is an web application for tablet computer (or any other object) rental management.
 
TabletScanner provides functionality for registering rental/returns, disabling/adding users/devices into the system and to see statistical overview.

## Quickstart
To deploy web application locally on Ubuntu 16.04, use the following commands in the terminal:
* Update: `sudo apt update`
* Install git: `sudo apt install git`
* Install JDK: `sudo apt install default-jdk`
* Install PostgreSQL: `sudo apt install postgresql`
* Clone sources: `git clone https://github.com/PriitPaluoja/TabletScanner.git`
* Navigate to sources: `cd TabletScanner/`
* Run deployment: `./deploy-local.sh`
* Open web browser on http://localhost:8080/ to access rental scan page or http://localhost:8080/login to sign in to admin view (with credentials provided in the previous step)
## Usage
* To insert devices and users, go to http://localhost:8080/insert 
* Each device must have 5 symbol indentificator
* To register rental, go to http://localhost:8080/


Authors: Priit Paluoja, Alari Punning