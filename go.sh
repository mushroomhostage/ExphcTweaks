#!/bin/sh -x
CLASSPATH=../craftbukkit-1.2.5-R1.0.jar javac *.java -Xlint:unchecked -Xlint:deprecation
rm -rf me 
mkdir -p me/exphc/ExphcTweaks
mv *.class me/exphc/ExphcTweaks
jar cf ExphcTweaks.jar me/ *.yml *.java
