-   [Home](../../index.md)
-   [Documentation](../index.md)
-   Use the Java EventSource API

Use the Java EventSource API
============================

**Note:** To use the Gateway, a KAAZING client library, or a KAAZING demo, fork the repository from [kaazing.org](http://kaazing.org).

This procedure describes how you can use the `EventSource` API--provided by the Kaazing Java client library--in Java. This API allows you to take advantage of the server-sent events standard as described in the [HTML5 specification](http://www.w3.org/html/wg/html5/#server-sent-events). For example, you can create a Java applet or stand-alone Java application that uses the Java HTML5 Communications client library to receive streaming data from a news feed or streaming financial data. The support for server-sent events is provided by the `EventSource` class and its supporting classes.

The following steps show you how to use the `EventSource` API in a Java applet or stand-alone Java application. This example highlights some of the most commonly used `EventSource` methods and is not meant to be an end-to-end tutorial. Refer to the [EventSource API documentation](../apidoc/client/java/gateway/index.md) for a complete description of all the available methods. View the out of the box Server Sent Events demo code in `GATEWAY_HOME/demo/java/src/core/com/kaazing/net/sse/demo/ServerSentEventsApplet.java`. The example code below is taken from this demo.

Before You Begin
----------------

This procedure is part of [Build Java WebSocket Clients](o_dev_java.md):

1.  [Set Up Your Development Environment](p_dev_java_setup.md)
2.  [Use the Java WebSocket API](p_dev_java_websocket.md)
3.  **Use the Java EventSource API**
4.  [Migrate Android and Java Clients to KAAZING Gateway 5.0](p_dev_android_migrate.md)
5.  [Secure Your Java and Android Clients](p_dev_java_secure.md)
6.  [Display Logs for the Java Client](p_dev_java_logging.md)
7.  [Troubleshoot Your Java Client](p_dev_java_tshoot.md)

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

Next Step
---------

[Migrate Android and Java Clients to KAAZING Gateway 5.0](p_dev_android_migrate.md)
