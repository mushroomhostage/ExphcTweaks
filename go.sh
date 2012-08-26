#!/bin/sh -x
CLASSPATH=../craftbukkit-1.2.5-R4.1-MCPC-SNAPSHOT-173.jar:../SERVER-alpha-mcpc125/mods/thermalexpansion-1.0.4.3_bc2-mcpc-r1.zip:../SERVER-alpha-mcpc125/mods/industrialcraft2-1.103-mcpc1.2.5-r1+nospam.zip:../SERVER-alpha-mcpc125/mods/zforestry-1.4.8.6-mcpc1.2.5-r4.zip javac *.java -Xlint:unchecked -Xlint:deprecation
rm -rf me 
mkdir -p me/exphc/ExphcTweaks
mv *.class me/exphc/ExphcTweaks
jar cf ExphcTweaks.jar me/ *.yml *.java
