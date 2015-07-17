Build Java WebSocket Clients
============================

Note: To use the Gateway, a KAAZING client library, or a KAAZING demo, fork the repository from [kaazing.org](http://kaazing.org).

The following checklist provides the steps necessary to build clients to communicate with KAAZING Gateway:

| \#  | Step                                                                                                                                                               | Topic or Reference                                                                                                  |
|-----|--------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------|
| 1   | Learn how to use the Kaazing Java client library in your Java applet or stand-alone Java application.                                                              | [Set Up Your Development Environment](p_dev_java_setup.md)                                                        |
| 2   | Use the WebSocket API provided by the Kaazing Java client library to transmit data (both text and binary) over WebSocket.                                          | [Use the Java WebSocket API](p_dev_java_websocket.md)                                                             |
| 3   | Use the EventSource API provided by the Kaazing Java client library to take advantage of the HTML5 Server-Sent Events standard.                                    | [Use the Java EventSource API](p_dev_java_eventsource.md)                                                         |
| 4   | Migrate your legacy KAAZING Gateway WebSocket or ByteSocket-based client to the WebSocket API-compliant libraries in KAAZING Gateway 5.0. | [Migrate Android and Java Clients to KAAZING Gateway 5.0](p_dev_android_migrate.md) |
| 5   | Learn how to authenticate your Java or Android client with the Gateway.                                                                                         | [Secure Your Java and Android Clients](p_dev_java_secure.md)                                                      |
| 6   | Set up logging for your client.                                                                                                                                    | [Display Logs for the Java Client](p_dev_java_logging.md)                                                         |
| 7   | Troubleshoot the most common issues that occurs when using Java clients.                                                                                           | [Troubleshoot Your Java Client](p_dev_java_tshoot.md)                                                             |

**Note:** Java Developer Kit (JDK) or Java Runtime Environment (JRE) 1.6 and above are required.

Overview of Java
----------------

Java applications are platform–independent because they are compiled to [bytecode](http://en.wikipedia.org/wiki/Bytecode) that can then be run on a Java virtual machine (JVM) on any certified operating system.

**Note:** A Java applet is a Java program you can include in an HTML page. Java applets run in the browser's JVM. This documentation does not cover Java applets. For more information about Java and Java applets, visit <http://java.com>.

WebSocket and Java
------------------

KAAZING Gateway provides support for its HTML5 Communication protocol libraries in Java. Using the Java client library, you can enable the HTML5 Communication protocols (for example, WebSocket and Server-Sent Events (SSE)) in new or existing Java applications. For example, you can create a Java client to get streaming financial data from a back-end server using WebSocket, or you can create a Java client to receive streaming news data through SSE. The following figure shows a high-level overview of the architecture:

![Java client architecture overview](images/f-html5-java-client2-web.jpg)

**Figure: Java client architecture overview**

Taking a Look at the Java Client Demo
-------------------------------------

Before you start, take a look at an out of the box demo built using the Java client library: the Java applet that is part of the KAAZING Gateway bundle. To see this Java applet in action, perform the following steps:

1.  Start the Gateway as described in [Setting Up KAAZING Gateway](https://github.com/kaazing/gateway/blob/develop/doc/about/setup-guide.md).
2.  In a browser, navigate to the out of the box demos at `http://localhost:8001/demo/`.
3.  Click **Java** and follow the instructions.

The out of the box Java demo shows WebSocket being used in a Java environment.

Taking a Look at the Java Client Demo Using Eclipse
---------------------------------------------------

Running the out of the box Java demo in Eclipse enables you to see the code and a working Java client. The KAAZING Gateway download (the full download, not the “base” download) includes the demo files, and these can be easily added to an Eclipse project using the following steps (for download information, see **What are my download options?** in [Setting Up KAAZING Gateway](https://github.com/kaazing/gateway/blob/develop/doc/about/setup-guide.md)):

1.  Open Eclipse. If you do have Eclipse, you can [download it](http://www.eclipse.org/downloads/ "Eclipse Downloads") and install it.
2.  In Eclipse, in the **File** menu, click **New**, and then click **Java Project**.
3.  Enter a name for the project, such as **WebSocket**, and click **Next**.
4.  In **Java Settings**, click the **Libraries** tab, and then click **Add External JARs**.
5.  Navigate to the KAAZING Gateway Java libraries here: `GATEWAY_HOME/lib/client/java`, and double-click the library **org.kaazing.gateway.client.jar**.
6.  Click **Finish**.
7.  In your new project folder in Eclipse, right-click the **src** folder under your new project, click **New**, and then click **Package**.
8.  In **Java Package**, enter the name **org.kaazing.net.ws.demo** and click **Finish**.
9.  In your system's file manager, navigate to the demo source files that are included with the KAAZING Gateway download:
     `GATEWAY_HOME/demo/java/src/gateway/com/kaazing/net/ws/demo`.
10. Copy the files named **LoginDialog.java** and **WebSocketFrame.java**.
11. In Eclipse, right-click the **org.kaazing.net.ws.demo** package under the **src** folder in your new project, and click **Paste**. The demo source files are added to the package.
12. Start the Gateway as described in **How do I start and stop the Gateway?** in [Setting Up KAAZING Gateway](https://github.com/kaazing/gateway/blob/develop/doc/about/setup-guide.md).
13. In Eclipse, from the **Run** menu, click **Run As** and then **Java Application**. (By default, Eclipse should be configured to build the project automatically. If you receive a build error, from the **Project** menu, click **Build Project**.)

    The Java client launches in a new window titled **WebSocket Echo Demo**.

14. Click **Connect**. The Log displays:

     ```
     CONNECTED
     CONNECT: ws://localhost:8001/echo
     ```

15. Click **Send Text**. The Log displays the sent text message and the Echo response from the Gateway:

     ```
     RESPONSE:Hello, WebSocket!
     SENT:Hello, WebSocket!
     ```

16. Click **Send Binary**. The Log displays the sent binary message in hexadecimal and the Echo response from the Gateway:

     ```
     RESPONSE: 48 65 6c 6c 6f 2c 20 57 65 62 53 6f 63 6b 65 74 21
     SEND BINARY: 48 65 6c 6c 6f 2c 20 57 65 62 53 6f 63 6b 65 74 21
     ```

17. Click **Close** to close the WebSocket connection.

See Also
--------

[Java Client API](../apidoc/client/java/gateway/index.md)
