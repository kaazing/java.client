/**
** This is free and unencumbered software released into the public domain.
**
** Anyone is free to copy, modify, publish, use, compile, sell, or
** distribute this software, either in source code form or as a compiled
** binary, for any purpose, commercial or non-commercial, and by any
** means.
**
** In jurisdictions that recognize copyright laws, the author or authors
** of this software dedicate any and all copyright interest in the
** software to the public domain. We make this dedication for the benefit
** of the public at large and to the detriment of our heirs and
** successors. We intend this dedication to be an overt act of
** relinquishment in perpetuity of all present and future rights to this
** software under copyright law.
**
** THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
** EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
** MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
** IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
** OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
** ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
** OTHER DEALINGS IN THE SOFTWARE.
**
** For more information, please refer to <http://unlicense.org/>
*/

package org.kaazing.net.ws.amqp.demo;

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

public class AmqpPanel extends javax.swing.JPanel implements ActionListener {

    private static final long serialVersionUID = -2872874862601616651L;

    // Variables declaration - do not modify
    // GEN-BEGIN:variables
    private javax.swing.JButton connect;
    private javax.swing.JButton clearLog;
    private javax.swing.JButton disconnect;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton rollback;
    private javax.swing.JButton publish;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel status_temp;
    private javax.swing.JList jList1;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jUsernameField1;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel status;
    private DefaultListModel logModel = new DefaultListModel();
    // End of variables declaration
    // GEN-END:variables
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

    public AmqpPanel(String locationUrl)
    {
        initComponents();
        
        amqpClientFactory = AmqpClientFactory.createAmqpClientFactory();

        connect.addActionListener(this);
        disconnect.addActionListener(this);
        jButton3.addActionListener(this);
        jButton4.addActionListener(this);
        jButton5.addActionListener(this);
        jButton6.addActionListener(this);
        rollback.addActionListener(this);
        publish.addActionListener(this);
        jButton9.addActionListener(this);
        clearLog.addActionListener(this);

        disconnect.setEnabled(false);
        jButton3.setEnabled(false);
        jButton4.setEnabled(false);
        jButton5.setEnabled(false);
        jButton6.setEnabled(false);
        rollback.setEnabled(false);
        publish.setEnabled(false);
        jButton9.setEnabled(false);
        clearLog.setEnabled(false);

        setBackground(Color.WHITE);
        jTextField1.setText(locationUrl);
    }

    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getSource() == connect) {
            try {
                amqpClient = amqpClientFactory.createAmqpClient();
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
        else if (arg0.getSource()==publish) {
            // Transactions Publish
            logMessage("TXN MESSAGE PUBLISHED: "+jTextField8.getText());
            ByteBuffer buffer = ByteBuffer.allocate(512);
            buffer.put(jTextField8.getText().getBytes(Charset.forName("UTF-8")));
            buffer.flip();
            AmqpProperties props = new AmqpProperties();
            txnPublishChannel.publishBasic(buffer, props, jTextField7.getText(), routingKey, false, false);
        }
        else if(arg0.getSource()==jButton4){
            consumeChannel.flowChannel(true);
        }
        else if(arg0.getSource()==jButton5){
            consumeChannel.flowChannel(false);
        }
        else if(arg0.getSource()==jButton6){
            logMessage("TXN SELECT/START");
            rollback.setEnabled(true);
            publish.setEnabled(true);
            jButton9.setEnabled(true);
            txnPublishChannel.selectTx();
        }
        else if(arg0.getSource()==rollback){
            logMessage("TXN ROLLBACK");
            txnPublishChannel.rollbackTx();
        }
        else if(arg0.getSource()==jButton3){
            // Normal Publish.
            logMessage("MESSAGE PUBLISHED: "+jTextField6.getText());
            ByteBuffer buffer = ByteBuffer.allocate(512);
            buffer.put(jTextField6.getText().getBytes(Charset.forName("UTF-8")));
            buffer.flip();

            Timestamp      ts = new Timestamp(System.currentTimeMillis());
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

            publishChannel.publishBasic(buffer, props, jTextField5.getText(), routingKey, false, false);
        }
        else if (arg0.getSource()==jButton9){
            logMessage("TXN COMMIT");
            txnPublishChannel.commitTx();
        }
        else if (arg0.getSource()==clearLog){
            synchronized (logModel) {
                logModel.clear();
            }
        }
    }

    private void attachEventListeners() {
        amqpClient.addConnectionListener(new ConnectionListener() {
            @Override
            public void onConnecting(ConnectionEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        logMessage("CONNECTING");
                        status.setText("CONNECTING");
                    }
                });
            }

            @Override
            public void onConnectionError(final ConnectionEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        logMessage("ERROR:" + e.getMessage());
                        status.setText("ERROR");
                    }
                });
            }

            @Override
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

            @Override
            public void onConnectionClose(ConnectionEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        logMessage("DISCONNECTED");
                        status.setText("DISCONNECTED");

                        if (publishChannel != null) {
                            publishChannel.closeChannel(0, "", 0, 0);
                        }

                        if (txnPublishChannel != null) {
                           txnPublishChannel.closeChannel(0, "", 0, 0);
                        }

                        if (consumeChannel != null) {
                            consumeChannel.closeChannel(0, "", 0, 0);
                        }

                        if (txnConsumeChannel != null) {
                           txnConsumeChannel.closeChannel(0, "", 0, 0);
                        }

                        publishChannel = null;
                        txnPublishChannel = null;
                        consumeChannel= null;
                        txnConsumeChannel = null;

                        connect.setEnabled(true);
                        disconnect.setEnabled(false);
                        jButton3.setEnabled(false);
                        jButton4.setEnabled(false);
                        jButton5.setEnabled(false);
                        jButton6.setEnabled(false);
                        rollback.setEnabled(false);
                        publish.setEnabled(false);
                        jButton9.setEnabled(false);

                        jTextField5.setEditable(true);
                        jTextField7.setEditable(true);
                        // clearLog.setEnabled(false);
                    }
                });
            }
        });
    }

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

            @Override
            public void onError(final ChannelEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        logMessage("ERROR: Publish Channel - " + e.getMessage());
                    }
                });
            }

            @Override
            public void onDeclareExchange(ChannelEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        logMessage("EXCHANGE DECLARED: "+jTextField5.getText());
                    }
                });
            }

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

        // logMessage("OPEN: Publish Channel for Transaction");
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
                        txnPublishChannel.declareExchange(jTextField7.getText(), "fanout", false, false, false, null);
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

            @Override
            public void onClose(ChannelEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        logMessage("CLOSED: Consume Channel");
                    }
                });
            }

            @Override
            public void onConsumeBasic(ChannelEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        logMessage("CONSUME FROM QUEUE: "+queueName);
                    }
                });
            }

            @Override
            public void onDeclareQueue(ChannelEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        logMessage("QUEUE DECLARED: "+queueName);
                    }
                });
            }

            @Override
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

            @Override
            public void onMessage(final ChannelEvent e) {
            	byte[] bytes = new byte[e.getBody().remaining()];
            	e.getBody().get(bytes);
                final Long dt = (Long) e.getArgument("deliveryTag");
                final String value= new String(bytes, Charset.forName("UTF-8"));
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
        });

        // logMessage("OPEN: Consume Channel for Transaction");
        txnConsumeChannel = amqpClient.openChannel();
        txnConsumeChannel.addChannelListener(new ChannelAdapter() {
            @Override
            public void onMessage(final ChannelEvent e) {
            	byte[] bytes = new byte[e.getBody().remaining()];
            	e.getBody().get(bytes);
                final Long dt = (Long) e.getArgument("deliveryTag");
                final String value= new String(bytes, Charset.forName("UTF-8"));
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        logMessage("TXN MESSAGE CONSUMED: "+value);
                        
                        // Acknowledge the message as we passed in a 'false' for
                        // noAck in AmqpChannel.consumeBasic() call. If the
                        // message is not acknowledged, the broker will keep
                        // holding the message. And, as more and more messages
                        // are held by the broker, it will eventually result in 
                        // an OutOfMemoryError.
                        AmqpChannel channel = e.getChannel();
                        channel.ackBasic(dt.longValue(), true);
                    }
                });
            }

            @Override
            public void onOpen(ChannelEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        // logMessage("OPENED: Consume Channel for Transaction");

                        txnConsumeChannel.declareQueue(txnQueueName, false, false, false, false, false, null)
                                         .bindQueue(txnQueueName, jTextField7.getText(), routingKey, false, null)
                                         .consumeBasic(txnQueueName, myTxnConsumerTag, false, false, false, false, null);
                    }
                });
            }
        });
    }

    private void logMessage(String message) {
        synchronized (logModel) {
            logModel.add(0, message);
            if (logModel.getSize() > 20) {
                logModel.removeElementAt(20);
            }
        }
    }

    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList(logModel);
        disconnect = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        connect = new javax.swing.JButton();
        jTextField8 = new javax.swing.JTextField();
        publish = new javax.swing.JButton();
        jUsernameField1 = new javax.swing.JTextField();
        rollback = new javax.swing.JButton();
        clearLog = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jTextField7 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jPasswordField1 = new javax.swing.JPasswordField();
        jLabel12 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel15 = new javax.swing.JLabel();
        status_temp= new javax.swing.JLabel();
        status= new javax.swing.JLabel();

        jPanel1.setBackground(Color.WHITE);
        jPanel1.setBounds(0, 0, 930, 600);

        jTextField2.setText("/");
        jTextField2.setName("virtualhosttxt"); // NOI18N

        jLabel4.setFont(new java.awt.Font("Verdana", 1, 11));
        jLabel4.setText("User Name");
        jLabel4.setName("username"); // NOI18N

        jTextField1.setName("locationtxt"); // NOI18N

        jScrollPane1.setViewportView(jList1);

        disconnect.setText("Disconnect");
        disconnect.setName("disconnect"); // NOI18N

        jLabel5.setFont(new java.awt.Font("Verdana", 1, 11));
        jLabel5.setText("Password");
        jLabel5.setName("password"); // NOI18N

        jTextField5.setText("demo_exchange");
        jTextField5.setName("exchangetxt"); // NOI18N

        connect.setText("Connect");
        connect.setName("connect"); // NOI18N

        jTextField8.setText("Demo transaction message");
        jTextField8.setName("messagetxtxt"); // NOI18N

        publish.setText("Publish");
        publish.setName("publish"); // NOI18N

        jUsernameField1.setText("guest");
        jUsernameField1.setName("usernametxt"); // NOI18N

        rollback.setText("Rollback");
        rollback.setName("rollback"); // NOI18N

        clearLog.setText("Clear Log");
        clearLog.setName("clearlog"); // NOI18N

        jButton3.setText("Publish");
        jButton3.setName("publish"); // NOI18N

        jButton5.setText("Flow Off");
        jButton5.setName("flowoff"); // NOI18N

        jTextField7.setText("demo_txn_exchange");
        jTextField7.setName("exchangetxtxt"); // NOI18N

        jLabel3.setFont(new java.awt.Font("Verdana", 1, 11));
        jLabel3.setText("Virtual Host");
        jLabel3.setName("virtualhost"); // NOI18N

        jLabel6.setFont(new java.awt.Font("Verdana", 1, 11));
        jLabel6.setText("Exchange");
        jLabel6.setName("exchange"); // NOI18N

        jLabel14.setFont(new java.awt.Font("Verdana", 0, 12));
        //jLabel14.setIcon(new javax.swing.ImageIcon(this.getClass()
                //.getClassLoader().getResource("/resources/images/status-info.png"))); // NOI18N
        jLabel14.setText("Log messages");

        jLabel7.setFont(new java.awt.Font("Verdana", 1, 11));
        jLabel7.setText("Message");
        jLabel7.setName("message"); // NOI18N

        jButton6.setText("Select");
        jButton6.setName("select"); // NOI18N

        jPasswordField1.setText("guest");
        jPasswordField1.setName("passwordtxt"); // NOI18N

        jLabel12.setFont(new java.awt.Font("Verdana", 0, 12));
        //jLabel12.setIcon(new javax.swing.ImageIcon(this.getClass()
        //        .getClassLoader().getResource("/resources/images/status-info.png"))); // NOI18N
        jLabel12.setText("Publish a message to an exchange");
        jLabel12.setBorder(javax.swing.BorderFactory.createBevelBorder(BevelBorder.RAISED, new Color(240,240,240), Color.LIGHT_GRAY));

        jLabel9.setFont(new java.awt.Font("Verdana", 1, 11));
        jLabel9.setText("Message");
        jLabel9.setName("messagetx"); // NOI18N

        jLabel8.setFont(new java.awt.Font("Verdana", 1, 11));
        jLabel8.setText("Exchange");
        jLabel8.setName("exchangetx"); // NOI18N

        jButton4.setText("Flow On");
        jButton4.setName("flowon"); // NOI18N

        jButton9.setText("Commit");
        jButton9.setName("commit"); // NOI18N

        jLabel11.setFont(new java.awt.Font("Verdana", 0, 12));
        //jLabel11.setIcon(new javax.swing.ImageIcon(this.getClass()
        //        .getClassLoader().getResource("/resources/images/status-info.png"))); // NOI18N
        jLabel11.setText("Connect to an AMQP broker");
        jLabel11.setBorder(javax.swing.BorderFactory.createBevelBorder(BevelBorder.RAISED, new Color(240,240,240), Color.LIGHT_GRAY));

        jTextField6.setText("Demo message");
        jTextField6.setName("messagetxt"); // NOI18N

        jLabel2.setFont(new java.awt.Font("Verdana", 1, 11));
        jLabel2.setText("Location");
        jLabel2.setName("location"); // NOI18N

        jLabel13.setFont(new java.awt.Font("Verdana", 0, 12));
        //jLabel13.setIcon(new javax.swing.ImageIcon(this.getClass()
        //        .getClassLoader().getResource("/resources/images/status-info.png"))); // NOI18N
        jLabel13.setText("<html>Select, send a message to, and commit or <br>rollback a transaction</html>");
        jLabel13.setBorder(javax.swing.BorderFactory.createBevelBorder(BevelBorder.RAISED, new Color(240,240,240), Color.LIGHT_GRAY));

        jLabel10.setFont(new java.awt.Font("Verdana", 0, 12));
        //jLabel10.setIcon(new javax.swing.ImageIcon(this.getClass()
        //        .getClassLoader().getResource("/resources/images/status-info.png"))); // NOI18N
        jLabel10.setText("This is a demo of AMQP Java client. For more information about AMQP, see the interactive guide");

        jLabel1.setFont(new java.awt.Font("Verdana", 1, 14));
        jLabel1.setText("AMQP Demo");

        jCheckBox1.setName("autoreconnectchkbox"); // NOI18N

        jLabel15.setFont(new java.awt.Font("Verdana", 1, 11));
        jLabel15.setText("Auto Reconnect");

        status_temp.setFont(new java.awt.Font("Verdana", 1, 11));
        status_temp.setText("Status");

        status.setText("DISCONNECT");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1332, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 807, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(publish)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton9)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(rollback))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(status_temp, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            //.addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                                            .addComponent(connect, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                //.addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(status, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(1, 1, 1)
                                                .addComponent(jUsernameField1))
                                            .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)
                                            .addComponent(jTextField2)
                                            .addComponent(jPasswordField1)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jTextField8)
                                            .addComponent(jTextField7, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(140, 140, 140)
                                        .addComponent(disconnect, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE)))
                                .addGap(95, 95, 95)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(clearLog, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel7)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                                            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                    .addComponent(jTextField6, javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jTextField5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE))))
                                        .addGap(85, 85, 85)))))
                        .addGap(535, 535, 535))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jUsernameField1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3)
                            .addComponent(jButton4)
                            .addComponent(jButton5)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        //.addComponent(jCheckBox1)
                        //.addGap(10, 10, 10)
                        .addComponent(status)
                        .addGap(252, 252, 252))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        //.addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        //.addGap(17, 17, 17)
                        .addComponent(status_temp, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(connect)
                            .addComponent(disconnect)
                            .addComponent(clearLog, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(26, 26, 26)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
                                            .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButton6)
                                    .addComponent(publish, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(rollback)
                                    .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(56, 56, 56))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(51, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
}
