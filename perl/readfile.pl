#!/usr/bin/perl

open (MYFILE, $ARGV[0]);

while (<MYFILE>) {

  # Fix DOS newline characters
 	s/\r\n?/\n/g;

 	# Convert some values
 	s/"y"/"n"/g;

 	print "$_";
 }
 close (MYFILE);