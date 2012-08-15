all:
	javac -Djava.ext.dirs=lib/ -d . *.java

clean:
	#/bin/rm -f openisdm/*.class
	#/bin/rm -rfd data/* log/*
	rm -rfd openisdm data log/*

run:
	java -Djava.ext.dirs=lib/ openisdm.Crawler
