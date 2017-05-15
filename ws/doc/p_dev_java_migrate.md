Migrate WebSocket and ByteSocket Applications to KAAZING Gateway 5.0
========================================================================================

**Note:** To use the Gateway, a KAAZING client library, or a KAAZING demo, fork the repository from [kaazing.org](http://kaazing.org).

This topic covers the issues involved in migrating KAAZING Gateway \ - Community Edition 3.2-3.5 Java clients built using the ByteSocket API to the Java API in KAAZING Gateway 5.0. KAAZING Gateway 5.0 no longer includes the ByteSocket API because the WebSocket and WsURLConnection classes in the KAAZING Gateway 5.0 Java library support binary messaging.

For more information about the KAAZING Gateway 5.0 Java API, see [Use the Java WebSocket API](p_dev_java_websocket.md) and [Java Client API](../apidoc/client/java/gateway/index.md).

Before You Begin
----------------

This procedure is part of [Build Java WebSocket Clients](o_dev_java.md):

1.  [Set Up Your Development Environment](p_dev_java_setup.md)
2.  [Use the Java WebSocket API](p_dev_java_websocket.md)
3.  [Use the Java EventSource API](p_dev_java_eventsource.md)
4.  Migrate WebSocket and ByteSocket Applications to KAAZING Gateway 5.0
5.  [Secure Your Java and Android Clients](p_dev_java_secure.md)
6.  [Display Logs for the Java Client](p_dev_java_logging.md)
7.  [Troubleshoot Your Java Client](p_dev_java_tshoot.md)

Legacy Client Support
---------------------

KAAZING Gateway 5.0 supports Java clients written using the KAAZING Gateway 3.2-3.5 Java APIs. You do not need to migrate your legacy Java client to use KAAZING Gateway 5.0. If you wish to take advantage of the new Java API for KAAZING Gateway 5.0 or new features of KAAZING Gateway 5.0, you must migrate your Java client.

The new Java API for KAAZING Gateway 5.0 is easier to use than previous versions, but it does require that you change your KAAZING Gateway 3.2-3.5 Java API client code to take advantage of it.

Binary Support in the KAAZING Gateway 5.0 Java API
--------------------------------------------------

Both WebSocket and WsURLConnection classes support APIs that enable use the use of byte-based data streams as they define [java.io.InputStream](http://docs.oracle.com/javase/7/docs/api/java/io/InputStream.html) and [java.io.OutoutStream](http://docs.oracle.com/javase/7/docs/api/java/io/OutputStream.html) methods. `getInputStream()` is used for receiving binary streams and `getOutputStream()` is used to sending binary streams. If either method is used to receive text messages, or the methods are invoked before a connection is made, then an IOException is thrown. Once the connection is closed, a new InputStream or OutputStream should be obtained using these methods after the connection has been established. Using the old InputStream or OutputStream will result in IOException.

For information and example, see [Use the Java WebSocket API](p_dev_java_websocket.md).

Blocking Calls and ByteSocketListener
-------------------------------------

The ByteSocketListener interface in the KAAZING Gateway - HTML5 Edition 3.x Java WebSocket API is not included in the Java WebSocket API for KAAZING Gateway 5.0.

WebSocket.ReadyState and ByteSocket.ReadyState No Longer Used in the Java WebSocket API
---------------------------------------------------------------------------------------

The `WebSocket.ReadyState` class in the Java WebSocket API in KAAZING Gateway - HTML5 Edition 3.x is not included in the Java WebSocket API for KAAZING Gateway 5.0 (all editions).

The Java WebSocket API for KAAZING Gateway 5.0 supports blocking for the `connect()` and `close()` methods and eliminates the need for the `WebSocket.ReadyState` and `ByteSocket.ReadyState`.

Similarly, the `InputStream`, `Reader`, and `WebSocketMessageReader` support blocking calls that eliminate the need for `MessageEvent` as the control will come out of the blocking call only when the message has arrived.

Next Step
---------

[Secure Your Java and Android Clients](p_dev_java_secure.md)


