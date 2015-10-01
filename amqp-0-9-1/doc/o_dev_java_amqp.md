Build Java AMQP Clients
=======================

Note: To use the Gateway, a KAAZING client library, or a KAAZING demo, fork the repository from [kaazing.org](http://kaazing.org).
This checklist provides the steps necessary to enable your Java application to communicate with any AMQP 0-9-1 compliant message broker using the KAAZING Gateway AMQP Java client libraries and either the Gateway or and RFC-6455 WebSocket endpoint that supports AMQP 0-9-1:

| \# | Step                                                                                                                                | Topic or Reference                                                                        |
|:---|:------------------------------------------------------------------------------------------------------------------------------------|:------------------------------------------------------------------------------------------|
| 1  | Learn about the KAAZING Gateway Java AMQP client.                                                                                   | [Overview of the Java AMQP Client Libraries](#overview-of-the-java-amqp-client-libraries) |
| 2  | Learn to Use the Java AMQP Client Library and the supported methods.                                                                | [Use the Java AMQP Client Library](#use-the-java-amqp-client-library)                     |
| 3  | Learn how to authenticate your client by implementing a challenge handler to respond to authentication challenges from the Gateway. | [Secure Your Java AMQP Client](#secure-your-java-and-android-clients)                     |

**Note:** Java Developer Kit (JDK) or Java Runtime Environment (JRE) 1.6 and above is required.

Overview of AMQP 0-9-1
----------------------

Advanced Message Queuing Protocol (AMQP) is an open standard for messaging middleware that was originally designed by the financial services industry to provide an interoperable protocol for managing the flow of enterprise messages. To guarantee messaging interoperability, AMQP 0-9-1 defines both a wire-level protocol and a model—the AMQP Model—of messaging capabilities.

The AMQP Model defines three main components:

1.  *Exchange*: clients publish messages to an exchange
2.  *Queue*: clients read messages from a queue
3.  *Binding*: a mapping from an exchange to a queue

An AMQP client connects to an AMQP broker and opens a channel. Once the channel is established, the client can send messages to an exchange and receive messages from a queue. To learn more about AMQP functionality, take a look at the [Real-Time Interactive Guide to AMQP](../guide-amqp.md), an interactive guide that takes you step-by-step through the main features of AMQP version 0-9-1.

For more information about AMQP, visit [http://www.amqp.org](http://www.amqp.org).

WebSocket and AMQP
------------------

WebSocket enables direct communication from the browser to an AMQP broker. The Gateway Java AMQP library radically simplifies Web application design so developers can code directly against the back-end AMQP broker without the need for custom Servlets or server-side programming.

Overview of the Java AMQP Client Libraries
------------------------------------------

KAAZING Gateway Java AMQP client libraries allow clients to subscribe from and publish messages to a message broker using AMQP. With the KAAZING Gateway Java AMQP client libraries, you can leverage WebSocket in your application. This WebSocket client then enables communication over AMQP between your application and the message broker, as shown in the following figure:

![](images/f-amqp-web-java-client-web.png)

**Figure: KAAZING Gateway Java AMQP Client Connection**

Starting an AMQP Broker
-----------------------

There are a wide variety of AMQP brokers available that implement different AMQP versions. For example, RabbitMQ, Apache Qpid, OpenAMQ, Red Hat Enterprise MRG, ØMQ, and Zyre. </span>

**Note:** The AMQP client libraries are compatible with AMQP version 0-9-1. Refer your AMQP broker documentation for information about certified AMQP versions.
For information on integrating with RabbitMQ, see [Integrate RabbitMQ Messaging](https://github.com/kaazing/gateway/blob/develop/doc/integration-amqp/p_amqp_integrate_rabbitmq.md).

Taking a Look at the AMQP Demo
------------------------------

Before you start, take a look at a demo that was built with the Java version of the AMQP client library. To see this demo in action, perform the following steps:

1.  Go to [kaazing.org](http://kaazing.org) to fork or download the Java AMQP demo and the Gateway. You can also use an RFC-6455 WebSocket endpoint that supports AMQP 0-9-1.
2.  Start the KAAZING Gateway (or an RFC-6455 WebSocket endpoint that supports AMQP 0-9-1) and an AMQP broker (such as Apache Qpid).
3.  Run the Java demo, connect to the Gateway or RFC-6455 WebSocket endpoint that supports AMQP 0-9-1, and send and receive messages.


Use the Java AMQP Client Library
================================

**Note:** To use the Gateway, a KAAZING client library, or a KAAZING demo, fork the repository from [kaazing.org](http://kaazing.org).

In this procedure, you will learn how to use the KAAZING Gateway Java AMQP Client library and the supported APIs.

To Use the Java AMQP Client Library
-----------------------------------

**Note:** You can use the KAAZING Gateway Java AMQP Client library to communicate with any AMQP broker that supports AMQP version 0-9-1. You can use the KAAZING Gateway or any RFC-6455 WebSocket endpoint that supports AMQP version 0-9-1 to connect the KAAZING Gateway Java AMQP client to the AMQP broker. The following example uses KAAZING Gateway.

1.  Set up your development environment.

    1.  Ensure you have a Java IDE, such as Eclipse, which can be downloaded from <http://www.eclipse.org/downloads/>.
    2.  If you haven't done so already, download and install KAAZING Gateway, as described in [Setting Up KAAZING Gateway](https://github.com/kaazing/gateway/blob/develop/doc/about/setup-guide.md), or a RFC-6455 WebSocket endpoint that supports AMQP 0-9-1.
    3.  To develop clients using the KAAZING Gateway Java AMQP client library, you must configure the Gateway or RFC-6455 WebSocket endpoint to communicate with an AMQP broker.

        The following is an example of a configuration element for the AMQP service in the KAAZING Gateway, as specified in the configuration file `GATEWAY_HOME/conf/gateway-config.xml`:

        ``` xml
        <service>
            <name>AMQP Service</name>
            <description>AMQP Service</description>
            <accept>ws://${gateway.hostname}:${gateway.extras.port}/amqp</accept>
            <connect>tcp://${gateway.hostname}:5672</connect>

            <type>amqp.proxy</type>
            <properties>
                <service.domain>${gateway.hostname}</service.domain>
                <encryption.key.alias>session</encryption.key.alias>
            </properties>

            <realm-name>demo</realm-name>

            <!--
            <authorization-constraint>
                <require-role>AUTHORIZED</require-role>
            </authorization-constraint>
            -->

            <cross-site-constraint>
                <allow-origin>http://${gateway.hostname}:${gateway.extras.port}</allow-origin>
            </cross-site-constraint>
        </service>
        ```

        In this case, the service is configured to accept WebSocket AMQP requests from the browser at `ws://localhost:8001/amqp` (`ws://${gateway.hostname}:${gateway.extras.port}/amqp`) and proxy those requests to a locally installed AMQP broker (localhost) at port 5672.

        To configure the Gateway to accept WebSocket requests at another URL or to connect to a different AMQP broker, you can edit `GATEWAY_HOME/conf/gateway-config.xml`, update the values for the `accept` elements, change the `connect` property, and restart the Gateway. For example, the following configuration configures the Gateway to accept WebSocket AMQP requests at `ws://www.example.com:80/amqp` and proxy those requests to an AMQP broker (amqp.example.com) on port 5672.

        ``` xml
        <!-- Proxy service to AMQP server -->
        <service>
            <accept>ws://www.example.com:80/amqp</accept>
            <connect>tcp://amqp.example.com:5672</connect>

            <type>amqp.proxy</type>
        </service>
        ```

2.  Review the common Java AMQP programming steps.

    Now that you have set up your environment to develop Java applications using the Gateway's AMQP client library, you can start creating your application. You can either build a single application that both publishes and consumes messages, or create two different applications to handle each action. The AMQP Java demo located at [kaazing.org](http://kaazing.org) shows a single application that handles both actions. Refer to the [AmqpClient Java API](http://developer.kaazing.com/documentation/5.0/apidoc/client/java/amqp/index.html) documentation for the complete list of all the AMQP command and callback functions.

    **Note:** The Java AMQP programming examples listed in this topic use the Java code in the demo at [kaazing.org](http://kaazing.org) . Using the demo as an example when learning the AmqpClient Java API helps you to understand how the API classes are used in an application that captures and responds to user and message events.

    The common Java AMQP programming steps are:

    1.  Import the client library
    2.  Import the Java AMQP classes
    3.  Declare the variables
    4.  Create the AmqpClient object
    5.  Connect to an AMQP broker
    6.  Create channels
    7.  Declare an exchange
    8.  Declare a queue
    9.  Bind an exchange to a queue
    10. Publish messages
    11. Consume messages
    12. Use transactions
    13. Control message flow
    14. Handle exceptions

3.  Import the libraries.

    Download or fork the Java client library from [kaazing.org](http://kaazing.org) and then import it into your Java client.

    To Use the Java AMQP Client Library in your Java applet or stand-alone Java application, you must include the Java AMQP client library, located at [kaazing.org](http://kaazing.org). The required JAR files are **org.kaazing.gateway.client.jar** and **org.kaazing.gateway.amqp.client.java.jar**.

    **Note:** Refer to the [Java applet documentation](http://docs.oracle.com/javase/7/docs/api/java/applet/Applet.html "Applet (Java Platform SE 7 )") for more information on how to package external code in your Java applet.

    To import the Java client library into an Eclipse project, you add it as an External JAR file:

    1.  Open Eclipse and create a new Java Project.
    2.  Select the project in Project Explorer, click **File**, and then click **Properties**.
    3.  Click **Java Build Path**, click the **Libraries** tab, and then click **Add External JARs**.
    4.  Navigate to the location where you downloaded the **org.kaazing.gateway.client.jar** and **org.kaazing.gateway.amqp.client.java.jar** files, select them, and then click **Open**.

    5.  Click **OK** to close the **Properties** dialog.

        The Java AMQP client library is now listed in Project Explorer.

        **Note:** If you want to generate the KAAZING Gateway Java AMQP demo, you can add the Java AMQP demo Java files to the project. The files are located at [kaazing.org](http://kaazing.org). Simply copy and paste the files into the **src** folder in your Eclipse project. Then click the **Run** menu, click **Run As**, and then click **Java Application**. The Java Application launches.

        ![](images/f-amqp-web-java-app.jpg)

        **Figure: The Java AMQP Demo Run from Eclipse Using the Java Sources Files**

        Ensure that both the Gateway or RFC-6455 WebSocket endpoint that support AMQP version 0-9-1 and the AMQP message broker are running, and then test the application.

4.  Import the Java AMQP classes.

    Add the following import statements in your application's .java file (the file AmqpPanel.java is used in the KAAZING Gateway Java AMQP demo):

    ``` java
    import java.awt.Color;
    import java.awt.EventQueue;
    import java.awt.Frame;
    import java.awt.event.ActionEvent;
    import java.awt.event.ActionListener;
    import java.net.PasswordAuthentication;
    import java.nio.ByteBuffer;
    import java.nio.charset.Charset;
    import java.sql.Timestamp;
    import java.util.Random;

    import javax.swing.DefaultListModel;
    import javax.swing.border.BevelBorder;

    import org.kaazing.net.auth.BasicChallengeHandler;
    import org.kaazing.net.auth.LoginHandler;
    import org.kaazing.net.ws.WebSocketFactory;
    import org.kaazing.net.ws.amqp.AmqpArguments;
    import org.kaazing.net.ws.amqp.AmqpChannel;
    import org.kaazing.net.ws.amqp.AmqpClient;
    import org.kaazing.net.ws.amqp.AmqpProperties;
    import org.kaazing.net.ws.amqp.ChannelAdapter;
    import org.kaazing.net.ws.amqp.ChannelEvent;
    import org.kaazing.net.ws.amqp.ConnectionEvent;
    import org.kaazing.net.ws.amqp.ConnectionListener;
    import org.kaazing.net.ws.amqp.AmqpClientFactory;
    ```

    The 'org.kaazing.net' classes are the relevant Java AMQP client library classes. The other classes are used to capture and respond to action events. In fact, the entire program is defined within the **java.awt.event.ActionListener** interface. See [Lesson: Writing Event Listeners](http://docs.oracle.com/javase/tutorial/uiswing/events/index.html) for more information.

5.  Declare the variables.

    In your application, first declare the variables you will use, as shown in the following example from the Java AMQP demo:

    ``` java
    private AmqpClientFactory amqpClientFactory;
    private AmqpClient  amqpClient;
    private AmqpChannel publishChannel = null;
    private AmqpChannel txnPublishChannel = null;
    private AmqpChannel txnConsumeChannel = null;
    private AmqpChannel consumeChannel= null;
    private String queueName = "queue" + new Random().nextInt();
    private String myConsumerTag = "clientkey";
    private String routingKey = "broadcastkey";
    private String txnQueueName = "txnqueue" + new Random().nextInt();
    private String myTxnConsumerTag = "txnClientkey";
    ```

    There are more variables declared in the Java AMQP demo for handling text fields and buttons, but the variables listed above are used explicitly for the [Java AMQP API](http://developer.kaazing.com/documentation/5.0/apidoc/client/java/amqp/index.html).

6.  Create the AmqpClient Object.

    In the Java AMQP demo, the AmqpClient object is created in response to a user clicking the **Connect** button in the Java client (`connect.addActionListener(this)`).

    ``` java
        amqpClientFactory = AmqpClientFactory.createAmqpClientFactory();
        ...
        public void actionPerformed(ActionEvent arg0) {
            if (arg0.getSource() == connect) {
                try {
                    amqpClient = amqpClientFactory.createAmqpClient();
        ...
    ```

7.  Connect to an AMQP broker.

    Next, you must connect and log in to an AMQP broker. The client generally manages all of its communication on a single connection to an AMQP broker. You establish a connection to an AMQP broker by passing in the broker address, a username and password, the AMQP version you want to use, and, optionally, a virtual host name (the name of a collection of exchanges and queues hosted on independent server domains). These parameters are passed in when you call the connect method as shown in the following example.

    ``` java
    amqpClient.connect(url, virtualHost, jUsernameField1.getText(),
      new String(jPasswordField1.getPassword()));
    ```

    In this example, all of the parameter values are obtained from form fields in the client application.

    ``` java
    public AmqpPanel(String locationUrl)
    {
        initComponents();

        amqpClientFactory = AmqpClientFactory.createAmqpClientFactory();

        connect.addActionListener(this);
        disconnect.addActionListener(this);
        ...
    }
    public void actionPerformed(ActionEvent arg0) {
      if (arg0.getSource() == connect) {
          try {
              amqpClient = amqpClientFactory.createAmqpClient();
              ...
              WebSocketFactory wsFactory = amqpClient.getAmqpClientFactory().getWebSocketFactory();
              wsFactory.setDefaultChallengeHandler(challengeHandler);

              attachEventListeners();

              status.setText("CONNECTING");
              logMessage("\n");
              logMessage("CONNECTING: "+jTextField1.getText()+" "+jUsernameField1.getText());

              String url=jTextField1.getText();

              String virtualHost = jTextField2.getText();
              amqpClient.connect(url, virtualHost, jUsernameField1.getText(), new String(jPasswordField1.getPassword()));
          } catch (Exception e) {
              logMessage(e.getMessage());
          }
      }
      else if (arg0.getSource()==disconnect) {
          logMessage("DISCONNECT");
          status.setText("DISCONNECT");
          amqpClient.disconnect();
      }
      ...
    ```

    **Note:** The Gateway supports AMQP version 0-9-1.

8.  Create channels.

    Once a connection to an AMQP broker has been established, the client must create a channel to communicate to the broker. A channel is a bi-directional connection between an AMQP client and an AMQP broker. AMQP is multi-channeled, which means that channels are multiplexed over a single network socket connection. Channels are light-weight and consume little resources, and therefore used in AMQP's exception handling mechanism—channels are closed when an exception occurs.

    The variables for the two channels (one for publishing to an exchange and one for consuming from a queue) were created in step 5, **Declare the variables**:

    ``` java
        private AmqpChannel publishChannel = null;
        private AmqpChannel consumeChannel = null;
    ```

    With these variables created, you can create the channels as shown in the following example.

    ``` java
        publishChannel = amqpClient.openChannel();
        consumeChannel = amqpClient.openChannel();
    ```

    Once you have created the channels, an event handler is added to `attachEventListeners()` for when the connection opens.

    ``` java
    public void onConnectionOpen(ConnectionEvent e) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                connect.setEnabled(false);
                disconnect.setEnabled(true);
                jButton3.setEnabled(true);
                jButton4.setEnabled(true);
                jButton5.setEnabled(true);
                jButton6.setEnabled(true);
                clearLog.setEnabled(true);
                jTextField5.setEditable(false);
                jTextField7.setEditable(false);
                onOpen();
            }
        });
    }
    ```

    You can see the `onOpen()` function called at the end of the event handler. `onOpen()` then calls `createChannel()` which contains `publishChannel` and `consumeChannel`.

    Most importantly, `createChannel()` uses `ChannelAdapter()` as part of an adapter or wrapper design pattern, and allows you to specify only the methods that your client requires, instead of having to specify every method. See [Adapter pattern](http://en.wikipedia.org/wiki/Adapter_pattern) for more information on this design pattern.

    ``` java
    private void onOpen(){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                logMessage("CONNECTED");
                status.setText("CONNECTED");
                createChannel();
            }
        });
    }

    private void createChannel()
    {
        logMessage("OPEN: Publish Channel");
        publishChannel = amqpClient.openChannel();
        publishChannel.addChannelListener(new ChannelAdapter() {
            @Override
            public void onClose(ChannelEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        logMessage("CLOSED: Publish Channel");
                    }
                });
            }
    ...
        @Override
        public void onOpen(ChannelEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    logMessage("OPENED: Publish Channel");
                    publishChannel.declareExchange(jTextField5.getText(), "fanout", false, false, false, null);
                }
            });
        }
    });
    ...
    logMessage("OPEN: Consume Channel");
        consumeChannel = amqpClient.openChannel();
        consumeChannel.addChannelListener(new ChannelAdapter(){
            @Override
            public void onBindQueue(ChannelEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        logMessage("QUEUE BOUND: "+jTextField5.getText()+" - "+queueName);
                    }
                });
            }
    ...
        @Override
        public void onOpen(ChannelEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    logMessage("OPENED: Consume Channel");

                    consumeChannel.declareQueue(queueName, false, false, false, false, false, null)
                        .bindQueue(queueName, jTextField5.getText(), routingKey, false, null)
                        .consumeBasic(queueName, myConsumerTag, false, false, false, false, null);
                    }
                });
            }
    ...
    }
    ```

    This example shows the event handlers for when a connection is opened. The `createChannel()` function also contains handlers for all other events, such as `onClose()` and `onError()`.

9.  Declare an exchange.

    AMQP messages are published to exchanges. Messages contain a routing key that contains the information about the message's destination. The exchange accepts messages and their routing keys and delivers them to a message queue. You can think of an exchange as an electronic mailman that delivers the messages to a mailbox (the queue) based on the address on the message's envelope (the routing key). Exchanges do not store messages.

    AMQP defines different exchange types. Some of these exchange types (Direct, Fanout, and Topic) must be supported by all AMQP brokers while others (Headers and System) are optional. AMQP brokers can also support custom exchange types. The following are the different types of exchanges:

    -   **Direct:** Messages are sent only to a queue that is bound with a binding key that matches the message's routing key.
    -   **Fanout:** Messages are sent to every queue that is bound to the exchange.
    -   **Topic:** Messages are sent to a queue based on categorical binding keys and wildcards.
    -   **Headers:** Messages are sent to a queue based on their header property values.
    -   **System:** Messages are sent to system services.

    Exchanges can be durable, meaning that the exchange survives broker shutdown and must be deleted manually or non-durable (temporary), meaning that the exchange lasts only until the broker is shutdown. Finally, to check if an exchange exists on the AMQP broker (without actually creating it), you can create a passive exchange. The following example shows how you can create a direct exchange on the publish channel:

    ``` java
    publishChannel.declareExchange(jTextField5.getText(), "fanout", false, false, false, null);
    ```

    In this example, the exchange value is obtained from the text field in the client application (`jTextField5.getText()`), "fanout" is the exchange type, and false specifies whether the exchange is passive, durable, and noWait. null indicates that there are no [AmqpArguments](http://developer.kaazing.com/documentation/5.0/apidoc/client/java/amqp/index.html).

10. Declare a queue.

    AMQP messages are consumed from queues. You can think of a queue as a mailbox: messages addressed to a particular address (the routing key) are placed in the mailbox for the consumer to pick up. If multiple consumers are bound to a single queue, only one of the consumers receives the message (the one that picked up the mail).

    To check if a queue exists on the AMQP broker (without creating it), you can create a passive queue. Additionally, queues can be marked exclusive, meaning that they are tied to a specific connection. If a queue is marked exclusive, it is deleted when the connection on which it was created is closed.

    Queues can be durable, meaning that the queue survives broker shutdown and must be deleted manually or non-durable (temporary) meaning that the queue lasts only until the broker is shut down. Queues can also be marked auto delete, meaning that the queue is deleted automatically when it is no longer in use. The following example shows how you can create a queue on the consume channel:

    ``` java
    consumeChannel.declareQueue(queueName, false, false, false, false, false, null)
        .bindQueue(queueName, jTextField5.getText(), routingKey, false, null)
        .consumeBasic(queueName, myConsumerTag, false, false, false, false, null);
    ```

    In this example, the `queue` value is obtained from the variable defined earlier:

    ``` java
    private String queueName = "queue" + new Random().nextInt();
    ```

    `false` specifies that the queue is not `passive`, `durable`, `exclusive`, `autoDelete` is not enabled, and `noWait` is not set. `null` indicates that there are no [AmqpArguments](http://developer.kaazing.com/documentation/5.0/apidoc/client/java/amqp/index.html).

11. Bind an exchange to a queue.

    Once you have created an exchange and a queue in AMQP, you must bind—or map—one to the other so that messages published to a specific exchange are delivered to a particular queue. You bind a queue to an exchange with a routing key as shown in the following example.

    ``` java
    consumeChannel.declareQueue(queueName, false, false, false, false, false, null)
        .bindQueue(queueName, jTextField5.getText(), routingKey, false, null)
        .consumeBasic(queueName, myConsumerTag, false, false, false, false, null);
    ```

    Note that the exchange name is obtained from the text field in the client application (`jTextField5.getText()`).

    After the exchange is bound to the queue successfully, a BindQueue event is raised, which calls an event handler registered previously:

    ``` java
    public void onBindQueue(ChannelEvent e) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                logMessage("QUEUE BOUND: "+jTextField5.getText()+" - "+queueName);
            }
        });
    }
    ```

12. Publish messages.

    Messages are published to exchanges. The established binding rules (routing keys) then determine to which queue a message is delivered. Messages have content that consists of two parts:

    1.  **Content Header:** A set of properties that describes the message
    2.  **Content Body:** A blob of binary data

    Additionally, messages can be marked mandatory to send a notification to the publisher in case a message cannot be delivered to a queue. You can also mark a message immediate so that it is returned to the sender if the message cannot be routed to a queue consumer immediately. The following example shows how the content body of a message is added to a buffer (AMQP uses a binary message format) and published to an exchange using the publish channel:

    ``` java
    ...
    else if(arg0.getSource()==jButton3){
        // Normal Publish.
        logMessage("MESSAGE PUBLISHED: "+jTextField6.getText());
        ByteBuffer buffer = ByteBuffer.allocate(512);
        buffer.put(jTextField6.getText().getBytes(Charset.forName("UTF-8")));
        buffer.flip();

        Timestamp ts = new Timestamp(System.currentTimeMillis());
        AmqpProperties props = new AmqpProperties();
        props.setMessageId("1");
        props.setCorrelationId("4");
        props.setAppId("AMQPDemo");
        props.setUserId(jUsernameField1.getText());
        props.setContentType("text/plain");
        props.setContentEncoding("UTF-8");
        props.setPriority(6);
        props.setDeliveryMode(1);
        props.setTimestamp(ts);

        AmqpArguments customHeaders = new AmqpArguments();
        customHeaders.addInteger("headerKey1", 100);
        customHeaders.addLongString("headerKey2", "Header value");

        props.setHeaders(customHeaders);

        publishChannel.publishBasic(buffer, props, jTextField5.getText(),
            routingKey, false, false);

    }
    ```

    A custom parameter is passed in for the message. The message text entered by the user is stored in a variable and converted to binary (`buffer.putString(jTextField6.getText(), Charset.forName("UTF-8"));`), and then sent to the exchange specified by the user (`jTextField5.getText()`). Also note that the last two arguments use boolean values for `mandatory` and `immediate`.

    The `AmqpProperties` class defines pre-defined properties as per AMQP 0-9-1 spec and provides type-safe getters and setters for those pre-defined properties. The value of AMQP 0-9-1's standard "headers" property is of type [AmqpArguments](http://developer.kaazing.com/documentation/5.0/apidoc/client/java/amqp/index.html). The KAAZING Gateway Java AMQP library implementation uses `AmqpArguments` to encode the table. Similarly, the KAAZING Gateway AMQP implementation decodes the table and constructs an instance of `AmqpArguments`.

    The username set with the `setUserId()` method must match the user that is currently authenticated with the AMQP broker. If they do not match you will see the following error:
    `PRECONDITION_FAILED - user_id property set to '<name>' but authenticated user was '<name>'`

13. Consume messages.

    Once messages are published, they can be consumed from a queue. A variety of options can be applied to messages in a queue. For example, publishers can choose to require acknowledgement (ack) of messages so that messages can be redelivered in the case of a delivery failure. If the queue is set to **exclusive**, it is scoped to just the current connection and deleted when the connection on which it was established is closed. Additionally, you can use the no local setting to notify the broker not to send messages to the connection on which the messages were published. The following example shows how you can consume messages from a queue on the consume channel:

    ``` java
    consumeChannel.declareQueue(queueName, false, false, false, false, false, null)
        .bindQueue(queueName, jTextField5.getText(), routingKey, false, null)
        .consumeBasic(queueName, myConsumerTag, false, false, false, false, null);
    ```

    After the `consumeBasic` method is successful, the AMQP broker can then start delivering messages to the client and these messages raise the Message event, which calls the corresponding event handler (`onMessage()`). The following example shows how the `onMessage()` function retrieves information from the event object (`e`):

    ``` java
    public void onMessage(final ChannelEvent e) {
        ...
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                logMessage("MESSAGE CONSUMED: "+value);
                AmqpProperties props = e.getAmqpProperties();
                if (props != null) {
                    AmqpArguments headers = props.getHeaders();

                    if (headers != null) {
                        logMessage("Headers: " + headers.toString());
                    }
                    logMessage("Properties " + (String) props.toString());

                    // Acknowledge the message as we passed in a 'false' for
                    // noAck in AmqpChannel.consumeBasic() call. If the
                    // message is not acknowledged, the broker will keep
                    // holding the message. And, as more and more messages
                    // are held by the broker, it will eventually result in
                    // an OutOfMemoryError.
                    AmqpChannel channel = e.getChannel();
                    channel.ackBasic(dt.longValue(), true);
                }
            }
        });
    }
    ```

    Here you can see how the properties and headers are retrieved using AmqpProperties and AmqpArguments methods (`getAmqpProperties()` and `getHeaders()`, respectively).

    #### Message Acknowledgement

    The Boolean parameter `noAck` is optional with the default value of `true`. If `noAck` is `true`, the AMQP broker will not expect any acknowledgement from the client before discarding the message. If `noAck` is `false`, then the AMQP broker will expect an acknowledgement before discarding the message. If `noAck` is specified to be `false`, then you must explicitly acknowledge the received message using `AmqpChannel` `ackBasic()`.

    In the Java AMQP demo code in this procedure, message acknowledgement is being performed because `false` was passed in for `noAck` in `consumeBasic()`. If the Java client acknowledges a message **and** `noAck` is `true` (the default setting), then the AMQP message broker will close the channel.

14. Use transactions.

    AMQP supports transactional messaging, through *server local transactions*. In a transaction, the server only publishes a set of messages as one unit when the client commits the transaction. Transactions only apply to message publishing and not to the consumption of the messages.

    **Note:** Once you commit or rollback a transaction on a channel, a new transaction is started automatically. For this reason you must commit all future messages you want to publish on that channel or create a new, non-transactional channel to publish messages on.

    The following transaction-related methods can be used to work select (start), commit, and rollback a transaction:

    ``` java
    txnPublishChannel.selectTx();
    txnPublishChannel.commitTx();
    txnPublishChannel.rollbackTx();
    ```

    After the transaction is selected successfully, committed, or rolled back, the corresponding events are raised and previously registered event handlers are called.

    ``` java
    txnPublishChannel = amqpClient.openChannel();
    txnPublishChannel.addChannelListener(new ChannelAdapter() {
        @Override
        public void onCommit(ChannelEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    jButton6.setEnabled(true);
                    rollback.setEnabled(false);
                    publish.setEnabled(false);
                    jButton9.setEnabled(false);
                    logMessage("TXN COMMITTED");
                }
            });
        }

        @Override
        public void onOpen(ChannelEvent e) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                // logMessage("OPENED: Publish Channel for Transaction");
                txnPublishChannel.declareExchange(jTextField7.getText(),
                    "fanout", false, false, false, null);
            }
        });
        }

        @Override
        public void onRollback(ChannelEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    jButton6.setEnabled(true);
                    rollback.setEnabled(false);
                    publish.setEnabled(false);
                    jButton9.setEnabled(false);
                    logMessage("TXN ROLLEDBACK");
                }
            });
        }
        @Override
        public void onSelect(ChannelEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    jButton6.setEnabled(false);
                    logMessage("TXN SELECTED/STARTED");
                }
            });
        }
    });
    ...
    ```

15. Control message flow.

    You can use flow control in AMQP to temporarily—or permanently—halt the flow of messages on a channel from a queue to a consumer. If you turn the message flow off, no messages are sent to the consumer. The following example shows how you can turn the flow of messages on a channel off and back on:

    ``` java
    consumeChannel.flowChannel(true);
    consumeChannel.flowChannel(false);
    ```

    After the flow on a channel is halted or resumed successfully, a flow event is raised, which calls the event handler registered previously.

    ``` java
    public void onFlow(ChannelEvent e) {
        try {
            final boolean isActive = e.isFlowActive();
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    logMessage("FLOW: "+(isActive ? "ON" : "OFF"));
                }
            });
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    ```

16. Handle exceptions.

    Channels do not consume large resources, and therefore used in AMQP's exception handling mechanism—channels are closed when an exception occurs. In the Java AMQP demo, detailed information about the exception is captured using the `onConnectionError()` method for ConnectionListener and the `onError()` method for ChannelListener. Together, these methods can be used for all error handling.

    ``` java
    public void onConnectionError(final ConnectionEvent e) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                logMessage("ERROR:" + e.getMessage());
                status.setText("ERROR");
            }
        });
    }
    ...
    public void onError(final ChannelEvent e) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                logMessage("ERROR: Publish Channel - " + e.getMessage());
            }
        });
    }
    ```


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
