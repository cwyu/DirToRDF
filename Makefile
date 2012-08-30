all:
	mkdir -p ./output/classes/
	javac -Djava.ext.dirs=./lib/ -d ./output/classes/ *.java

clean:
	rm -rfd ./output/classes/ ./output/ftp-data/

run:
	java -Djava.ext.dirs=./lib/ -classpath ./output/classes/ openisdm.Main
	#java -Djava.ext.dirs=./lib/ -classpath ./output/classes/ openisdm.Crawler
