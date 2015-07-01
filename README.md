Advertising WebApp Tutorial
====================

This tutorial is how to install & run Advertising WebApp on Tomcat.

Running on Tomcat
-------------------
-Tomcat version 7.0.51 or high (websocket support)
-For xuggler lib support copy to tomcat\lib jar's
  -xuggle-xuggler-5.3.jar (or high) 
  -slf4j-api-1.x.x.jar  
-Custom setting file:///${avs.conf:/etc/avs.conf}

WebDav support
-------------------
For build with WEB DAV support use Apache commons VFS2 2.1-SNAPSHOT from repository apache-vfs2
See https://issues.apache.org/jira/browse/VFS-467
See https://issues.apache.org/jira/browse/VFS-428

Also need for web dav server
BrowserMatch "^Jakarta-Commons-VFS" redirect-carefully
BrowserMatch "^Jakarta Commons-HttpClient/3.0" redirect-carefully
BrowserMatch "^Jakarta Commons-HttpClient/3.1" redirect-carefully

Thanks.