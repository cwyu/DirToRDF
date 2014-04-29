all:
	mkdir -p ./output/classes/
	javac -Djava.ext.dirs=./lib/ -d ./output/classes/ *.java

clean:
	rm -rfd ./output/classes/ ./output/ftp-data/ ./output/rdf/

run:
	java -Djava.ext.dirs=./lib/ -classpath ./output/classes/ openisdm.Main

rdf:
	java -Djava.ext.dirs=./lib/ -classpath ./output/classes/ openisdm.FileTraversal
