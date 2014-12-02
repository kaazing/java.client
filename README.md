# gateway.client.java.internal 

# About this Project

This project contains the internal implementation for the public APIs exposed in  [gateway.client.java.api](https://github.com/kaazing/gateway.client.java.api). The jar from this project is shaded along with other jars to create a single consolidated jar in [gateway.client.java](https://github.com/kaazing/gateway.client.java).

# Building this Project

## Minimum requirements for building the project
* Java Developer Kit (JDK) or Java Runtime Environment (JRE) Java 7 (version 1.7.0_21) or higher
* Maven 3.0.5

## Steps for building this project
0. Clone the repo: ```git clone https://github.com/kaazing/gateway.client.java.internal.git```
0. Go to the cloned directory: ```cd gateway.client.java.internal```
0. Build the project: ```mvn clean install```

# Integrate this project

0. Integrate this component in gateway.client.java by updating the version in gateway.client.java pom file
0. Build gateway.client.java and use it for application development

