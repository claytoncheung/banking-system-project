FRONTEND_CC = \
	src/frontend/main.cc	\
	src/frontend/FrontEnd/changeplan.cc	\
	src/frontend/FrontEnd/create.cc	\
	src/frontend/FrontEnd/delete.cc	\
	src/frontend/FrontEnd/deposit.cc	\
	src/frontend/FrontEnd/disable.cc	\
	src/frontend/FrontEnd/enable.cc	\
	src/frontend/FrontEnd/helpers.cc	\
	src/frontend/FrontEnd/init.cc	\
	src/frontend/FrontEnd/login.cc	\
	src/frontend/FrontEnd/logout.cc	\
	src/frontend/FrontEnd/paybill.cc	\
	src/frontend/FrontEnd/run.cc	\
	src/frontend/FrontEnd/transfer.cc	\
	src/frontend/FrontEnd/withdrawal.cc

FRONTEND_H = \
	src/frontend/FrontEnd.h	\
	src/frontend/Account.h		\
	src/frontend/constants.h

BACKEND_JAVA = \
	src/backend/Account.java \
	src/backend/Transaction.java \
	src/backend/TransactionHandler.java \
	src/backend/CurrentData.java \
	src/backend/BankAccountWriter.java \
	src/backend/main.java

BACKEND_TEST_JAVA = \
	tests/backend/AccountTest.java \
	tests/backend/BankAccountWriterTest.java \
	tests/backend/MainTest.java \
	tests/backend/TransactionHandlerTest.java \
	tests/backend/TransactionTest.java
	

BACKEND_TEST_CLASSES = \
	tests.backend.AccountTest \
	tests.backend.BankAccountWriterTest \
	tests.backend.MainTest \
	tests.backend.TransactionHandlerTest \
	tests.backend.TransactionTest

HAMCREST_JAR_URL = \
	http://search.maven.org/remotecontent?filepath=org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar

JUNIT_JAR_URL = \
	http://search.maven.org/remotecontent?filepath=junit/junit/4.12/junit-4.12.jar

.PHONY: all clean test test_frontend test_backend

all: bin/frontend/frontend bin/backend/main.class

test: test_frontend test_backend

bin/frontend/frontend: $(FRONTEND_CC) $(FRONTEND_H)
	c++ -Wall -g -std=c++11 $(FRONTEND_CC) -o $@

bin/backend/main.class: $(BACKEND_JAVA)
	javac -d bin -classpath src src/backend/main.java

test_frontend: bin/frontend/frontend
	@bash -c "cd tests/frontend && ./run_frontend_tests.sh"

test_backend: hamcrest-core-1.3.jar junit-4.12.jar bin/backend/main.class
	@javac -classpath ".:bin:junit-4.12.jar:hamcrest-core-1.3.jar" $(BACKEND_TEST_JAVA)
	@java -classpath ".:bin:junit-4.12.jar:hamcrest-core-1.3.jar" org.junit.runner.JUnitCore $(BACKEND_TEST_CLASSES)

hamcrest-core-1.3.jar:
	wget -O $@ $(HAMCREST_JAR_URL)

junit-4.12.jar:
	wget -O $@ $(JUNIT_JAR_URL)

clean:
	rm bin/frontend/frontend
	rm bin/backend/*.class
