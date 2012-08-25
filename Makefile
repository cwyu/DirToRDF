all:
	javac -Djava.ext.dirs=lib/ -d . *.java
	#/System/Library/Frameworks/JavaVM.framework/Versions/1.6/Commands/javac -Djava.ext.dirs=lib/ -d . *.java

clean:
	rm -rfd openisdm data log/*

run:
	java -Djava.ext.dirs=lib/ openisdm.Crawler
	#/System/Library/Frameworks/JavaVM.framework/Versions/1.6/Commands/java -Djava.ext.dirs=lib/ jena/examples/rdf/Tutorial09
	#/System/Library/Frameworks/JavaVM.framework/Versions/1.6/Commands/java -Djava.ext.dirs=lib/ arq/examples/ExProg1
