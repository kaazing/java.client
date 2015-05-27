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

package org.kaazing.net.ws.demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.nio.ByteBuffer;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.SwingUtilities;

import org.kaazing.net.auth.BasicChallengeHandler;
import org.kaazing.net.auth.LoginHandler;
import org.kaazing.net.http.HttpRedirectPolicy;
import org.kaazing.net.ws.WebSocket;
import org.kaazing.net.ws.WebSocketFactory;
import org.kaazing.net.ws.WebSocketMessageReader;
import org.kaazing.net.ws.WebSocketMessageType;
import org.kaazing.net.ws.WebSocketMessageWriter;

public class WebSocketFrame extends JFrame {

    private static final long serialVersionUID = 5027838948297191966L;

    private WebSocket webSocket;

    private static final int LIST_SIZE = 150;
    private javax.swing.JButton connect;
    private javax.swing.JButton close;
    private javax.swing.JButton sendText;
    private javax.swing.JButton sendBinary;
    private javax.swing.JTextField location;
    private javax.swing.JTextField message;
    private JList logList;
    private javax.swing.JButton clear;
    private DefaultListModel logModel;

    private javax.swing.JLabel locationLabel;
    private javax.swing.JLabel introLabel;
    private javax.swing.JLabel messageLabel;
    private javax.swing.JLabel logLabel;
    private javax.swing.JPanel connectPanel;
    private javax.swing.JPanel messagePanel;
    
    private WebSocketFactory   wsFactory;
    private boolean            closedExplicitly = false;
    
    public static void main(String[] args) {
        // Schedule a job for the event-dispatching thread: creating and showing this application's GUI.
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

    /**
     * Sets up the login handler for responding to "Application Basic" or "Application Negotiate" challenges.
     */
    private void setupLoginHandler(final Frame parentFrame, String locStr) {
        wsFactory = WebSocketFactory.createWebSocketFactory();
        int index = locStr.indexOf("://");
        if (index != -1) {
            locStr = locStr.substring(index + 3);
            index = locStr.indexOf("/");
            if (index != -1) {
                locStr = locStr.substring(0, index);
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
                            log("EXCEPTION: "+e.getMessage());
                        }
                        return new PasswordAuthentication(username, password);
                    }
                };
                    
                BasicChallengeHandler challengeHandler = BasicChallengeHandler.create();
                challengeHandler.setLoginHandler(loginHandler);
                wsFactory.setDefaultChallengeHandler(challengeHandler);
                wsFactory.setDefaultRedirectPolicy(HttpRedirectPolicy.ALWAYS);
            }
        }
    }

    public void start() {
        logModel = new DefaultListModel();
        Container p = this.getContentPane();
        p.removeAll();
        p.setLayout(new BorderLayout());

        WebSocketPanel webSocketPanel = new WebSocketPanel();
        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closedExplicitly = false;
                
                // WebSocket.connect() is a blocking call. And, it can lead
                // to modal login dialogs to popup. So, we don't want to block AWT's
                // EventDispatchThread. That's why we should create a separate
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

        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    closedExplicitly = true;
                    log("CLOSE");
                    webSocket.close(4002, "Closing with 4002");
                    
                    updateButtonsForClosed();
                    log("CLOSED");
                } catch (Exception e1) {
                    e1.printStackTrace();
                    log(e1.getMessage());
                }
            }
        });

        sendText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    WebSocketMessageWriter messageWriter = webSocket.getMessageWriter();
                    log("SEND: " + message.getText());
                    messageWriter.writeText(message.getText());
                } catch (Exception e1) {
                    log(e1.getMessage());
                    updateButtonsForClosed();
                    log("CLOSED");
                }
            }
        });

        sendBinary.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ByteBuffer payload = ByteBuffer.wrap(message.getText().getBytes());
                    log("SEND BINARY:" + getHexDump(payload));
                    webSocket.getMessageWriter().writeBinary(payload);
                } catch (Exception e1) {
                    log(e1.getMessage());
                    updateButtonsForClosed();
                    log("CLOSED");
                }
            }
        });

        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logModel.clear();
            }
        });
        p.add(webSocketPanel, BorderLayout.CENTER);

        location.setText("ws://echo.websocket.org");

        logModel.setSize(LIST_SIZE);

        updateButtonsForClosed();
    }

    public void stop() {
        if (webSocket != null) {
            try {
                webSocket.close(4002, "Testing");
            } catch (Exception e) {
                e.printStackTrace();
                log("EXCEPTION: "+e.getMessage());
            }
        }
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

    private synchronized void log(final String str) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                logModel.add(0, str);
                if (logModel.getSize() > LIST_SIZE) {
                    logModel.removeElementAt(LIST_SIZE);
                }
            }
        });
    }

    private void updateButtonsForClosed() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                connect.setEnabled(true);
                close.setEnabled(false);
                sendText.setEnabled(false);
                sendBinary.setEnabled(false);
            }
        });
    }

    private void updateButtonsForConnected() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                connect.setEnabled(false);
                close.setEnabled(true);
                sendText.setEnabled(true);
                sendBinary.setEnabled(true);
            }
        });
    }

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
                    javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
                    new java.awt.Font("Dialog", 1, 12))); // NOI18N
            setRequestFocusEnabled(false);

            introLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
            introLabel
                    .setText("<html>This is a demo of an echo server client that uses WebSocket to send text or binary messages to the Kaazing Gateway Echo service, <br>which echoes back the messages.</html>");
            introLabel.setForeground(blueText);

            connectPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
            connectPanel.setPreferredSize(new java.awt.Dimension(400, 75));

            locationLabel.setText("Location:");
            locationLabel.setToolTipText("Enter WebSocket Location");

            location.setText("ws://echo.websocket.org");
            location.setToolTipText("Enter the location of the WebSocket");
            location.setColumns(25);

            connect.setText("Connect");
            connect.setToolTipText("Connect to the Kaazing Gateway via WebSocket");
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
                    javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.ABOVE_TOP));

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
            layout.setHorizontalGroup(layout.createParallelGroup().addComponent(introLabel).addComponent(connectPanel)
                    .addComponent(messagePanel).addComponent(logLabel).addComponent(logList).addComponent(clear));
            layout.setVerticalGroup(layout.createSequentialGroup().addComponent(introLabel).addComponent(connectPanel,
                    GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(
                    messagePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(logLabel).addComponent(logList, 300, 300, 300).addComponent(clear));

        }// </editor-fold>

    }

}
