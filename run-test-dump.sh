#/bin/bash

mvn clean package exec:exec 

find ./ -name \*.sql -print 
