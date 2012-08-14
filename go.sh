#!/bin/sh -x
CLASSPATH=../craftbukkit-1.2.5-R1.0.jar:../SERVER-alpha-mcpc125/mods/thermalexpansion-1.0.4.1_bc2-mcpc-r1.zip:../SERVER-alpha-mcpc125/mods/industrialcraft2-1.97-mcpc1.2.5-r9.zip:../SERVER-alpha-mcpc125/mods/zforestry-1.4.8.6-mcpc1.2.5.zip javac *.java -Xlint:unchecked -Xlint:deprecation
rm -rf me 
mkdir -p me/exphc/ExphcTweaks
mv *.class me/exphc/ExphcTweaks
jar cf ExphcTweaks.jar me/ *.yml *.java
