#!/usr/bin/env bash
#$1 times
#$2 version tested
for i in $(seq $1); do
    ./benchmark.sh $2
done