# gateway.client.java

# About this Project

gateway.client.java is an implementation of the WebSocket specification [RFC-6455] (https://tools.ietf.org/html/rfc6455) in Java. The jars from [gateway.client.java.api](https://github.com/kaazing/gateway.client.java.api), [gateway.client.java.internal](https://github.com/kaazing/gateway.client.java.internal), and [gateway.client.java.transport](https://github.com/kaazing/gateway.client.java.transport) repos are shaded/combined to create a single jar to make it convenient for the application developers.

Include this project as a maven dependency with the following
```xml
<dependency>
    <groupId>org.kaazing</groupId>
    <artifactId>gateway.client.java</artifactId>
    <version>[5.1.0.0,5.2.0.0)</version>
</dependency>

```


# Building this Project

## Minimum requirements for building the project

* Java SE Development Kit (JDK) 7 or higher
* maven 3.0.5 or higher

## Steps for building this project

0. Clone the repo: ```git clone https://github.com/kaazing/gateway.client.java.git```
0. Go to the cloned directory: ```cd gateway.client.java```
0. Build the project: ```mvn clean install```

# Learning How to Develop Client Applications

Learn to develop RFC-6455 based [Java client applications](http://kazing.org/documentaton/5.0/dev-java/o_dev_java.html).

# View a Running Demo

View a demo (see kaazing.org)
