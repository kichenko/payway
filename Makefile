.DEFAULT_GOAL		:= all

TEST_URL			:= http://192.168.1.25:8080/manager/text
STAGING_URL			:= http://test.pwypp.com:7033/manager/text
LIVE_URL			:= http://41.220.10.10:7070/manager/text

ADVERTISING_WAR		:= payway-client-modules/payway-advertising/payway-advertising-webapp/target/payway-advertising-webapp.war
ADVERTISING_PATH	:= /advertising

BUS_TICKETS_WAR		:= payway-client-modules/payway-bus-tickets/payway-bus-tickets-webapp/target/payway-bus-tickets-webapp.war
BUS_TICKETS_PATH	:= /bus-tickets

KIOSK_CASHIER_WAR	:= payway-client-modules/payway-kiosk-cashier/payway-kiosk-cashier-webapp/target/payway-kiosk-cashier-webapp.war
KIOSK_CASHIER_PATH	:= /kiosk-cashier

all:
	mvn -U clean install

update:
	svn update

##### Advertising

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

##### BusTickets

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

##### KioskCashier

kiosk-cashier-deploy-test:
	echo "Deploying: $(KIOSK_CASHIER_PATH) onto $(TEST_URL) ..."
	curl -u tomcat:tomcat --upload-file $(KIOSK_CASHIER_WAR) $(TEST_URL)/deploy?path=$(KIOSK_CASHIER_PATH)

kiosk-cashier-undeploy-test:
	echo "Undeploying: $(KIOSK_CASHIER_PATH) from $(TEST_URL) ..."
	curl -u tomcat:tomcat $(TEST_URL)/undeploy?path=$(KIOSK_CASHIER_PATH)

kiosk-cashier-redeploy-test: kiosk-cashier-undeploy-test kiosk-cashier-deploy-test

kiosk-cashier-deploy-staging:
	echo "Deploying: $(KIOSK_CASHIER_PATH) onto $(STAGING_URL) ..."
	curl -u tomcat:tomcat --upload-file $(KIOSK_CASHIER_WAR) $(STAGING_URL)/deploy?path=$(KIOSK_CASHIER_PATH)

kiosk-cashier-undeploy-staging:
	echo "Undeploying: $(KIOSK_CASHIER_PATH) from $(STAGING_URL) ..."
	curl -u tomcat:tomcat $(STAGING_URL)/undeploy?path=$(KIOSK_CASHIER_PATH)

kiosk-cashier-redeploy-staging: kiosk-cashier-undeploy-staging kiosk-cashier-deploy-staging

kiosk-cashier-deploy-live:
	echo "Deploying: $(KIOSK_CASHIER_PATH) onto $(LIVE_URL) ..."
	curl -u tomcat:tomcat --upload-file $(KIOSK_CASHIER_WAR) $(LIVE_URL)/deploy?path=$(KIOSK_CASHIER_PATH)

kiosk-cashier-undeploy-live:
	echo "Undeploying: $(BUS_TICKETS_PATH) from $(LIVE_URL) ..."
	curl -u tomcat:tomcat $(LIVE_URL)/undeploy?path=$(BUS_TICKETS_PATH)

kiosk-cashier-redeploy-live: kiosk-cashier-undeploy-live kiosk-cashier-deploy-live

## Maven deployment

deploy:
	mvn deploy

rebuild: update all
