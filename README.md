# gateway.client.java.transport 

# About this Project

This project abstracts the transport layer for Kaazing WebSocket Java Client library. The jar from this project is shaded along with other jars to create a single consolidated jar in [gateway.client.java](https://github.com/kaazing/gateway.client.java).

# Building this Project

## Minimum requirements for building the project
* Java Developer Kit (JDK) or Java Runtime Environment (JRE) Java 7 (version 1.7.0_21) or higher
* Maven 3.0.5

## Steps for building this project
0. Clone the repo: ```git clone https://github.com/kaazing/gateway.client.java.transport.git```
0. Go to the cloned directory: ```cd gateway.client.java.transport```
0. Build the project: ```mvn clean install```

# Integrate this project

0. Integrate this component in gateway.client.java and gateway.client.java.internal by updating the version in gateway.client.java and gateway.client.java.internal pom files.
0. Build the gateway.client.java and use it for application development
