.DEFAULT_GOAL := all
STAGING_URL := http://192.168.1.25:8080/manager/text

all:
	MAVEN_OPTS="-Xmx1024m -XX:MaxPermSize=128m" mvn clean install

update:
	svn update

deploy-staging:
	find -name '*.war' | while read l; do \
		echo "Deploying: $$l onto $(STAGING_URL) ..."; \
		f=$$(basename -s .war $$l); \
		curl -u tomcat:tomcat --upload-file $$l $(STAGING_URL)/deploy?path=/$$f; \
	done;

rebuild: update all
