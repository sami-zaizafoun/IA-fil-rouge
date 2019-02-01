#!bin/sh

cd $(dirname $0)/..

sh scripts/compile.sh
[ -d jar ] || mkdir jar

nameArchive="ia_tp_fil_rouge.jar"
jar cf $nameArchive build/ src/ databases/ scripts/
mv $nameArchive jar/
