.DEFAULT_GOAL := all
TEST_URL := http://192.168.1.25:8080/manager/text
LIVE_URL := http://5.9.112.207:7070/manager/text

all:
	MAVEN_OPTS="-Xmx1024m -XX:MaxPermSize=128m" mvn clean install

update:
	svn update

deploy-test:
	find -name '*.war' | grep 'advertising' | while read l; do \
		f=$$(basename -s .war $$l); \
		echo "Deploying: $$f onto $(TEST_URL) ..."; \
		curl --progress-bar -u tomcat:tomcat --upload-file $$l $(TEST_URL)/deploy?path=/$$f; \
	done;

undeploy-test:
	find -name '*.war' | grep 'advertising' | while read l; do \
		f=$$(basename -s .war $$l); \
		echo "Undeploying: $$f from $(TEST_URL) ..."; \
		curl --progress-bar -u tomcat:tomcat $(TEST_URL)/undeploy?path=/$$f; \
	done;

deploy-live:
	find -name '*.war' | grep 'advertising' | while read l; do \
		f=$$(basename -s .war $$l); \
		echo "Deploying: $$f onto $(LIVE_URL) ..."; \
		curl -u tomcat:MT2UAtfv --upload-file $$l $(LIVE_URL)/deploy?path=/$$f; \
	done;

undeploy-live:
	find -name '*.war' | grep 'advertising' | while read l; do \
		f=$$(basename -s .war $$l); \
		echo "Undeploying: $$f from $(LIVE_URL) ..."; \
		curl -u tomcat:MT2UAtfv $(LIVE_URL)/undeploy?path=/$$f; \
	done;

deploy:
	mvn deploy

rebuild: update all

