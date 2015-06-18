# java.client

[![Build Status][build-status-image]][build-status]

[build-status-image]: https://travis-ci.org/kaazing/java.client.svg?branch=develop
[build-status]: https://travis-ci.org/kaazing/java.client

# About This Project

This project contains public Java [WebSocket] (https://tools.ietf.org/html/rfc6455) and AMQP 0-9-1 Client Libraries.

Developers can include the Java WebSocket Client using the following maven dependency to develop and run web applications:

```xml
<dependency>
    <groupId>org.kaazing</groupId>
    <artifactId>gateway.client.java</artifactId>
    <version>[5.1.0.0,5.2.0.0)</version>
</dependency>

```

Developers can include the Java AMQP 0-9-1 Client using the following maven dependency to develop and run web applications:

```xml
<dependency>
    <groupId>org.kaazing</groupId>
    <artifactId>amqp.client.java</artifactId>
    <version>[5.1.0.0,5.2.0.0)</version>
</dependency>

```

# Building This Project

## Minimum Requirements for Building the Project

* Java SE Development Kit (JDK) 7 or higher
* maven 3.0.5 or higher

## Steps for Building This Project

0. Clone the repo: ```git clone https://github.com/kaazing/java.client.git```
0. Go to the cloned directory: ```cd java.client```
0. Build the project: ```mvn clean install```

# Running the WebSocket demo

## Run From the Command-Line
0. Change directory: ```cd ws/demo/target```
0. Run the demo application: ```java -cp . -jar gateway.client.java.demo-develop-SNAPSHOT.jar```

## Run From within Eclipse
0. Import the project in Eclipse
0. Under `gateway.client.java.demo` project, right-click on `WebSocketFrame.java` or `WebSocketApplet.java` in
`src/main/java/org.kaazing.net.ws.demo` and run.

# Running the AMQP 0-9-1 demo

## From the Command-Line
0. Change directory: ```cd amqp0-9-1/demo/target```
0. Run the demo application: ```java -cp . -jar amqp.client.java.demo-develop-SNAPSHOT.jar```

## From within Eclipse
0. Import the project in Eclipse
0. Under `amqp.client.java.demo` project, right-click on `AmqpFrame.java` or `AmqpApplet.java` in
src/main/java/org.kaazing.net.ws.amqp.demo and run!

# Learning How to Use the Gateway

To learn how to administer the Gateway, its configuration files, and security, see the documentation on [developer.kaazing.com](http://developer.kaazing.com/documentation/5.0/index.html). To contribute to the documentation source, see the [doc directory](/doc).

# Learning How to Develop Client Applications

To learn how to develop client applications using the Gateway, see the documentation on [developer.kaazing.com](http://developer.kaazing.com/documentation/5.0/index.html).

# View a Running Demo

To view a demo of this client, see [kaazing.org](http://kaazing.org/)
