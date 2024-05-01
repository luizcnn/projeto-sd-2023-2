#!/bin/bash

port=$1
fruit=$2

for i in $(seq 1 100)
do
    curl localhost:"$port"/fruits/"$fruit"
done