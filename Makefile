.DEFAULT_GOAL := all

all:
	MAVEN_OPTS="-Xmx1024m -XX:MaxPermSize=128m" mvn clean install

update:
	svn update

rebuild: update all
