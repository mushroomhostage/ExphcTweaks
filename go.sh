#!/bin/sh -x
CLASSPATH=../craftbukkit-1.1-R4.jar:../plugins/RadioBeacon.jar javac *.java -Xlint:unchecked -Xlint:deprecation
rm -rf me 
mkdir -p me/exphc/ExphcTweaks
mv *.class me/exphc/ExphcTweaks
jar cf ExphcTweaks.jar me/ *.yml *.java
