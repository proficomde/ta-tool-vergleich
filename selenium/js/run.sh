#!/bin/bash
x=1
while [ $x -le 5 ]
do
  echo "Running iteration $x"
  npx mocha test_file_xpath.js
  x=$(( $x + 1 ))
done