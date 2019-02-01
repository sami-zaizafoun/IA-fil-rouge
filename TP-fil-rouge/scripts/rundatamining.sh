#!bin/sh

cd $(dirname $0)
sh runClass.sh "datamining.Main" ../databases/$1 $2 $3
