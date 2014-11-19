/**
 * Copyright (c) 2007-2014, Kaazing Corporation. All rights reserved.
 */

package com.kaazing.net.ws.amqp.demo;

import java.awt.BorderLayout;
import java.awt.Container;
import java.net.URL;

import javax.swing.JApplet;

public class AmqpApplet extends JApplet {

	private static final long serialVersionUID = -2872874862601616651L;
    
    public void init() {                 
        URL documentUrl = getDocumentBase();
        String locationUrl = "";
        if (documentUrl.getProtocol().equalsIgnoreCase("https")) {
            locationUrl = "wss://";
        }
        else {
            locationUrl = "ws://";
        }
	
        int port = documentUrl.getPort();
        if (port > 0) {
            locationUrl += documentUrl.getHost() + ":" + port;
        }
        else {
            if (documentUrl.getHost().equals(""))
            {
            	// This is to deal with the case when we run from within IDE.
            	locationUrl += "localhost:8001";
            }
            else
            {
                locationUrl += documentUrl.getHost();
            }
        }
        
        Container p = this.getContentPane();
        p.setLayout(new BorderLayout());

        AmqpPanel amqpPanel = new AmqpPanel(locationUrl + "/amqp");
          
        p.add(amqpPanel, BorderLayout.CENTER);          
    }
}
