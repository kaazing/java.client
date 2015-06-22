# java.client

# About This Project

This project contains the community version of the Java AMQP 0-9-1 Client Library.

The java.client project implements the AMQP protocol in Java. It provides a WebSocket client API that enables developers to build Java applications that communicate with an AMQP broker over WebSocket via an RFC-6455 endpoint, such as KAAZING Gateway.

Developers can include the Java AMQP 0-9-1 Client using the following maven dependency to develop and run web applications:

```xml
<dependency>
    <groupId>org.kaazing</groupId>
    <artifactId>amqp.client.java</artifactId>
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

# Running the AMQP 0-9-1 demo

## From the command-line
0. Change directory: ```cd amqp0-9-1/demo/target```
0. Run the demo application: ```java -cp . -jar amqp.client.java.demo-develop-SNAPSHOT.jar```

## From within Eclipse
0. Import the project in Eclipse
0. Under `amqp.client.java.demo` project, right-click on `AmqpFrame.java` or `AmqpApplet.java` in
src/main/java/org.kaazing.net.ws.amqp.demo and run!

# Using KAAZING Gateway or any RFC-6455 Endpoint

You can use an RFC-6455 endpoint, such KAAZING Gateway, to connect to a back-end service. To learn how to administer the Gateway, its configuration files, and security, see the documentation on [developer.kaazing.com](http://developer.kaazing.com/documentation/5.0/index.html).

# Learning How to Develop Client Applications

To learn how to develop client applications with these projects, see the documentation on [developer.kaazing.com](http://developer.kaazing.com/documentation/5.0/index.html).

# View a Running Demo

To view demos of clients built with these projects, see [kaazing.org](http://kaazing.org/)
