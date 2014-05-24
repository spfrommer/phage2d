#!/bin/bash
JSHARED=../lib
NATIVES=$JSHARED/natives
#export CLASSPATH=.:$JSHARED/example.jar:$JSHARED/example2.jar
export CLASSPATH=.:$JSHARED/*
export LD_LIBRARY_PATH=$NATIVES
export DYLD_LIBRARY_PATH=$NATIVES
