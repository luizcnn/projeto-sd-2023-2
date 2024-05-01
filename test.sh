#!/bin/bash

port=$1

while true;
do
    curl localhost:"$port"/fruits
done