#!/bin/bash

########################################################################
# A b2 backup script
# Uses duplicity (http://duplicity.nongnu.org/)
#
# Run this daily and keep 1 month's worth of backups
########################################################################

# b2 variables
B2_ACCOUNT_ID=account_id
B2_APPLICATION_KEY=application_key
BUCKET=my-bucket-name

# GPG
ENCRYPT_KEY=gpg_key_id
export PASSPHRASE=key_passphrase

# Database credentials
DB_USER='root'
DB_PASS='password'

# Backup these databases
DATABASES=(my_db_1 my_db_2 my_db_3) 

# Working directory
WORKING_DIR=/root/bak

########################################################################

# Make the working directory
mkdir $WORKING_DIR

#
# Dump the databases
#
for database in ${DATABASES[@]}; do
  mysqldump -u$DB_USER -p$DB_PASS $database > $WORKING_DIR/$database.sql
done

# Send them to s3
duplicity --full-if-older-than 7D --encrypt-key="$ENCRYPT_KEY" $WORKING_DIR b2://$B2_ACCOUNT_ID:$B2_APPLICATION_KEY@$BUCKET

# Verify
duplicity verify --encrypt-key="$ENCRYPT_KEY" b2://$B2_ACCOUNT_ID:$B2_APPLICATION_KEY@$BUCKET $WORKING_DIR

# Cleanup
duplicity remove-older-than 30D --force --encrypt-key=$ENCRYPT_KEY b2://$B2_ACCOUNT_ID:$B2_APPLICATION_KEY@$BUCKET

# Remove the working directory
rm -rf $WORKING_DIR
