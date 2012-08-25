all:
	mkdir -p ./output/classes/
	javac -Djava.ext.dirs=./lib/ -d ./output/classes/ *.java
	#/System/Library/Frameworks/JavaVM.framework/Versions/1.6/Commands/javac -Djava.ext.dirs=lib/ -d . *.java

clean:
	rm -rfd ./output/classes/ ./output/ftp-data/

run:
	java -Djava.ext.dirs=./lib/ -classpath ./output/classes/ openisdm.Crawler
	#/System/Library/Frameworks/JavaVM.framework/Versions/1.6/Commands/java -Djava.ext.dirs=lib/ jena/examples/rdf/Tutorial09
	#/System/Library/Frameworks/JavaVM.framework/Versions/1.6/Commands/java -Djava.ext.dirs=lib/ arq/examples/ExProg1
