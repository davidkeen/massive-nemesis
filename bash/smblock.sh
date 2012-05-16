#!/bin/bash
# smblock.sh -- shows which users are locking a file
#
# Copyright (c) 2005, David Keen <david@sharedmemory.net>
#
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA


usage="Usage: $(basename $0) file

  --help    display this help and exit
  --version output version information and exit"
version="1"
fail=0

case $# in
    0)
	echo "$usage" || fail=1; exit $fail;;
    1)
	case "z${1}" in
	    z--help )
		echo "$usage" || fail=1; exit $fail;;
	    z--version )
		echo -e "$(basename $0)\nVersion: $version" || fail=1; exit $fail;;
	    * );;
	esac
	;;
    * );;
esac

processes=$(smbstatus --locks | grep -i $1 | cut -d " " -f 1 | sort -u)

if [ -z $processes ]; then
    echo "Nobody locking a file called $1"
else
    ps -fp $processes
fi
exit $fail
