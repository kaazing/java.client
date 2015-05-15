/**
 * Copyright (c) 2007-2014, Kaazing Corporation. All rights reserved.
 */

package org.kaazing.net.sse.demo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.kaazing.net.sse.SseEventReader;
import org.kaazing.net.sse.SseEventSource;
import org.kaazing.net.sse.SseEventSourceFactory;
import org.kaazing.net.sse.SseEventType;

@SuppressWarnings("serial")
public class ServerSentEventsApplet extends JApplet implements ActionListener {

    private static final long serialVersionUID = 3412240189439244444L;
    JTextField url = new JTextField(30);
    JButton addSse = new JButton("Create Sse");
    JButton closeSse = new JButton("Stop Sse");
    JButton clear = new JButton("Clear");
    SseEventSource eventSource = null;
    
    JLabel logArea = new JLabel() {
        public Dimension getPreferredSize() {
            return new Dimension(400, 400);
        }

        public Dimension getMinimumSize() {
            return new Dimension(400, 200);
        }

        public Dimension getMaximumSize() {
            return new Dimension(400, 800);
        }
    };

    public void init() {
        setBackground(Color.WHITE);
        setSize(800, 600);
        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout());
        p.add(new JLabel("Enter SSE URL:"));
        p.add(url);

        URL documentUrl = getDocumentBase();
        String host = documentUrl.getHost();
        int    port = documentUrl.getPort();
        
        if ((host == null) || (host.trim().length() == 0)) {
            host = "localhost";
        }
        
        if (port == -1) {
            port = 80;
        }
        String locationUrl = "sse://" + host + ":" + port;
        url.setText(locationUrl + "/sse");

        JPanel p1 = new JPanel();
        p1.setLayout(new FlowLayout());
        p1.add(addSse);
        p1.add(closeSse);
        p1.add(clear);
        JPanel p2 = new JPanel();
        p2.setLayout(new FlowLayout());
        p2.add(logArea);
        addSse.addActionListener(this);
        closeSse.addActionListener(this);
        clear.addActionListener(this);
        closeSse.setEnabled(false);
        this.getContentPane().add(p);
        this.getContentPane().add(p1);
        this.getContentPane().add(p2);
    }

    public void actionPerformed(ActionEvent arg0) {
        try {
            if (arg0.getSource() == addSse) {
                SseEventSourceFactory factory = SseEventSourceFactory.createEventSourceFactory();
                eventSource = factory.createEventSource(URI.create(url.getText()));

                // Connect to the event source.                
                eventSource.connect();

                // Since this method is being executed on AWT's 
                // EventDispatchThread, we don't want to block it. So, a new
                // thread is being spawned to receive SSE events for as long
                // as the connection stays alive.
                Thread sseEventReaderThread = new Thread() {
                    public void run() {
                        try {
                            SseEventReader reader = eventSource.getEventReader();
                            
                            SseEventType type = null;
                            while ((type = reader.next()) != SseEventType.EOS) {
                                switch (type) {
                                    case DATA:
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
                sseEventReaderThread.start();
                
                addSse.setEnabled(false);
                closeSse.setEnabled(true);
            } else if (arg0.getSource() == clear) {
                logArea.setText("");
            } else {
                eventSource.close();
                addSse.setEnabled(true);
                closeSse.setEnabled(false);
            }
        } catch (Exception e) {
            logArea.setText("Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
