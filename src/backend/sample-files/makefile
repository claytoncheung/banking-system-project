JAVAC=javac -d bin -classpath bin

sources = src/Account.java\
 src/Transaction.java\
 src/TransactionHandler.java\
 src/CurrentData.java\
 src/BankAccountWriter.java\
 src/main.java

classes = $(sources:.java=.class)

all: $(classes)

run :
	java -cp ./bin main

clean :
	rm -f bin/*.class
	rm -f src/*.class

%.class : %.java
	$(JAVAC) $<
