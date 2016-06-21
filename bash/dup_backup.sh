#!/bin/bash

########################################################################
# An s3 backup script
# Uses duplicity (http://duplicity.nongnu.org/)
#
# Run this daily and keep 1 month's worth of backups, e.g.
# m h  dom mon dow   command
# 0 4 * * * /root/dup_backup.sh >>/var/log/duplicity/backup.log
########################################################################

# S3 variables
export AWS_ACCESS_KEY_ID=my_access_key
export AWS_SECRET_ACCESS_KEY=my_secret_access_key
export BUCKET=bucket_name
export S3_USE_SIGV4="True"

# GPG
export ENCRYPT_KEY=key_id
export PASSPHRASE=secret_passphrase

# Database credentials
DB_USER='root'
DB_PASS='password'

# Backup these databases
DATABASES=(database1 database2) 

# Working directory (consider the permissions of this directory)
WORKING_DIR=/tmp/bak

########################################################################

# Make the working directory
mkdir $WORKING_DIR

#
# Backup the databases
#
for database in ${DATABASES[@]}; do
  mysqldump -u$DB_USER -p$DB_PASS $database > $WORKING_DIR/$database.sql
done

# Send them to s3
duplicity --full-if-older-than 7D --encrypt-key="$ENCRYPT_KEY" $WORKING_DIR s3://s3-eu-west-1.amazonaws.com/$BUCKET

# Verify
duplicity verify --encrypt-key="$ENCRYPT_KEY" s3://s3-eu-west-1.amazonaws.com/$BUCKET $WORKING_DIR

# Cleanup
duplicity remove-older-than 30D --force --encrypt-key=$ENCRYPT_KEY s3://s3-eu-west-1.amazonaws.com/$BUCKET

# Remove the working directory
rm -rf $WORKING_DIR
