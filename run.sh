#!/bin/bash
cd src
javac -d ../build -cp ../lib/tests.jar modelling/*.java planning/*.java

if [ $? -eq 0 ]; then
    java -cp .:../build/:../lib/tests.jar modelling.Tests
else
    echo "Erreur de compilation."
    exit 1
fi