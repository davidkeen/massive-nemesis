#!/bin/bash

if [ -z "$1" ]; then
    echo "Pass the script to be executed as a parameter"
    exit 1
fi

export XDEBUG_CONFIG="idekey=netbeans-xdebug"

/Applications/MAMP/bin/php5/bin/php $1
