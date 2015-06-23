# java.client

# About This Project

The java.client project implements the [WebSocket standard](https://tools.ietf.org/html/rfc6455) in Java. It provides a WebSocket client API that enables developers to build Java applications that communicate over WebSocket with an RFC-6455 endpoint, such as KAAZING Gateway.

Developers can include the Java WebSocket Client using the following maven dependency to develop and run web applications:

```xml
<dependency>
    <groupId>org.kaazing</groupId>
    <artifactId>gateway.client.java</artifactId>
    <version>[5.1.0.0,5.2.0.0)</version>
</dependency>

```

# Building This Project

## Minimum Requirements for Building the Projects in This Repo

* Java SE Development Kit (JDK) 7 or higher
* maven 3.0.5 or higher

## Steps for Building this Project

0. Clone the repo: ```git clone https://github.com/kaazing/java.client.git```
0. Go to the cloned directory: ```cd java.client```
0. Build the project: ```mvn clean install```

# Running the WebSocket demo

## From the command-line
0. Change directory: ```cd ws/demo/target```
0. Run the demo application: ```java -cp . -jar gateway.client.java.demo-develop-SNAPSHOT.jar```

## From within Eclipse
0. Import the project in Eclipse
0. Under `gateway.client.java.demo` project, right-click on `WebSocketFrame.java` or `WebSocketApplet.java` in
src/main/java/org.kaazing.net.ws.demo and run!

# Using KAAZING Gateway or any RFC-6455 Endpoint

You can use an RFC-6455 endpoint, such as KAAZING Gateway, to connect to a back-end service. To learn how to administer the Gateway, its configuration files, and security, see the documentation on [developer.kaazing.com](http://developer.kaazing.com/documentation/5.0/index.html).

# Learning How to Develop Client Applications

To learn how to develop client applications with this project, see the documentation on [developer.kaazing.com](http://developer.kaazing.com/documentation/5.0/index.html).

# View a Running Demo

To view demos of clients built with this project, see [kaazing.org](http://kaazing.org/)
