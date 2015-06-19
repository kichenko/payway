.DEFAULT_GOAL		:= all

TEST_URL			:= http://192.168.1.25:8080/manager/text
STAGING_URL			:= http://192.168.1.33:7070/manager/text
LIVE_URL			:= http://5.9.112.207:7070/manager/text

ADVERTISING_WAR		:= payway-client-modules/payway-advertising/payway-advertising-webapp/target/payway-advertising-webapp.war
ADVERTISING_PATH	:= /advertising

BUS_TICKETS_WAR		:= payway-client-modules/payway-bus-tickets/payway-bus-tickets-webapp/target/payway-bus-tickets-webapp.war
BUS_TICKETS_PATH	:= /bus-tickets

all:
	MAVEN_OPTS="-Xmx1024m -XX:MaxPermSize=128m" mvn clean install

update:
	svn update

advertising-deploy-test:
	echo "Deploying: $(ADVERTISING_PATH) onto $(TEST_URL) ..."
	curl -u tomcat:tomcat --upload-file $(ADVERTISING_WAR) $(TEST_URL)/deploy?path=$(ADVERTISING_PATH)

advertising-undeploy-test:
	echo "Undeploying: $(ADVERTISING_PATH) from $(TEST_URL) ..."
	curl -u tomcat:tomcat $(TEST_URL)/undeploy?path=$(ADVERTISING_PATH)

advertising-redeploy-test: advertising-undeploy-test advertising-deploy-test

advertising-deploy-staging:
	echo "Deploying: $(ADVERTISING_PATH) onto $(STAGING_URL) ..."
	curl -u tomcat:tomcat --upload-file $(ADVERTISING_WAR) $(STAGING_URL)/deploy?path=$(ADVERTISING_PATH)

advertising-undeploy-staging:
	echo "Undeploying: $(ADVERTISING_PATH) from $(STAGING_URL) ..."
	curl -u tomcat:tomcat $(STAGING_URL)/undeploy?path=$(ADVERTISING_PATH)

advertising-redeploy-staging: advertising-undeploy-staging advertising-deploy-staging

advertising-deploy-live:
	echo "Deploying: $(ADVERTISING_PATH) onto $(LIVE_URL) ..."
	curl -u tomcat:tomcat --upload-file $(ADVERTISING_WAR) $(LIVE_URL)/deploy?path=$(ADVERTISING_PATH)

advertising-undeploy-live:
	echo "Undeploying: $(ADVERTISING_PATH) from $(LIVE_URL) ..."
	curl -u tomcat:tomcat $(LIVE_URL)/undeploy?path=$(ADVERTISING_PATH)

advertising-redeploy-live: advertising-undeploy-live advertising-deploy-live

bus-tickets-deploy-test:
	echo "Deploying: $(BUS_TICKETS_PATH) onto $(TEST_URL) ..."
	curl -u tomcat:tomcat --upload-file $(BUS_TICKETS_WAR) $(TEST_URL)/deploy?path=$(BUS_TICKETS_PATH)

bus-tickets-undeploy-test:
	echo "Undeploying: $(BUS_TICKETS_PATH) from $(TEST_URL) ..."
	curl -u tomcat:tomcat $(TEST_URL)/undeploy?path=$(BUS_TICKETS_PATH)

bus-tickets-redeploy-test: bus-tickets-undeploy-test bus-tickets-deploy-test

bus-tickets-deploy-staging:
	echo "Deploying: $(BUS_TICKETS_PATH) onto $(STAGING_URL) ..."
	curl -u tomcat:tomcat --upload-file $(BUS_TICKETS_WAR) $(STAGING_URL)/deploy?path=$(BUS_TICKETS_PATH)

bus-tickets-undeploy-staging:
	echo "Undeploying: $(BUS_TICKETS_PATH) from $(STAGING_URL) ..."
	curl -u tomcat:tomcat $(STAGING_URL)/undeploy?path=$(BUS_TICKETS_PATH)

bus-tickets-redeploy-staging: bus-tickets-undeploy-staging bus-tickets-deploy-staging

bus-tickets-deploy-live:
	echo "Deploying: $(BUS_TICKETS_PATH) onto $(LIVE_URL) ..."
	curl -u tomcat:tomcat --upload-file $(BUS_TICKETS_WAR) $(LIVE_URL)/deploy?path=$(BUS_TICKETS_PATH)

bus-tickets-undeploy-live:
	echo "Undeploying: $(BUS_TICKETS_PATH) from $(LIVE_URL) ..."
	curl -u tomcat:tomcat $(LIVE_URL)/undeploy?path=$(BUS_TICKETS_PATH)

bus-tickets-redeploy-live: bus-tickets-undeploy-live bus-tickets-deploy-live

deploy:
	mvn deploy

rebuild: update all
