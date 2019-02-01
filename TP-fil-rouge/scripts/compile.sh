#!bin/sh

cd $(dirname $0)/..
[ -d build ] || mkdir build/ build/datamining/
javac -d build src/*/*.java
