/**
 * Copyright (c) 2007-2014, Kaazing Corporation. All rights reserved.
 */

package com.kaazing.net.ws.amqp.demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;

import javax.swing.JFrame;

public class AmqpFrame extends JFrame {
	private static final long serialVersionUID = -2872874862601616651L;

    public AmqpFrame(String title) {
        super(title);
    }

    public static void main(String[] args) {
        // Schedule a job for the event-dispatching thread: creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	AmqpFrame frame = new AmqpFrame("Java AMQP Demo");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.init();
                frame.pack();
                frame.setVisible(true);
                frame.setSize(800, 600);
            }
        });
    }

    public void init() {
        AmqpPanel webSocketPanel = new AmqpPanel("ws://localhost:8001/amqp");

        setBackground(Color.WHITE);
        Container p = this.getContentPane();
        p.setBackground(Color.WHITE);
        p.setLayout(new BorderLayout());
        p.add(webSocketPanel, BorderLayout.CENTER);
    }
}
