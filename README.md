Album Indexer
==========================

This project contains 2 parts:

- Spring boot based backend to index an album release RSS feed (http://newalbumreleases.net/feed/) on a local Elasticsearch node.
- A web project to display a dashboard about album releases

How to start
============

### Prerequisites:

To build this project you'll need following things installed

- maven
- java 8
 
 
Then you can execute:

- mvn clean package
- java -jar album-indexer-0.0.1.jar

It will start the server on port 8080 (make sure this port is available). It will also create a data folder in the
location where you executed the jar, this folder will contain Elasticsearch data.
 


### License
[The MIT License (MIT)](http://opensource.org/licenses/MIT)