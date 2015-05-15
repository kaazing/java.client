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

import java.awt.BorderLayout;
import java.awt.Container;
import java.net.URL;

import javax.swing.JApplet;

public class AmqpApplet extends JApplet {

	private static final long serialVersionUID = -2872874862601616651L;
    
    public void init() {                 
//        URL documentUrl = getDocumentBase();
//        String locationUrl = "";
//        if (documentUrl.getProtocol().equalsIgnoreCase("https")) {
//            locationUrl = "wss://";
//        }
//        else {
//            locationUrl = "ws://";
//        }
//	
//        int port = documentUrl.getPort();
//        if (port > 0) {
//            locationUrl += documentUrl.getHost() + ":" + port;
//        }
//        else {
//            if (documentUrl.getHost().equals(""))
//            {
//            	// This is to deal with the case when we run from within IDE.
//            	locationUrl += "localhost:8001";
//            }
//            else
//            {
//                locationUrl += documentUrl.getHost();
//            }
//        }
//        AmqpPanel amqpPanel = new AmqpPanel(locationUrl + "/amqp");
        AmqpPanel amqpPanel = new AmqpPanel("ws://sandbox.kaazing.net:80/amqp091");
        
        this.setSize(900, 600);
        Container p = this.getContentPane();
        p.setLayout(new BorderLayout());

          
        p.add(amqpPanel, BorderLayout.CENTER);
    }
}
