all:
	mvn package
run:
	mvn exec:java -Dexec.mainClass="com.geekcap.informit.App"
