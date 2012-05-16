#!/bin/sh

########################################################################
# An s3 backup script
#
# Run this weekly and keep 1 month's worth of backups (4)
########################################################################

# System Binaries
DATE=/bin/date
MYSQLDUMP=/usr/bin/mysqldump
RM=/bin/rm
TAR=/bin/tar
SVN_HOTBACKUP=/usr/share/doc/subversion-1.6.6/tools/backup/hot-backup.py
TRAC_ADMIN='/usr/bin/trac-admin'

# S3 variables
export AWS_ACCESS_KEY_ID=9999999999
export AWS_SECRET_ACCESS_KEY=0000000000
export SSL_CERT_FILE='/etc/pki/tls/cert.pem'
S3SYNCDIR='/usr/local/s3sync'
RUBY="/usr/bin/ruby -I $S3SYNCDIR"
S3CMD="$S3SYNCDIR/s3cmd.rb --ssl"
BUCKET='my_bucket_name'
PREFIX='backup'

DB_USER='root'
DB_PASS='password'

# Backup these databases
DATABASES=(database1 database2) 

# Backup these svn repos
REPOS=(repo1 repo2)

# Backup Storage Locations
#BACKUP=/root/backup/data

# Working directory
WORKING_DIR=/tmp/david_bak

# tar's Backup Options
OPTIONS=cjpf

# Get the current date
date=`$DATE '+%Y%m%d'`

# We keep 1 month's worth of backups
LAST_MONTH=$($DATE -d '1 month ago' +%Y%m%d)

########################################################################

# Make the working directory
mkdir $WORKING_DIR

#
# Backup config files
#
CONFIG_FILES='/home/david/s3backup.sh /etc/httpd/conf.d/vhosts.conf /etc/httpd/conf.d/ssl.conf'

# Tar them up
$TAR $OPTIONS $WORKING_DIR/$date-conf.tar.bz2 $CONFIG_FILES 1>/dev/null 2>&1

# Send them to s3
$RUBY $S3CMD put $BUCKET:$PREFIX/$date-conf.tar.bz2 $WORKING_DIR/$date-conf.tar.bz2

#
# Backup the databases
#
for database in ${DATABASES[@]}; do
  $MYSQLDUMP -u$DB_USER -p$DB_PASS $database > $WORKING_DIR/$date-$database.sql
done

# Tar them up
$TAR $OPTIONS $WORKING_DIR/$date-db.tar.bz2 $WORKING_DIR/*.sql 1>/dev/null 2>&1

# Send them to s3
$RUBY $S3CMD put $BUCKET:$PREFIX/$date-db.tar.bz2 $WORKING_DIR/$date-db.tar.bz2

#
# Backup blog files (use --dereference so we get symlinks)
#
$TAR $OPTIONS $WORKING_DIR/$date-blog.tar.bz2 --dereference /home/david/public_html/www/example.com/blog 1>/dev/null 2>&1

# Send it to s3
$RUBY $S3CMD put $BUCKET:$PREFIX/$date-blog.tar.bz2 $WORKING_DIR/$date-blog.tar.bz2

#
# Backup subversion
#
mkdir $WORKING_DIR/svn
for repo in ${REPOS[@]}; do
    $SVN_HOTBACKUP /home/david/public_html/svn/$repo $WORKING_DIR/svn
done
$TAR $OPTIONS $WORKING_DIR/$date-svn.tar.bz2 $WORKING_DIR/svn 1>/dev/null 2>&1

# Send it to s3
$RUBY $S3CMD put $BUCKET:$PREFIX/$date-svn.tar.bz2 $WORKING_DIR/$date-svn.tar.bz2

rm -rf $WORKING_DIR/svn

#
# Backup trac
#
mkdir $WORKING_DIR/trac
$TRAC_ADMIN /home/david/public_html/trac/project hotcopy $WORKING_DIR/trac/project
$TAR $OPTIONS $WORKING_DIR/$date-trac.tar.bz2 $WORKING_DIR/trac 1>/dev/null 2>&1

# Send it to s3
$RUBY $S3CMD put $BUCKET:$PREFIX/$date-trac.tar.bz2 $WORKING_DIR/$date-trac.tar.bz2

rm -rf $WORKING_DIR/trac

#############################
# CLEANUP
#

# Get a list of files in the bucket, strip the prefix and compare their date string
files=$($RUBY $S3CMD list $BUCKET:$PREFIX | sed -e "s/$PREFIX\///g" | awk --posix -v last_month=$LAST_MONTH '
    BEGIN { FS = "-" }
    $1 ~ /^[[:digit:]]{8}/ { if ($1 < last_month) print $0 }
')

for key in $files; do
    $RUBY $S3CMD delete $BUCKET:$PREFIX/$key
done

# Remove the working directory
rm -rf $WORKING_PATH


# Remove the working directory
rm -rf $WORKING_DIR
