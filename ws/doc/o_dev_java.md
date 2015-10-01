Build Java WebSocket Clients
============================

Note: To use the Gateway, a KAAZING client library, or a KAAZING demo, fork the repository from [kaazing.org](http://kaazing.org).

The following checklist provides the steps necessary to build clients to communicate with KAAZING Gateway:

| \# | Step                                                                                                                                      | Topic or Reference                                                                                                                          |
|:---|:------------------------------------------------------------------------------------------------------------------------------------------|:--------------------------------------------------------------------------------------------------------------------------------------------|
| 1  | Learn how to use the Kaazing Java client library in your Java applet or stand-alone Java application.                                     | [Set Up Your Development Environment](#set-up-your-development-environment)                                                                 |
| 2  | Use the WebSocket API provided by the Kaazing Java client library to transmit data (both text and binary) over WebSocket.                 | [Use the Java WebSocket API](#use-the-java-websocket-api)                                                                                   |
| 3  | Use the EventSource API provided by the Kaazing Java client library to take advantage of the HTML5 Server-Sent Events standard.           | [Use the Java EventSource API](#use-the-java-eventsource-api)                                                                               |
| 4  | Migrate your legacy KAAZING Gateway WebSocket or ByteSocket-based client to the WebSocket API-compliant libraries in KAAZING Gateway 5.0. | [Migrate Android and Java Clients to KAAZING Gateway 5.0](p_dev_android_migrate.md#migrate-android-and-java-clients-to-kaazing-gateway-50x) |
| 5  | Learn how to authenticate your Java or Android client with the Gateway.                                                                   | [Secure Your Java and Android Clients](#secure-your-android-and-java-clients)                                                               |
| 6  | Set up logging for your client.                                                                                                           | [Display Logs for the Java Client](#display-logs-for-the-android-or-java-client)                                                            |
| 7  | Troubleshoot the most common issues that occurs when using Java clients.                                                                  | [Troubleshoot Your Java Client](#troubleshoot-your-android-or-java-client)                                                                  |

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

[Java Client API](http://developer.kaazing.com/documentation/5.0/apidoc/client/java/gateway/index.html)

Use the Java WebSocket API
==========================

Note: To use the Gateway, a KAAZING client library, or a KAAZING demo, fork the repository from [kaazing.org](http://kaazing.org).
This procedure describes how you can use the WebSocket API provided by the Kaazing Java client library in Java. This API allows you to take advantage of the WebSocket standard as described in the [HTML5 specification](http://dev.w3.org/html5/spec/Overview.html#network). For example, you can create a stand-alone Java application that uses the Java HTML5 Communications client library to interact directly with a back-end server. The support for WebSocket is provided by the WebSocket class and its supporting classes.

This topic covers the following information:

-   [To Use the WebSocket API in Java](#to-use-the-websocket-api-in-java)
-   [WebSocket and WsURLConnection](#websocket-and-wsurlconnection)
-   [URLFactory](#urlfactory)
-   [Setting and Overriding Defaults on the WebSocketFactory](#setting-and-overriding-defaults-on-the-websocketfactory)
-   [Methods for Text and Binary Messages](#methods-for-text-and-binary-messages)
-   [Build the Java API Client Demo](#build-the-java-api-client-demo)

There are two examples in this topic to show you how to use the WebSocket API in a Java client:

-   [To Use the WebSocket API in Java](#to-use-the-websocket-api-in-java): The first example is a brief, general example of how your client can invoke the interfaces sequentially. The relevant interface will block the next interface until it has been successful or it will throw an exception.
-   [Build the Java API Client Demo](#build-the-java-api-client-demo): The second example provides the steps to create a standalone Java client, including the steps needed to compile and run the interface in Eclipse.

Refer to the [Java API](http://developer.kaazing.com/documentation/5.0/apidoc/client/java/gateway/index.html) documentation for a complete description of all the available methods.

To Use the WebSocket API in Java
-----------------------------------------------------

1.  Add the necessary import statements:

    ``` java
    import org.kaazing.net.ws.WebSocket;
    import org.kaazing.net.ws.WebSocketFactory;
    import org.kaazing.net.ws.WebSocketMessageReader;
    import org.kaazing.net.ws.WebSocketMessageType;
    import org.kaazing.net.ws.WebSocketMessageWriter;
    ```

2.  Create a WebSocket object and connect to a server:

    ``` java
    wsFactory = WebSocketFactory.createWebSocketFactory();
    ws = wsFactory.createWebSocket(URI.create("ws://example.com:8001/path"));
    ws.connect(); // This will block or throw an exception if failed.
    ```

3.  To send messages, add a `WebSocketMessageWriter` object:

    ``` java
    WebSocketMessageWriter writer = ws.getMessageWriter();
    String text = "Hello WebSocket!";
    writer.writeText(text);   // Send text message
    ```

4.  To receive or consume messages, add `WebSocket` and `WebSocketMessageReader` objects:

    ``` java
    wsFactory = WebSocketFactory.createWebSocketFactory();
    ws = wsFactory.createWebSocket(URI.create("ws://example.com:8001/path"));
    ws.connect(); // This will block or throw an exception if failed.

    WebSocketMessageReader reader = ws.getMessageReader();
    WebSocketMessageType type = null; // Block till a message arrives
      // Loop till the connection goes away
      while ((type =  reader.next()) != WebSocketMessageType.EOS) {
        switch (type) { // Handle both text and binary messages
          case TEXT:
            CharSequence text = reader.getText();
            log("RECEIVED TEXT MESSAGE: " + text.toString());
            break;
          case BINARY:
            ByteBuffer buffer = reader.getBinary();
            log("RECEIVED BINARY MESSAGE: " + getHexDump(buffer));
            break;
        }
    }
    ```

    **Note:** The WebSocket connection is created for both the send and receive functions (`ws.connect()`). These examples are used because most clients will either send *or* receive messages. If your client both sends *and* receives messages, you would only need to create a single WebSocket connection.

    Here is an example using a for loop to alternate between text and binary messages, and the code is placed within try catch blocks:

    ``` java
    try {
        // Create a new WebSocket object
        wsFactory = WebSocketFactory.createWebSocketFactory();
        ws = wsFactory.createWebSocket(URI.create("ws://example.com:8001/path"));
        ws.connect(); // This will block or throw an exception if failed.

         /* Use the WebSocketMessageWriter class method getMessageWriter()
         to send text and binary messages */
         WebSocketMessageWriter writer = ws.getMessageWriter();

         // Send messages using a for loop to alternate between text and binary messages
         for (int i = 0; i < 100; i++) {
             String text = "Hello WebSocket - " + i;
             // For even numbered loops, send text
             if (( i %2) == 0) {
                 writer.writeText(text);   // Send text message
             }
             else {
                 // For odd numbered loops, send binary
                 ByteBuffer buffer = ByteBuffer.wrap(text.getBytes());
                 writer.writeBinary(buffer);   // Send binary message
             }
         }
         ws.close();
     }
    catch (Exception ex) {
        ex.printStackTrace();
    }
    ```

    Here is how to receive messages on the consumer side using a while loop and switch statement:

    ``` java
    try {
        // Create a new WebSocket object
        wsFactory = WebSocketFactory.createWebSocketFactory();
        ws = wsFactory.createWebSocket(URI.create("ws://example.com:8001/path"));
        ws.connect(); // This will block or throw an exception if failed.

         // Use the getMessageReader() method
         WebSocketMessageReader reader = ws.getMessageReader();

         WebSocketMessageType type = null; // Block until a message arrives
         while ((type =  reader.next()) != WebSocketMessageType.EOS) { // Loop until the connection is closed
             switch (type) {
                 // Run if type is TEXT
                 case TEXT:
                     CharSequence text = reader.getText();
                      log("RECEIVED TEXT MESSAGE: " + text.toString());
                      break;

                 // Run if type is BINARY
                 case BINARY:
                     ByteBuffer buffer = reader.getBinary();
                      log("RECEIVED BINARY MESSAGE: " + getHexDump(data)); // see getHexDump() below
                      break;
             }
         }
         ws.close();
     }
    catch (Exception ex){
       ex.printStackTrace();
    }

    private String getHexDump(ByteBuffer buf) {
        if (buf.position() == buf.limit()) {
            return "empty";
        }

        StringBuilder hexDump = new StringBuilder();
        for (int i = buf.position(); i < buf.limit(); i++) {
            hexDump.append(Integer.toHexString(buf.get(i)&0xFF)).append(' ');
        }
        return hexDump.toString();
    }
    ```

WebSocket and WsURLConnection
-----------------------------------------------------

The KAAZING Gateway Java WebSocket API offers two options for creating and using WebSocket connections to enable developers to leverage their java.net Socket or URL experience:

-   **WebSocket** - this class and its methods are provided for developers familiar with the [Socket](http://docs.oracle.com/javase/7/docs/api/java/net/Socket.html) class in the java.net package. It implements a socket for stream-based interprocess communication over the Web.
-   **WsURLConnection** - this class is an extension of the [URLConnection](http://docs.oracle.com/javase/7/docs/api/java/net/URLConnection.html) class in the java.net package. It defines a network connection to an object specified by a URL. WsURLConnection adds WebSocket support to URLConnection, allowing you to create, connect and use WebSocket connections in addition to the default URLConnection subclasses HttpURLConnection and JarURLConnection. You can use all of the methods in URLConnection and the additional methods included in the WsURLConnection extension.

### WebSocket Class

The WebSocket class is demonstrated in the [Build the Java API Client Demo](#build-the-java-api-client-demo) example, but there are some additional elements to be aware of such as methods for text and/or binary WebSocket messages. These methods are described in [Methods for Text and Binary Messages](#methods-for-text-and-binary-messages).

### WsURLConnection

The WsURLConnection class is provided for developers accustomed to creating an URLConnection object and then using [java.io.InputStream](http://docs.oracle.com/javase/7/docs/api/java/io/InputStream.html) and [java.io.OutputStream](http://docs.oracle.com/javase/7/docs/api/java/io/OutputStream.html) classes from java.io to receive and send data. The WsURLConnection extends URLConnection to enable you to use WebSocket-specific features and provide bidirectional communication.

The following example demonstrates how WsURLConnection enables you to create a URLConnection object for a WebSocket URL:

``` java
URL location = URLFactory.createURL("ws://localhost:8000/echo");
URLConnection urlConn = location.openConnection();
InputStream inStream = urlConn.getInputStream();
```

There are two important things to note in this example:

-   This example uses the URLFactory class which enables you to instantiate URL objects that support custom protocols and schemes, such as the WebSocket protocol’s ws:// and wss://. [java.net.URL](http://docs.oracle.com/javase/7/docs/api/java/net/URL.html) has native support for http, https, ftp, file, and jar protocols only.
-   The URLConnection object created in the example is an instance of the WsURLConnection class. Since `getInputStream()` and `getOutputStream()` methods are available on URLConnection, there was no need to downcast URLConnection to WsURLConnection. However, if you need to access methods that are not available on URLConnection, but are only available on WsURLConnection, you can [downcast](http://en.wikipedia.org/wiki/Downcasting) the `urlConn` object in the example and then invoke `getMessageReader()`:

    ``` java
    URL location = URLFactory.createURL("ws://localhost:8000/echo");
    URLConnection urlConn = location.openConnection();
    WsURLConnection  wsConn = (WsURLConnection) urlConn;   // Downcasting to WsURLConnection
    WebSocketMessageReader msgReader = wsConn.geMessageReader();
    ```

With WsURLConnection and URLFactory, you can continue to create URLConnection objects as you have previously and simply use WebSocket to take advantage of additional methods provided by WsURLConnection.

URLFactory
-----------------------------------

The URLFactory class is included to support custom protocols and schemes not supported by java.net.URL. Namely, the WebSocket protocol’s ws:// and wss:// schemes. java.net.URL supports http, https, ftp, file, and jar protocols only and the java.net.URLStreamHandlerFactory class registration is not extensible. After importing the URLFactory class, one of the `createURL()` methods is used to create a WebSocket URL object from either:

-   The String representation, by parsing the given specification within a specified context.
-   A specified protocol name, host name, and file name.
-   A specified protocol name, host name, port number, and file name.

</p>
For more information about the URLFactory class, see the KAAZING Gateway [Java WebSocket API](http://developer.kaazing.com/documentation/5.0/apidoc/client/java/gateway/index.html).

Setting and Overriding Defaults on the WebSocketFactory
-------------------------------------------------------

You can set a default redirect-policy on the WebSocketFactory. All the WebSockets created using that factory automatically inherit the default. You can then override the defaults on an individual WebSocket, if desired. Unlike the HttpURLConnection in the Java SDK that uses the boolean InstanceFollowRedirects method to specify whether the WebSocket follows redirects automatically, the KAAZING Gateway Java WebSocket API also provides the following options:

| Option       | Description                                                                                                                                                                                                                                                     |
|:-------------|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| NEVER        | Do not follow HTTP redirects.                                                                                                                                                                                                                                   |
| ALWAYS       | Follow the HTTP redirect requests always, regardless of the origin, domain, etc.                                                                                                                                                                                |
| SAME\_ORIGIN | Follow the HTTP redirect only if the origin of the redirect request matches. This implies that both the scheme/protocol and the authority between the current and the redirect URIs should match. The port number should also be the same between the two URIs. |
| SAME\_DOMAIN | Follow HTTP redirect only if the domain of the redirect request matches the domain of the current request. For example, URIs with identical domains would be `ws://production.example.com:8001` and `ws://production.example.com:8002`.                         |
| PEER\_DOMAIN | Follow the HTTP redirect only if the redirected request is for a peer-domain. For example, the domain in the URI ws://sales.example.com:8001 is a peer of the domain in the URI ws://marketing.example.com:8002.                                                |
| SUB\_DOMAIN  | Follow the HTTP redirect only if the request is for sub-domain. For example, the domain in the URI ws://benefits.hr.example.com:8002 is a sub-domain of the domain in the URI ws://hr.example.com:8001.                                                         |

You can set the default redirect option on the WebSocket Factory using the `setDefaultRedirectPolicy()` method and then override it on a WebSocket connection using the `setRedirectPolicy()` method.

``` java
import org.kaazing.net.http.HttpRedirectPolicy;
.
.
.
WebSocket wsFactory = WebSocketFactory.createWebSocketFactory();
wsFactory.setDefaultRedirectPolicy(HttpRedirectPolicy.SUB_DOMAIN);
wsFactory.createWebSocket(location);
```

Here is an example of the SUB\_DOMAIN option overridden on a WebSocket connection:

``` java
WebSocket ws;
ws = wsFactory.createWebSocket(location);
ws.setRedirectPolicy(HttpRedirectPolicy.ALWAYS);
ws.connect();
```

Methods for Text and Binary Messages
---------------------------------------------------------

Both the WebSocket and WsURLConnection classes offer methods to suit the data types your client will handle.

### Text and Binary Clients

Clients using both text and binary messages can use the `getMessageReader()` and `getMessageWriter()` methods, available in both the WebSocket and WsURLConnection classes. These methods receive binary and text messages based on the WebSocketMessageType class. The WebSocketMessageType class represents the types of messages that are received by `WebSocketMessageReader`. There are three types: TEXT, BINARY, and EOS for end–of–stream. For receiving messages, you can use a switch block for the different message types:

``` java
Thread messageReceivingThread = new Thread() {
    public void run() {
      WebSocketMessageType type = null;
      try {
          WebSocketMessageReader reader = webSocket.getMessageReader();
          while ((type = reader.next()) != WebSocketMessageType.EOS) {
              switch (type) {
                  case BINARY:
                      ByteBuffer data = reader.getBinary();
                      log("RESPONSE:" + getHexDump(data));
                      break;
                  case TEXT:
                      CharSequence text = reader.getText();
                      log("RESPONSE:" + text.toString());
                      break;
              }
          }
      webSocket.close()
        }
      catch (Exception ex) {
          log("Exception: " + ex.getMessage());
      }
    }
};
```

**Note:** In UI-based Java clients, receiving messages should be done on a separate thread to avoid blocking the java.awt [EventDispatchThread](http://docs.oracle.com/javase/tutorial/uiswing/concurrency/dispatch.html). Review the example in [Build the Java API Client Demo](#build-the-java-api-client-demo) to see a demonstration.

### Text-only Clients

Text-only clients can use the `getReader()` and `getWriter()` methods, available in both the WebSocket and WsURLConnection classes. If either method is used to receive binary messages, or the methods are invoked before a connection is made, then an IOException is thrown.

### Binary-only Clients and I/O Streams

Both WebSocket and WsURLConnection classes support APIs that enable use the use of byte-based data streams as they define `getInputStream()` and `getOutputStream()` methods. `getInputStream()` is used for receiving binary streams and `getOutputStream()` is used to send binary streams. If either method is used to receive text messages, or the methods are invoked before a connection is made, then an IOException is thrown. Once the connection is closed, a new InputStream or OutputStream must be obtained using the `getInputStream()` and `OutputStream()` methods after the connection has been established. Using the old InputStream or OutputStream will result in IOException.

Build the Java API Client Demo
--------------------------------------------------------

The following procedure walks through the steps of creating the out of the box Java WebSocket demo that is included with the KAAZING Gateway bundle. The demo code displays how to use the Java WebSocket API to create a client that creates a WebSocket connection with the Gateway, sends and receives text and binary messages, and includes a UI.

In this procedure you will do the following:

1.  Set up a Java project in Eclipse.
2.  Import the KAAZING Gateway Java libraries.
3.  Create the package and class for the Java client.
4.  Add the import statements for the common Java classes.
5.  Add the import statements for the KAAZING Gateway Java library classes.
6.  Add objects for the UI elements, including labels and buttons.
7.  Add the WebSocket and WebSocketFactory objects.
8.  Add the event listeners.
9.  Start the Gateway.
10. Test your new client.

For information about the KAAZING Gateway Java WebSocket API, see [Java WebSocket API](http://localhost:8001/documentation/javadoc/html5/index.html).

With the Gateway running, the out of the box Java demo can be viewed at `http://localhost:8001/demo/`. The files for the demo are located in the following location in the bundle: `GATEWAY_HOME/demo/java/src/gateway`.

The following procedure uses the Eclipse IDE. You can download Eclipse from [www.eclipse.org](http://www.eclipse.org). Java Developer Kit (JDK) or Java Runtime Environment (JRE) Java 7 or higher from [Oracle](http://www.oracle.com/technetwork/java/javase/downloads/index.html) is required also.

1.  Set up a Java project in Eclipse.

    1.  Create a new Java Project in Eclipse. From the **File** menu, click **New** and then **Java Project**. You can name the project whatever you like. This procedure uses the project name **JavaDemo**.
    2.  Right-click the new project, and click **Properties**.

2.  Import the KAAZING Gateway Java libraries.

    1.  In the **Properties** dialog, click **Java Build Path**, click the **Libraries** tab, and click **Add External JARs**.
    2.  Locate and select the KAAZING Gateway Java library. The libraries are located in `GATEWAY_HOME/lib/client/java`. The library you need is `org.kaazing.gateway.client.jar`. The folder also contains a library for clients using Kerberos V5 Network Authentication with the Gateway, `org.kaazing.gateway.client.security.kerberos.jar`. This library is not needed for clients that do not use Kerberos V5 Network Authentication.
    3.  In the **Properties** dialog, click **OK**. You can see the imported library and its classes in your project under `com/kaazing/gateway/client`.

3.  Create the package and class for the Java client.

    1.  In the new project, right-click the **src** folder, click **New** and then **Package**.
    2.  Give the package a unique name, such as **org.kaazing.gateway.client.core.demo**.
    3.  Right-click the new package, click **New** and then **Class**.
    4.  Name the class **WebSocketFrame** and click **Finish**.

4.  Add the import statements for the following common Java classes that will be used in the client:

    ``` java
    import java.awt.BorderLayout;
    import java.awt.Color;
    import java.awt.Container;
    import java.awt.FlowLayout;
    import java.awt.Frame;
    import java.awt.event.ActionEvent;
    import java.awt.event.ActionListener;
    import java.net.URI;
    import java.nio.ByteBuffer;
    import java.util.Arrays;
    import java.util.Collection;

    import javax.swing.DefaultListModel;
    import javax.swing.GroupLayout;
    import javax.swing.JFrame;
    import javax.swing.JList;
    ```

    The java.net.URI class is used to represent URI references in the program and java.nio.ByteBuffer is used to define byte buffers for sending and receiving binary.

5.  Add the import statements for the KAAZING Gateway Java library classes that will be used in the client:

    ``` java
    import org.kaazing.net.ws.WebSocket;
    import org.kaazing.net.ws.WebSocketFactory;
    import org.kaazing.net.ws.WebSocketMessageReader;
    import org.kaazing.net.ws.WebSocketMessageType;
    import org.kaazing.net.ws.WebSocketMessageWriter;
    ```

    For information about these classes, see the [Java WebSocket API](http://developer.kaazing.com/documentation/5.0/apidoc/client/java/gateway/index.html).

6.  Change the default class statement that was generated by Eclipse to:

    ``` java
    public class WebSocketFrame extends JFrame {
        private static final long serialVersionUID = 5027838948297191966L;
    ```

7.  Add objects for the UI elements, including labels and buttons:

    ``` java
    private static final int LIST_SIZE = 150;
    // Buttons and fields
    private javax.swing.JButton connect; // Connect button
    private javax.swing.JButton close; // Close button
    private javax.swing.JButton sendText; // Send Text button
    private javax.swing.JButton sendBinary; // Send Binary button
    private javax.swing.JTextField location; // WebSocket service location field
    private javax.swing.JTextField message; // Message field
    private JList logList;
    private javax.swing.JButton clear;
    private DefaultListModel logModel;

    // Labels
    private javax.swing.JLabel locationLabel;
    private javax.swing.JLabel introLabel;
    private javax.swing.JLabel messageLabel;
    private javax.swing.JLabel logLabel;
    private javax.swing.JPanel connectPanel;
    private javax.swing.JPanel messagePanel;
    ```

8.  Add the WebSocket and WebSocketFactory objects:

    ``` java
    private WebSocket        webSocket;
    private WebSocketFactory wsFactory;
    private boolean          closedExplicitly = false;
    ```

9.  Schedule a job to create and display the client UI:

    ``` java
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                WebSocketFrame frame = new WebSocketFrame("WebSocket Echo Demo");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.start();
                frame.pack();
                frame.setVisible(true);
            }
        });
    }

    public WebSocketFrame(String string) {
        super(string);
    }
    ```

10. Create the function for the WebSocket event listeners:

    ``` java
    public void start() {
            logModel = new DefaultListModel();
            Container p = this.getContentPane();
            p.removeAll();
            p.setLayout(new BorderLayout());

            WebSocketPanel webSocketPanel = new WebSocketPanel();
    ...
    }
    ```

11. Within the `start()` function (`...`), add the event listeners. Before you add event listeners, review what the final GUI of the client will look like so that the relationship between the buttons and GUI event listeners is clear:

    ![](images/gateway-java-GUI.jpg)

    **Figure: Java WebSocket Echo Demo GUI**

    The first event listener is for the **Connect** button. The function will define the parameters of the WebSocket connection (lines 1-17), connect to the server (line 18), and define how received messages are handled (lines 24-60). A separate thread is used for receiving messages because we do not want to block the EventDispatchThread in the swing event handling ([javax.swing.SwingUtilities.isEventDispatchThread](http://docs.oracle.com/javase/tutorial/uiswing/concurrency/dispatch.html)). Note the use of a try catch block. A try catch block is the recommended method for connections to catch any exceptions (lines 31-64). Also, a switch statement is used to switch between text and binary messages (lines 41-59).

    ``` java
    connect.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        closedExplicitly = false;

        // WebSocket.connect() is a blocking call and can lead
        // to modal login dialogs. Do not block AWT's
        // EventDispatchThread. Create a separate
        // thread to connect and subsequently receive messages.
        Thread wsThread = new Thread() {
          public void run() {
            try {
              String locationText = location.getText();
              log("CONNECT: "+locationText);
              setupLoginHandler(Frame.getFrames()[0], locationText);

              URI location = new URI(locationText);

              webSocket = wsFactory.createWebSocket(location);
              webSocket.connect();

              updateButtonsForConnected();
              log("CONNECTED");

              // Receive messages using WebSocketMessageReader.
              final WebSocketMessageReader messageReader = webSocket.getMessageReader();
              WebSocketMessageType type = null;

              while ((type = messageReader.next()) != WebSocketMessageType.EOS) {
                switch (type) {
                  case BINARY:
                  ByteBuffer data = messageReader.getBinary();
                  log("RESPONSE:" + getHexDump(data));
                  break;
                  case TEXT:
                  CharSequence text = messageReader.getText();
                  log("RESPONSE:" + text.toString());
                  break;
                }
              }

              if (!closedExplicitly) {
                updateButtonsForClosed();
                log("CLOSED");
              }
            }
            catch (Exception e1) {
              e1.printStackTrace();
              log("EXCEPTION: "+e1.getMessage());
            }
          }
        };

        wsThread.setName("WebSocket ConnectAndReceiveThread");
        wsThread.start();
      }
    });
    ```

12. Within the `start()` function, add an event listener for when the **Close** button is clicked:

    ``` java
    close.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                log("CLOSE");
                webSocket.close();
                updateButtonsForClosed();
                log("CLOSED");
            } catch (Exception e1) {
                e1.printStackTrace();
                log(e1.getMessage());
            }
        }
    });
    ```

    The `close()` method disconnects with the server. This is a blocking call that returns only when the shutdown is complete. You can supply a status code (section 7.4 in the WebSocket RFC 6455) and a reason for the close.

13. Within the `start()` function, add an event listener for when the **Send Text** button is clicked:

    ``` java
    sendText.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // log("SEND TEXT:" + message.getText());
                final WebSocketMessageWriter writer = webSocket.getMessageWriter();
                log("SEND: " + message.getText());
                writer.writeText(message.getText());
            } catch (Exception e1) {
                e1.printStackTrace();
                log(e1.getMessage());
            }
        }
    });
    ```

    This event listener gets the text submitted by the user using `message.getText()` and then uses the [WebSocketMessageWriter](http://developer.kaazing.com/documentation/5.0/apidoc/client/java/gateway/index.html) class to send the text message to the Gateway using the `writeText()` method. The WebSocketMessageWriter class is invoked by the `getMessageWriter()` method. Once the WebSocket connection is closed, a new WebSocketMessageReader should be obtained after the connection has been established. Using the old reader will result in IOException.

14. Within the `start()` function, add an event listener for when the **Send Binary** button is clicked:

    ``` java
    sendBinary.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                ByteBuffer payload = ByteBuffer.wrap(message.getText().getBytes());
                log("SEND BINARY:" + getHexDump(payload));
                webSocket.getMessageWriter().writeBinary(payload);
            } catch (Exception e1) {
                e1.printStackTrace();
                log(e1.getMessage());
            }
        }
    });
    ```

    This event listener gets the text submitted by the user and then gets the binary values of that text and adds them to a byte buffer. Then the binary is sent to the Gateway using the `getMessageWriter()` method and `writeBinary()`. For the log message, the binary is then sent to the `getHexDump()` function defined later in the client. The `getHexDump()` method will convert the binary into hexadecimal and then into ASCII and return that to this event listener to be written to the log. For example, the text `Hello WebSocket!` is displayed in the log as:
    `48 65 6c 6c 6f 2c 20 57 65 62 53 6f 63 6b 65 74 21`.

15. Within the `start()` function, add an event listener for when the **Clear** button is clicked:

    ``` java
    clear.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            logModel.clear();
        }
    });
    p.add(webSocketPanel, BorderLayout.CENTER);
    ```

    This function clears the log of any messages.

16. Close the `start()` function by setting the default location, log size, and by calling the `updateButtonsForClosed()` function to set the buttons to the closed state.

    ``` java
        location.setText("ws://localhost:8001/echo");

        logModel.setSize(LIST_SIZE);

        updateButtonsForClosed();

    }
    ```

    **Note:** Ensure that you closed the `start()` function using the brace (`}`) in this code.

17. Add a stop function in case the WebSocket connection is still open after close:

    ``` java
    public void stop() {
        if (webSocket != null) {
            try {
                webSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
                log("EXCEPTION: "+e.getMessage());
            }
        }
    }
    ```

18. Add the getHexDump function used by the `sendBinary.addActionListener` event listener:

    ``` java
    private String getHexDump(ByteBuffer buf) {
        if (buf.position() == buf.limit()) {
            return "empty";
        }

        StringBuilder hexDump = new StringBuilder();
        for (int i = buf.position(); i < buf.limit(); i++) {
            hexDump.append(Integer.toHexString(buf.get(i)&0xFF)).append(' ');
        }
        return hexDump.toString();
    }
    ```

    As stated earlier, this function receives a byte buffer containing the message entered by the user converted to binary, and then convert the binary into hexadecimal and then into ASCII and returns that to the event listener to be written to the log.

19. Add a function to remove any log entries beyond the 150 lines set in the `LIST_SIZE` variable created at the beginning of the code:

    ``` java
    private synchronized void log(String str) {
        logModel.add(0, str);
        if (logModel.getSize() > LIST_SIZE) {
            logModel.removeElementAt(LIST_SIZE);
        }
    }
    ```

20. Add functions for updating the buttons when the connection is open or closed:

    ``` java
    private void updateButtonsForClosed() {
        connect.setEnabled(true);
        close.setEnabled(false);
        sendText.setEnabled(false);
        sendBinary.setEnabled(false);
    }

    private void updateButtonsForConnected() {
        connect.setEnabled(false);
        close.setEnabled(true);
        sendText.setEnabled(true);
        sendBinary.setEnabled(true);
    }
    ```

21. Next, add the JPanel GUI for the client:

    ``` java
    public class WebSocketPanel extends javax.swing.JPanel {

        private static final long serialVersionUID = 546964538273207028L;

        public WebSocketPanel() {
            initComponents();
        }

        private void initComponents() {

            connectPanel = new javax.swing.JPanel();
            locationLabel = new javax.swing.JLabel();
            location = new javax.swing.JTextField();
            connect = new javax.swing.JButton();
            close = new javax.swing.JButton();
            introLabel = new javax.swing.JLabel();
            messagePanel = new javax.swing.JPanel();
            messageLabel = new javax.swing.JLabel();
            message = new javax.swing.JTextField();
            sendText = new javax.swing.JButton();
            sendBinary = new javax.swing.JButton();
            logLabel = new javax.swing.JLabel();
            logList = new JList(logModel);
            clear = new javax.swing.JButton();

            setBackground(Color.WHITE);
            connectPanel.setBackground(Color.WHITE);
            messagePanel.setBackground(Color.WHITE);

            Color blueText = new Color(0x3C708F);

            setBorder(javax.swing.BorderFactory.createTitledBorder(null, "WebSocket Demo",
                    javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                    javax.swing.border.TitledBorder.DEFAULT_POSITION,
                    new java.awt.Font("Dialog", 1, 12))); // NOI18N
            setRequestFocusEnabled(false);

            introLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
            introLabel
              .setText("This is a demo of an echo server client that uses
              WebSocket to send text or binary messages to the Echo service,

    which echoes back the messages.");
            introLabel.setForeground(blueText);

            connectPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
            connectPanel.setPreferredSize(new java.awt.Dimension(400, 75));

            locationLabel.setText("Location:");
            locationLabel.setToolTipText("Enter WebSocket Location");

            location.setText("ws://localhost:8001/echo");
            location.setToolTipText("Enter the location of the WebSocket");
            location.setColumns(25);

            connect.setText("Connect");
            connect.setToolTipText("Connect to the the Gateway via WebSocket");
            connect.setName("connect");

            close.setText("Close");
            close.setToolTipText("Close the WebSocket");
            close.setName("close");

            FlowLayout panel1Layout = new FlowLayout(FlowLayout.LEADING, 10, 10);
            connectPanel.setLayout(panel1Layout);
            connectPanel.add(locationLabel);
            connectPanel.add(location);
            connectPanel.add(connect);
            connectPanel.add(close);

            messagePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "",
                    javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                    javax.swing.border.TitledBorder.ABOVE_TOP));

            messageLabel.setText("Message:");
            messageLabel.setToolTipText("Message");

            message.setText("Hello, WebSocket!");
            message.setToolTipText("Enter message for WebSocket");
            message.setColumns(25);

            sendText.setText("Send Text");
            sendText.setToolTipText("Send text message to WebSocket");
            sendText.setName("sendText");

            sendBinary.setText("Send Binary");
            sendBinary.setToolTipText("Send binary message to WebSocket");
            sendBinary.setName("sendBinary");

            FlowLayout panel2Layout = new FlowLayout(FlowLayout.LEADING, 10, 10);
            messagePanel.setLayout(panel2Layout);
            messagePanel.add(messageLabel);
            messagePanel.add(message);
            messagePanel.add(sendText);
            messagePanel.add(sendBinary);

            logLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
            logLabel.setText("Log messages");
            logLabel.setPreferredSize(new java.awt.Dimension(0, 0));
            logLabel.setForeground(blueText);

            logList.setName("log");
            /*
            JScrollPane logScrollPane = new JScrollPane(logList,
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            */
            clear.setText("Clear");
            clear.setToolTipText("Send message to WebSocket");
            clear.setName("clear");

            GroupLayout layout = new GroupLayout(this);
            layout.setAutoCreateGaps(true);
            layout.setAutoCreateContainerGaps(true);
            this.setLayout(layout);
            layout.setHorizontalGroup(
              layout.createParallelGroup()
              .addComponent(introLabel)
              .addComponent(connectPanel)
              .addComponent(messagePanel)
              .addComponent(logLabel)
              .addComponent(logList)
              .addComponent(clear)
            );
            layout.setVerticalGroup(
              layout.createSequentialGroup()
              .addComponent(introLabel)
              .addComponent(
                connectPanel,
                GroupLayout.PREFERRED_SIZE,
                GroupLayout.PREFERRED_SIZE,
                GroupLayout.PREFERRED_SIZE)
              .addComponent(
                messagePanel, GroupLayout.PREFERRED_SIZE,
                GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
              .addComponent(logLabel)
              .addComponent(logList, 300, 300, 300)
              .addComponent(clear)
             );

        }// </editor-fold>

    }
    ```

    The GUI for the completed client will look like this when run from Eclipse:

    ![](images/gateway-java-GUI.jpg)

    **Figure: Java WebSocket Echo Demo GUI**

22. Ensure that the WebSocketFrame class is closed with a final brace:

    ``` java
    }
    ```

    Eclipse has real-time error checking to inform you of any missing syntax elements.

23. Save the client in Eclipse.
24. Start the Gateway as described in **How do I start and stop the Gateway?** in [Setting Up KAAZING Gateway](https://github.com/kaazing/gateway/blob/develop/doc/about/setup-guide.md).
25. Test your new client. In Eclipse, in the **Run** menu, click **Run**. The Java client launches. The **Location** field contains a URI for the Echo service running on the Gateway using the WebSocket scheme, ws://: `ws://localhost:8001/echo`.
26. Click **Connect**. The Log displays:

    ```
    CONNECTED
    CONNECT: ws://localhost:8001/echo
    ```

27. Click **Send Text**. The Log displays the sent text message and the Echo response from the Gateway:

    ```
    RESPONSE:Hello, WebSocket!
    SENT:Hello, WebSocket!
    ```

28. Click **Send Binary**. The Log displays the sent binary message in hexadecimal and the Echo response from the Gateway:

    ```
    RESPONSE: 48 65 6c 6c 6f 2c 20 57 65 62 53 6f 63 6b 65 74 21
    SEND BINARY: 48 65 6c 6c 6f 2c 20 57 65 62 53 6f 63 6b 65 74 21
    ```

29. Click **Close** to close the WebSocket connection.

You now have a working WebSocket client using the KAAZING Gateway Java API. Congratulations! Using what you’ve learned here, you can now build your own Java clients and leverage the power of WebSocket.


Use the Java EventSource API
============================

**Note:** To use the Gateway, a KAAZING client library, or a KAAZING demo, fork the repository from [kaazing.org](http://kaazing.org).

This procedure describes how you can use the `EventSource` API--provided by the Kaazing Java client library--in Java. This API allows you to take advantage of the server-sent events standard as described in the [HTML5 specification](http://www.w3.org/html/wg/html5/#server-sent-events). For example, you can create a Java applet or stand-alone Java application that uses the Java HTML5 Communications client library to receive streaming data from a news feed or streaming financial data. The support for server-sent events is provided by the `EventSource` class and its supporting classes.

The following steps show you how to use the `EventSource` API in a Java applet or stand-alone Java application. This example highlights some of the most commonly used `EventSource` methods and is not meant to be an end-to-end tutorial. Refer to the [EventSource API documentation](http://developer.kaazing.com/documentation/5.0/apidoc/client/java/gateway/index.html) for a complete description of all the available methods. View the out of the box Server Sent Events demo code in `GATEWAY_HOME/demo/java/src/core/com/kaazing/net/sse/demo/ServerSentEventsApplet.java`. The example code below is taken from this demo.

To Use the EventSource API in Java
----------------------------------

1.  Add the necessary import statements:

    ``` java
    // Import java.net classes
    import java.net.URI;
    import java.net.URL;

    // Import EventSource API classes
    import org.kaazing.net.sse.SseEventReader;
    import org.kaazing.net.sse.SseEventSource;
    import org.kaazing.net.sse.SseEventSourceFactory;
    import org.kaazing.net.sse.SseEventType;
    ```

2.  Create a new SseEventSource object and connection using the Server Sent Events factory class:

    ``` java
    SseEventSource eventSource = null; // Create a variable for the Event Source

    // Create Event Source factory
    SseEventSourceFactory factory = SseEventSourceFactory.createEventSourceFactory();

    // Create a target location using the java.net.URI create() method
    eventSource = factory.createEventSource(URI.create(url.getText()));

    // Connect to the event source.                
    eventSource.connect();
    ```

3.  Use the SseEventReader class to create a new reader object, and then use `getEventReader()` method to receive events:

    ``` java
    Thread sseEventReaderThread = new Thread() {
        public void run() {
          try {
              SseEventReader reader = eventSource.getEventReader(); // Receive event stream

              SseEventType type = null;
              while ((type = reader.next()) != SseEventType.EOS) { // Wait until type is DATA
                  switch (type) {
                      case DATA:
                          // Return the payload of the last received event
                          logArea.setText("<html>" + reader.getData() + "</html>");
                          break;
                      case EMPTY:
                          logArea.setText("<html>" + "</html>");
                          break;
                  }
              }

          }
          catch (Exception ex) {
              logArea.setText("Exception: " + ex.getMessage());
          }
        }
    };
    ```

    **Notes:**

    -   The SseEventReader class has a `next()` method that causes the thread to block until an event is received.
    -   The `getData()` method returns the payload of the last received event. This is not a blocking call. This method should be invoked after `next()` only if the returned type is `SseEventType.DATA`. Otherwise, an IOException is thrown.
    -   Receiving events is performed in a separate thread. In UI-based Java clients, receiving events should be done on a separate thread to avoid blocking the java.awt EventDispatchThread.
    -   A switch block is used to manage different types of events. The SseEventType class is used to identify the type. It has three values: `EOS` for end of stream, `EMPTY` for empty events, and `DATA` for events.

4.  Later, you can call the `close()` method in case you want to stop listening to messages from a particular event source.

    `eventSource.close();`


Secure Your Java and Android Clients
====================================

This topic provides information on how to add user authentication functionality to Java and Android clients. The Java and Android Client APIs use the same authentication classes and methods.

A challenge handler is a constructor used in an application to respond to authentication challenges from the Gateway when the application attempts to access a protected resource. Each of the resources protected by the Gateway is configured with a different authentication scheme (for example, Basic, Application Basic, or Application Token), and your application requires a challenge handler for each of the schemes that it will encounter or a single challenge handler that will respond to all challenges. Also, you can add a dispatch challenge handler to route challenges to specific challenge handlers according to the URI of the requested resource.

For information about each authentication scheme type, see [Configure the HTTP Challenge Scheme](https://github.com/kaazing/gateway/blob/develop/doc/security/p_authentication_config_http_challenge_scheme.md).

Before you add security to your clients, follow the steps in [Secure Network Traffic with the Gateway](../security/o_tls.md) and [Configure Authentication and Authorization](https://github.com/kaazing/gateway/blob/develop/doc/security/o_auth_configure.md) to set up security on KAAZING Gateway for your client. The authentication and authorization methods configured on the Gateway influence your client security implementation. In this procedure, we provide an example of the most common implementation.


To Secure Your Java and Android Clients
---------------------------------------

This section includes the following topics:

-   [Overview of Challenge Handlers](#overview-of-challenge-handlers)
-   [Challenge Handler Class Imports](#challenge-handler-class-imports)
-   [Creating a Basic Challenge Handler](#creating-a-basic-challenge-handler)
-   [Creating a Login Handler](#creating-a-login-handler)
-   [Creating a Custom Challenge Handler](#creating-a-custom-challenge-handler)
-   [Overriding Default Challenge Handler Implementations](#overriding-default-challenge-handler-implementations)
-   [Managing Log In Attempts](#managing-log-in-attempts)
-   [Authentication and Connections](#authentication-and-connections)
-   [Registering Challenge Handlers at Locations](#registering-challenge-handlers-at-locations)
-   [Using Wildcards to Match Sub Domains and Paths](#using-wildcards-to-match-sub-domains-and-paths)
-   [Creating Kerberos Challenge Handlers](#creating-kerberos-challenge-handlers) ![This feature is available in KAAZING Gateway - Enterprise Edition](images/enterprise-feature.png)

Overview of Challenge Handlers
-----------------------------------------------------

A challenge handler is responsible for producing responses to authentication challenges from the Gateway. The challenge handler process is as follows:

1.  When an attempt to access a URI protected by the Gateway is made, the Gateway responds with an authentication request, indicating that credentials need to be provided before access to the resource is granted. The specific type of challenge is indicated in a HTTP header called "WWW-Authenticate".
2.  The authentication request and the header are converted into a ChallengeRequest (as defined in RFC 2617) and sent to a challenge handler registered in the client application for authentication challenge responses.
3.  The ChallengeResponse credentials generated by a registered challenge handler are included in a replay of the original request to the Gateway, which allows access to the resource (assuming the credentials are sufficient).

Authenticating your Java client involves implementing a [challenge handler](http://developer.kaazing.com/documentation/5.0/apidoc/client/java/gateway/index.html) to respond to authentication challenges from the Gateway. If your challenge handler is responsible for obtaining user credentials, then implement a [login handler](http://developer.kaazing.com/documentation/5.0/apidoc/client/java/gateway/com/kaazing/net/auth/LoginHandler.html).

Challenge Handler Class Imports
-----------------------------------------------------

To use a challenge handler in your Java client you must add the following imports:

``` java
import org.kaazing.net.auth.BasicChallengeHandler;
import org.kaazing.net.ws.WebSocket;
import org.kaazing.net.ws.WebSocketFactory;
```

Here is an example of all the WebSocket imports, including challenge handlers and kerberos challenge handlers: ![This feature is available in KAAZING Gateway - Enterprise Edition](images/enterprise-feature.png)

``` java
import org.kaazing.net.auth.BasicChallengeHandler;
import org.kaazing.net.auth.LoginHandler;
import org.kaazing.net.http.HttpRedirectPolicy;
import org.kaazing.net.ws.WebSocket;
import org.kaazing.net.ws.WebSocketFactory;
import org.kaazing.net.ws.WebSocketMessageReader;
import org.kaazing.net.ws.WebSocketMessageType;
import org.kaazing.net.ws.WebSocketMessageWriter;
```

Creating a Basic Challenge Handler
----------------------------------

Clients with a single challenge handling strategy for authentication requests can set a specific challenge handler as the default using the setDefaultChallengeHandler() method in the WebSocketFactory class. For example:

``` java
private WebSocketFactory wsFactory;

wsFactory = WebSocketFactory.createWebSocketFactory();
...  
BasicChallengeHandler challengeHandler = BasicChallengeHandler.create();
challengeHandler.setLoginHandler(loginHandler);
wsFactory.setDefaultChallengeHandler(challengeHandler);
```

Each WebSocket created from the factory can have its own Challenge Handler associated with it:

``` java
wsFactory = WebSocketFactory.createWebSocketFactory();
WebSocket ws = wsFactory.createWebSocket(location);
BasicChallengeHandler challengeHandler = BasicChallengeHandler.create();
challengeHandler.setLoginHandler(loginHandler);
ws.setChallengeHandler(challengeHandler);
```

**Note:** The challenge handler API is very flexible and there are many different ways to implement challenge handlers to suit the needs of your client application. For more detailed information on challenge handlers, see the [Java Client API](http://developer.kaazing.com/documentation/5.0/apidoc/client/java/gateway/index.html).

Creating a Login Handler
------------------------

A login handler is responsible for obtaining credentials from an arbitrary source, such as a dialog presented to the user. Login handlers can be associated with one or more challenge handlers (ChallengeHandler objects) to ensure that when a challenge handler requires credentials for a challenge response (ChallengeResponse), the work is delegated to a login handler.

Here is an example using a login dialog to respond to login challenges and obtain user credentials as part of the authentication challenge:

``` java
final LoginHandler loginHandler = new LoginHandler() {
    private String username;
    private char[] password;

    @Override
    public PasswordAuthentication getCredentials() {
        try {
        LoginDialog dialog = new LoginDialog(parentFrame);
        if (dialog.isCanceled()) {
            return null;
        }
        username = dialog.getUsername();
        password = dialog.getPassword();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PasswordAuthentication(username, password);
    }
};

wsFactory = WebSocketFactory.createWebSocketFactory();
wsFactory.setDefaultChallengeHandler(
    BasicChallengeHandler.create().setLoginHandler(loginHandler);
```

**Note:** This example is taken from the out of the box Java Demo at `http://localhost:8001/demo/` and uses an extension of JDialog (javax.swing.JDialog). The source code for the demo is available at [kaazing.org](http://kaazing.org).</span>

Creating a Custom Challenge Handler
-----------------------------------

There are two methods used in ChallengeHandler:

-   `canHandle(ChallengeRequest challengeRequest)` determines if the challenge handler can handle the authentication scheme required by the Gateway (for example, Basic, Application Basic, Negotiate, or Application Token). The method takes a ChallengeRequest object containing a challenge and returns true if the challenge handler has the potential to respond meaningfully to the challenge. If this method determines that the challenge handler can handle the authentication scheme, it returns true and the `handle()` method is used. If this method returns false, the ChallengeHandler class (that contains all of the registered individual ChallengeHandler objects) continues looking for a ChallengeHandler to handle the request.
-   `handle(ChallengeRequest challengeRequest)` handles the authentication challenge by returning a challenge response. Typically, the challenge response invokes a login handler to collect user credentials and transforms that information into a ChallengeResponse object. The ChallengeResponse sends the credentials to the Gateway in an Authorization header and notifies the Gateway on what challenge handler to use for future requests. If `handle()` cannot create a challenge response, it returns `null`.

For information about each authentication scheme type, see [Configure the HTTP Challenge Scheme](https://github.com/kaazing/gateway/blob/develop/doc/security/p_authentication_config_http_challenge_scheme.md).

### Overriding Default Challenge Handler Implementations

After you have developed your own challenge handler, you can install it for future use. For example, to install your own implementation of `BasicChallengeHandler` for a Java client:

1.  Add a JAR file with your `BasicChallengeHandler` implementation to your classpath parameter before the KAAZING Gateway Java client libraries.
2.  Ensure the JAR file contains the following file inside:

    ```
    META-INF/services/org.kaazing.gateway.client.security.BasicChallengeHander
    ```

    The contents of the file should consist of a single line listing the fully-qualified name of your new implementation class (for example, `fully.qualified.challenge.handler.impl.MyChallengeHandler`). For more information, see the [Service Loader](http://docs.oracle.com/javase/7/docs/api/java/util/ServiceLoader.html) documentation.

Managing Log In Attempts
------------------------

When it is not possible for the KAAZING Gateway client to create a challenge response, the client must return `null` to the Gateway to stop the Gateway from continuing to issue authentication challenges.

The following example demonstrates how to stop the Gateway from issuing further challenges.

``` java
/**
* Sets up the login handler for responding to "Application Basic" challenges.
*/
private static int maxRetries = 2; //max retries allowed for wrong credentials
private int retry = 0;    // retry counter

private void setupLoginHandler(final Frame parentFrame, String locStr) {
  wsFactory = WebSocketFactory.createWebSocketFactory();
  int index = locStr.indexOf("://");
  @Override
  public PasswordAuthentication getCredentials() {
    try {
      if (retry++ >= maxRetries) {
        return null;    // stop authentication process if max retry reached
      }
      LoginDialog dialog = new LoginDialog(parentFrame);
      if (dialog.isCanceled()) {
        retry = 0;    // user stopped authentication, reset retry counter
        return null;  // stop authentication process
      }
      username = dialog.getUsername();
      password = dialog.getPassword();

      updateButtonsForConnected();
      log("CONNECTED");
      retry = 0;    //reset retry counter;

      // Receive messages using WebSocketMessageReader.
      final WebSocketMessageReader messageReader = webSocket.getMessageReader();
    }
  }
  catch (Exception e1) {
    retry = 0;     //reset retry counter
    e1.printStackTrace();
    log("EXCEPTION: "+e1.getMessage());  }
...
```

Authentication and Connections
------------------------------

Both `WebSocketFactory` and `JMSConnectionFactory` are used when adding a challenge handler to a Java or Android client's JMS connection to the Gateway. In the following code example, the challenge handler is initiated during the connect event for the JMS connection:

``` java
...
import org.kaazing.net.auth.BasicChallengeHandler;
import org.kaazing.net.auth.ChallengeHandler;
import org.kaazing.net.auth.LoginHandler;
...
public class JmsPanel extends javax.swing.JPanel implements ActionListener, MessageListener, ExceptionListener {
  ...
  private ChallengeHandler createChallengeHandler(String location) {
    final LoginHandler loginHandler = new LoginHandler() {
      private String username;
      private char[] password;
      @Override
      public PasswordAuthentication getCredentials() {
        try {
          LoginDialog dialog = new LoginDialog(Frame.getFrames()[0]);
          if (dialog.isCanceled()) {
            return null;
          }
          username = dialog.getUsername();
          password = dialog.getPassword();
        } catch (Exception e) {
          e.printStackTrace();
        }
        return new PasswordAuthentication(username, password);
      }
    };
    BasicChallengeHandler challengeHandler = BasicChallengeHandler.create();
    challengeHandler.setLoginHandler(loginHandler);
    return challengeHandler;
  }
  ...
  public void actionPerformed(ActionEvent arg0) {
    try {
      if (arg0.getSource() == connect) {

        final ExceptionListener applet = this;
        Thread connectThread = new Thread() {

          @Override
          public void run() {
            try {
              String url = location.getText();
              logMessage("CONNECT: " + url);

              if (connectionFactory instanceof JmsConnectionFactory) {
                JmsConnectionFactory stompConnectionFactory = (JmsConnectionFactory)connectionFactory;
                // initialize the login handler for the target location
                ChallengeHandler challengeHandler = createChallengeHandler(url);
                stompConnectionFactory.setGatewayLocation(new URI(url));
                WebSocketFactory webSocketFactory = stompConnectionFactory.getWebSocketFactory();
                webSocketFactory.setDefaultChallengeHandler(challengeHandler);
                webSocketFactory.setDefaultRedirectPolicy(HttpRedirectPolicy.SAME_DOMAIN);
              }
              ...
```


Registering Challenge Handlers at Locations
-------------------------------------------

When authentication challenges arrive for specific URI locations, the `DispatchChallengeHandler` is used to route challenges to the appropriate challenge handlers. This allows clients to use specific challenge handlers to handle specific types of challenges at different URI locations.

Here is an example of registering a specific location for a challenge handler:

``` java
LoginHandler someServerLoginHandler = ...
NegotiateChallengeHandler  nch = NegotiateChallengeHandler.create();
NegotiableChallengeHandler nblch = NegotiableChallengeHandler.create();
DispatchChallengeHandler   dch = DispatchChallengeHandler.create();

WebSocketFactory        wsFactory = WebSocketFactory.createWebSocketFactory();
wsFactory.setDefaultChallengeHandler(dch.register("ws://host.example.com",
    nch.register(nblch).setLoginHandler(someServerLoginHandler)
);
 // register more alternatives to negotiate here.
)
```

Using Wildcards to Match Sub Domains and Paths
----------------------------------------------

You can use wildcards (“\*”) when registering locations using `locationDescription` in `DispatchChallengeHandler`. Some examples of `locationDescription` values with wildcards are:

-   `*/` matches all requests to any host on port 80 (default port), with no user information or path specified.
-   `*.hostname.com:8000` matches all requests to port 8000 on any sub domain of hostname.com, but not hostname.com itself.
-   `server.hostname.com:*/*` matches all requests to a particular server on any port on any path but not the empty path.

Creating Kerberos Challenge Handlers![This feature is available in KAAZING Gateway - Enterprise Edition](images/enterprise-feature.png)
---------------------------------------------------------------------------------------

The following examples demonstrate different implementations of Kerberos challenge handlers. When registered with the `DispatchChallengeHandler`, a `KerberosChallengeHandler` directly responds to Negotiate challenges where Kerberos-generated authentication credentials are required. In addition, you can use a `KerberosChallengeHandler` indirectly in conjunction with a `NegotiateChallengeHandler` to assist in the construction of a challenge response using object identifiers. For more information, see the [Java Client API](http://developer.kaazing.com/documentation/5.0/apidoc/client/java/gateway/index.html).

This abstract class captures common requirements for a number of implementation flavors for Kerberos, including [Microsoft's SPNEGO implementation](http://tools.ietf.org/html/rfc4559), and a [SPNEGO](http://tools.ietf.org/html/rfc4178) [Kerberos v5 GSS](http://tools.ietf.org/html/rfc4121) implementation.

To successfully use a `KerberosChallengeHandler`, one must know one or more Kerberos KDC service locations and optionally (if not defaulted to "HTTP/requestedURIHostname") provide the name of the specific service being requested.

For the KDC service location, one must establish either:

-   a default Kerberos KDC service location, using `setDefaultLocation(java.net.URI)`, or
-   a mapping from a Kerberos Realm to at least one Kerberos KDC service location using
    `setRealmLocation(String, java.net.URI)`.

For the non-defaulted service name being requested, one can configure the service name using `setServiceName(String)`.

For example, one may install negotiate and a kerberos challenge handler that work together to handle a challenge as:

``` java
import org.kaazing.net.auth.BasicChallengeHandler;
import org.kaazing.net.auth.DispatchChallengeHandler;
import org.kaazing.net.auth.KerberosChallengeHandler;
import org.kaazing.net.auth.LoginHandler;
import org.kaazing.net.auth.NegotiateChallengeHandler;
...
LoginHandler someServerLoginHandler = ...; // perhaps this pops a dialog

KerberosChallengeHandler kerberosChallengeHandler =
    KerberosChallengeHandler.create()
        .setDefaultLocation(URI.create("ws://kb.hostname.com/kerberos5"))
        .setRealmLocation("ATHENA.MIT.EDU", URI.create("ws://athena.hostname.com/kerberos5"))
        .setServiceName("HTTP/servergw.hostname.com")
            .setLoginHandler(someServerLoginHandler)

NegotiateChallengeHandler negotiateChallengeHandler = NegotiateChallengeHandler.create()
    .register(kerberosChallengeHandler);

WebSocketFactory wsFactory = WebSocketFactory.createWebSocketFactory();
wsFactory.setDefaultChallengeHandler(WebSocketDemoChallengeHandler.create()
    .register("ws://gateway.kaazing.wan:8001/echo", negotiateChallengeHandler)
    .register("ws://gateway.kaazing.wan:8001/echo/*", negotiateChallengeHandler));
```

At this point, any user attempting to access `servergw.hostname.com:8000/echo` will be challenged using a `KerberosChallengeHandler` instance. If the user enters credentials with the ATHENA.MIT.EDU realm the realm-specific `athena.hostname.com` KDC will be used to ask for Kerberos credentials for the challenge response. If the user enters credentials with any other realm the `kb.hostname.com` KDC will be used to ask for Kerberos credentials. All requests to either KDC will be for the service name `HTTP/servergw.hostname.com` (indicating access to that HTTP server is the service for which Kerberos credentials are being requested).


Display Logs for the Java Client
=============================================================

**Note:** To use the Gateway, a KAAZING client library, or a KAAZING demo, fork the repository from [kaazing.org](http://kaazing.org).

You can control the log levels for the Java client in the `logging.properties` file on your operating system. For example, for Java 7 (required) on Mac OS X, this file is located in: `$JAVA_HOME/jre/lib/logging.properties`.

For more information, see [Java Logging Overview](http://docs.oracle.com/javase/7/docs/technotes/guides/logging/overview.html). Ensure that you have write permissions to this file.

To Enable the Java Client Logs
------------------------------

1.  Build your Java client, as described in [Build Java WebSocket Clients](o_dev_java.md).
2.  Add the following line to the `logging.properties` file to display the complete log output:

    `org.kaazing.gateway.client.level = ALL`

    **Note:** The Java Logging API has a default logging configuration file located inside the JRE directory at `JRE_DIRECTORY/lib/logging.properties`. For example, on a Mac:

    `/System/Library/Java/JavaVirtualMachines/jdk1.7.0_40.jdk/Contents/Home/jre/lib/logging.properties`

3.  Configure the `ConsoleLogHandler` to display all messages by changing the line:

    `java.util.logging.ConsoleHandler.level = INFO`

    to

    `java.util.logging.ConsoleHandler.level = ALL`

4.  Save the `logging.properties` file.
5.  Enable logging in the Java Console or Java Preferences. To do so:
    -   In Windows, in the Java Control Panel, on the **Advanced** tab, choose **Java console**, then select **Show console**.
    -   On Mac OS X, for Java 7 (required), access the **Java** system preference in **System Preferences**, click **Advanced**, and under **Debugging**, click **Enable logging**. Then, under **Java console**, select **Show console**.

6.  Start the Gateway as described in [Setting Up KAAZING Gateway](https://github.com/kaazing/gateway/blob/develop/doc/about/setup-guide.md).
7.  Restart the browser and use the out of the box Java Echo Demo.
8.  In a browser, navigate to the out of the box demos at `http://localhost:8001/demo/`, click **Java** and follow the instructions. The Java Console will open and display all logs from the Java Client.

Notes
-----

If you are using Microsoft Internet Explorer 8, you may need to restart the browser as an administrator to view the logs from the Java Client.

See Also
--------

See the [Java Client API](http://developer.kaazing.com/documentation/5.0/apidoc/client/java/gateway/index.html).

Troubleshoot Your Java Client
=============================

**Note:** To use the Gateway, a KAAZING client library, or a KAAZING demo, fork the repository from [kaazing.org](http://kaazing.org).

This procedure provides troubleshooting information for the most common issue that occurs when using KAAZING Gateway Java clients.

What Problem Are You Having?
----------------------------

-   [Kerberos challenge handler not working](#kerberos-challenge-handler-not-working)

Kerberos challenge handler not working
-------------------------------------------------------------

**Cause:** [Kerberos challenge handlers](https://github.com/kaazing/java.client/blob/develop/ws/doc/p_dev_java_secure.md#creating-kerberos-challenge-handlers) might not work for one or more of the following reasons:

-   The client cannot connect to the Kerberos Domain Controller (KDC).

    **Solution:** Ping the KDC from the computer running the client and the server hosting the Gateway. Also, ensure that you can Telnet to Kerberos port number 88 from both computers (`telnet> open KDC-server-name 88`).

-   The client cannot obtain a Kerberos ticket.

    **Solution:** Test ticket acquisition by executing the following commands to ensure that the KDC is accessible and able to issue service tickets:

    **For Linux:**

    `$ kinit -t /etc/keytab-name.keytab -S service-instance-name username@KDC-server-name`

    **For Windows:**

    `$ kinit username@KDC-server-name`

    The output will be:

    `Please enter the password for username@KDC-server-name:`

    Enter the password, and then enter:

    `$ klist`

    The ticket cache is displayed along with each ticket's expiration date.

-   Service name is in the incorrect format in the Kerberos challenge handler code.

    **Solution:** The service name should be in the format: `HTTP/servergw.hostname.com`. See [Creating Kerberos Challenge Handlers](https://github.com/kaazing/java.client/blob/develop/ws/doc/p_dev_java_secure.md#creating-kerberos-challenge-handlers) for examples.

-   The pop-up dialog in the client used to obtain user credentials does not ensure that the username format is correct.

    **Solution:** Ensure that the result of the pop-up dialog used to obtain user credentials is formatted as
    `username@KDC-server-name`.

Next Step
---------

You have completed the Java client checklist. For more information on client API development, see the [Java Client API](http://developer.kaazing.com/documentation/5.0/apidoc/client/java/gateway/index.html).
