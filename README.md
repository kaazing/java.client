# java.client

# About this Project

This project contains community version of Java [WebSocket] (https://tools.ietf.org/html/rfc6455) and AMQP 0-9-1 Client Libraries.

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

# Building this Project

## Minimum requirements for building the project

* Java SE Development Kit (JDK) 7 or higher
* maven 3.0.5 or higher

## Steps for building this project

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

# Running the AMQP 0-9-1 demo

## From the command-line
0. Change directory: ```cd amqp0-9-1/demo/target```
0. Run the demo application: ```java -cp . -jar amqp.client.java.demo-develop-SNAPSHOT.jar```

## From within Eclipse
0. Import the project in Eclipse
0. Under `amqp.client.java.demo` project, right-click on `AmqpFrame.java` or `AmqpApplet.java` in
src/main/java/org.kaazing.net.ws.amqp.demo and run!

# Learning How to Develop Client Applications

Learn to develop RFC-6455 based [Java client applications](http://kazing.org/documentaton/5.0/dev-java/o_dev_java.html).

# View a Running Demo

View a demo (see kaazing.org)
