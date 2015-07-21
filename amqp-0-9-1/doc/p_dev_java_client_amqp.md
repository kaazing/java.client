Use the Java AMQP Client Library
================================

**Note:** To use the Gateway, a KAAZING client library, or a KAAZING demo, fork the repository from [kaazing.org](http://kaazing.org).

In this procedure, you will learn how to use the KAAZING Gateway Java AMQP Client library and the supported APIs.

Before You Begin
----------------

This procedure is part of [Build Java AMQP Clients](o_dev_java.md):

1.  [Overview of the KAAZING Gateway AMQP Client Library](o_dev_java_amqp.md#overview-of-the-java-amqp-client-libraries)
2.  **Use the Java AMQP Client Library**
3.  [Secure Your Java AMQP Client](p_dev_java_secure.md)

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


Next Step
---------

[Secure Your Java AMQP Client](p_dev_java_secure.md)
