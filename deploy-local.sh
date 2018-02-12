#!/bin/bash

echo "Set database name for rental service, followed by [ENTER]:"
read db_name

echo "Set name for database user, followed by [ENTER]:"
read db_user

echo "Set password for the database, followed by [ENTER]:"
read db_pass

echo "Set name for the admin account (used to sign in to the UI), followed by [ENTER]:"
read admin_name

echo "Set password for the admin account, followed by [ENTER]:"
read admin_pass

# Set enviromental variables
export TABLET_SCAN_URL=jdbc:postgresql://localhost:5432/$db_name
export TABLET_SCAN_USER=$db_user
export TABLET_SCAN_PASS=$db_pass
export REGULAR_ADMIN_NAME=$admin_name
export REGULAR_ADMIN_PASS=$admin_pass

# Start database cluster
sudo service postgresql start

# Create database for web application
sudo -u postgres createuser $db_user
sudo -u postgres createdb $db_name
sudo -u postgres psql -c "ALTER USER $db_user WITH ENCRYPTED PASSWORD '$db_pass';"
sudo -u postgres psql -c "GRANT ALL PRIVILEGES ON DATABASE $db_name TO $db_user;"

# Set db schema
psql postgresql://$db_user:$db_pass@localhost:5432/$db_name?sslmode=require -a -f db/db.sql

# Run the web application
./gradlew bootRun --stacktrace
