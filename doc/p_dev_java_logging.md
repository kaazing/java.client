-   [Home](../../index.md)
-   [Documentation](../index.md)
-   Display Logs for the Java Client

Display Logs for the Java Client
=============================================================

**Note:** To use the Gateway, a KAAZING client library, or a KAAZING demo, fork the repository from [kaazing.org](http://kaazing.org).

You can control the log levels for the Java client in the `logging.properties` file on your operating system. For example, for Java 7 (required) on Mac OS X, this file is located in: `$JAVA_HOME/jre/lib/logging.properties`.

For more information, see [Java Logging Overview](http://docs.oracle.com/javase/7/docs/technotes/guides/logging/overview.html). Ensure that you have write permissions to this file.

Before You Begin
----------------

This procedure is part of [Build Java WebSocket Clients](o_dev_java.md):

1.  [Set Up Your Development Environment](p_dev_java_setup.md)
2.  [Use the Java WebSocket API](p_dev_java_websocket.md)
3.  [Use the Java EventSource API](p_dev_java_eventsource.md)
4.  [Migrate WebSocket and ByteSocket Applications to KAAZING Gateway 5.0](p_dev_java_migrate.md)
5.  [Secure Your Java and Android Clients](p_dev_java_secure.md)
6.  **Display Logs for the Java Client**
7.  [Troubleshoot Your Java Client](p_dev_java_tshoot.md)

To Enable the Java Client Logs
------------------------------

1.  Build your Java client, as described in [Build Java WebSocket Clients](../dev-java/o_dev_java.md).
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

6.  Start the Gateway as described in [Setting Up KAAZING Gateway](../about/setup-guide.md).
7.  Restart the browser and use the out of the box Java Echo Demo.
8.  In a browser, navigate to the out of the box demos at `http://localhost:8001/demo/`, click **Java** and follow the instructions. The Java Console will open and display all logs from the Java Client.

Notes
-----

If you are using Microsoft Internet Explorer 8, you may need to restart the browser as an administrator to view the logs from the Java Client.

Next Step
---------

[Troubleshoot Your Java Client](p_dev_java_tshoot.md)

See Also
--------

You have completed the Java client howtos. For more information on client API development, see the [Java Client API](../apidoc/client/java/gateway/index.md).

