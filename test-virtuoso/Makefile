all:
	javac -Djava.ext.dirs=./lib/ -d . Virtuoso.java
	#javac -Djava.ext.dirs=./lib/ -Djdbc.drivers=./lib/virtuoso.jdbc4.Driver -d . Virtuoso.java

clean:
	rm *.class

run:
	#java -Djava.ext.dirs=./lib/ -Djdbc.drivers=./lib/virtuoso.jdbc4.Driver. -classpath ./ Virtuoso
	java -Djava.ext.dirs=./lib/ -Djdbc.drivers=virtuoso.jdbc4.Driver -classpath . Virtuoso
	#java -Djava.ext.dirs=./lib/ -Djdbc.drivers=./lib/virtuoso.jdbc4.Driver -classpath ./ Virtuoso

#all:
#    javac -Djava.ext.dirs=./lib/ -d . Virtuoso.java
#    #javac -Djava.ext.dirs=./lib/ -Djdbc.drivers=./lib/virtuoso.jdbc.Driver -d . Virtuoso.java

#clean:
#    rm *.class

#run:
#    java -Djava.ext.dirs=./lib/ -Djdbc.drivers=./lib/virtuoso.jdbc.Driver. -classpath ./ Virtuoso
#    #java -Djava.ext.dirs=./lib/ -Djdbc.drivers=./lib/virtuoso.jdbc.Driver -classpath ./ Virtuoso
