#!/usr/bin/perl

# (I didn't write this. I think I found it on the MySQL forums.)
# Use it like this:
# mysqldump --no-data database > schema.sql
# perl rowsize.pl schema.sql > out.txt

use Data::Dumper;
use strict;
$| = 1;

my %DataType =
("TINYINT"=>1, "SMALLINT"=>2, "MEDIUMINT"=>3,
"INT"=>4, "BIGINT"=>8,
"FLOAT"=>'if ($M <= 24) {return 4;} else {return 8;}',
"DOUBLE"=>8,
"DECIMAL"=>'if ($M < $D) {return $D + 2;} elsif ($D > 0) {return $M + 2;} else {return $M + 1;}',
"NUMERIC"=>'if ($M < $D) {return $D + 2;} elsif ($D > 0) {return $M + 2;} else {return $M + 1;}',
"DATE"=>3, "DATETIME"=>8, "TIMESTAMP"=>4, "TIME"=>3, "YEAR"=>1,
"CHAR"=>'$M', "VARCHAR"=>'$M+1',
"ENUM"=>1,
"TINYBLOB"=>'$M+1', "TINYTEXT"=>'$M+1',
"BLOB"=>'$M+2', "TEXT"=>'$M+2',
"MEDIUMBLOB"=>'$M+3', "MEDIUMTEXT"=>'$M+3',
"LONGBLOB"=>'$M+4', "LONGTEXT"=>'$M+4');

my ($D, $M, $dt);

my $fieldCount = 0;
my $byteCount = 0;
my $fieldName;

open (TABLEFILE,"< $ARGV[0]");

LOGPARSE:while (<TABLEFILE>) {
chomp;
if ( $_ =~ s/create table[ ]`*([a-zA-Z_]*).*`/$1/i ) {
print "Fieldcount: $fieldCount Bytecount: $byteCount\n" if $fieldCount;
$fieldCount = 0;
$byteCount = 0;
print "\nTable: $_\n";
next;
}
next if $_ !~ s/(.*)[ ]+(TINYINT[ ]*\(*[0-9,]*\)*|SMALLINT[ ]*\(*[0-9,]*\)*|MEDIUMINT[ ]*\(*[0-9,]*\)*|INT[ ]*\(*[0-9,]*\)*|BIGINT[ ]*\(*[0-9,]*\)*|FLOAT[ ]*\(*[0-9,]*\)*|DOUBLE[ ]*\(*[0-9,]*\)*|DECIMAL[ ]*\(*[0-9,]*\)*|NUMERIC[ ]*\(*[0-9,]*\)*|DATE[ ]*\(*[0-9,]*\)*|DATETIME[ ]*\(*[0-9,]*\)*|TIMESTAMP[ ]*\(*[0-9,]*\)*|TIME[ ]*\(*[0-9,]*\)*|YEAR[ ]*\(*[0-9,]*\)*|CHAR[ ]*\(*[0-9,]*\)*|VARCHAR[ ]*\(*[0-9,]*\)*|TINYBLOB[ ]*\(*[0-9,]*\)*|TINYTEXT[ ]*\(*[0-9,]*\)*|ENUM[ ]*\(*['A-Za-z_,]*\)*|BLOB[ ]*\(*[0-9,]*\)*|TEXT[ ]*\(*[0-9,]*\)*|MEDIUMBLOB[ ]*\(*[0-9,]*\)*|MEDIUMTEXT[ ]*\(*[0-9,]*\)*|LONGBLOB[ ]*\(*[0-9,]*\)*|LONGTEXT[ ]*\(*[0-9,]*\)*).*/$2/gix;
$fieldName=$1;
$_=uc;
$D=0;
($D = $_) =~ s/.*\,([0-9]+).*/$1/g if ( $_ =~ m/\,/ );
$_ =~ s/\,([0-9]*)//g if ( $_ =~ m/\,/ );
($M = $_) =~ s/[^0-9]//g;
$M=0 if ! $M;
($dt = $_) =~ s/\(.*\)//g;
$dt =~ s/[^A-Za-z_]*//g;
print "$fieldName $_:\t".eval($DataType{"$dt"})." bytes\n";
++$fieldCount;
$byteCount += eval($DataType{"$dt"});
}
print "Fieldcount: $fieldCount Bytecount: $byteCount\n";