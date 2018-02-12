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
* Each device identificator must be exactly 5 symbols long.
* To register rental, go to http://localhost:8080/
* For detailed usage (in Estonian), see [PDF](https://github.com/PriitPaluoja/TabletScanner/blob/master/Seadmete_laenutamine_ja_tagastamine.pdf).


![example](https://github.com/PriitPaluoja/TabletScanner/blob/master/usage.jpg)

Authors: Priit Paluoja, Alari Punning