#!/bin/sh -x
CLASSPATH=../craftbukkit-1.2.3-R0.1.jar javac *.java -Xlint:unchecked -Xlint:deprecation
rm -rf me 
mkdir -p me/exphc/ExphcTweaks
mv *.class me/exphc/ExphcTweaks
jar cf ExphcTweaks.jar me/ *.yml *.java
