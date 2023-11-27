#!/bin/bash
x=1
while [ $x -le 5 ]
do
  echo "Running iteration $x"
  mvn clean test
  x=$(( $x + 1 ))
done