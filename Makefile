all:
	javac -Djava.ext.dirs=lib/ -d . *.java

clean:
	rm -rfd openisdm data log/*

run:
	java -Djava.ext.dirs=lib/ openisdm.Crawler
