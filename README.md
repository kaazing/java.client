AMQP 0-9-1 over WebSocket -- Java Client Demo
=============================================

About this Project
------------------
This is a simple maven-based project that shows how to use AMQP 0-9-1 Java Client library!

Requirements
------------
* Java SE Development Kit (JDK) 7 or higher
* Maven 3.0.5 or higher

Steps for building this project
--------------------------------
0. Clone the repo: ```git clone https://github.com/kaazing/amqp.client.java.demo.git```
0. Go to the cloned directory: ```cd amqp.client.java.demo```
0. Build the project: ````mvn clean install````

Setup AMQP Broker
-----------------
0. Download and install Apache QPid 0.28 or later
0. Start Apache QPid using <QPID_INSTALL_DIR>/bin/qpid-start

Setup Kaazing WebSocket Gateway
-------------------------------
0. Download and install Kaazing WebSocket Gateway from kaazing.org
0. Start the Gateway with AMQP Service configured

Running the demo from the command-line
---------------------------------------
0. Change directory: ```cd target```
0. Run the demo application: ```java -cp . -jar amqp.client.java.demo-<5.0.0.1-SNAPSHOT>-shaded.jar```

Running the demo from within Eclipse
------------------------------------
0. Import the project in Eclipse
0. Right-click on AmqpFrame.java or AmqpApplet.java under src/main/java/org.kaazing.net.ws.amqp.demo and run!
