#!/bin/sh -x
CLASSPATH=../craftbukkit-1.1-R1-SNAPSHOT.jar javac *.java -Xlint:unchecked -Xlint:deprecation
rm -rf me 
mkdir -p me/exphc/BugTest1
mv *.class me/exphc/BugTest1
jar cf BugTest1.jar me/ *.yml 
cp BugTest1.jar ../plugins/
