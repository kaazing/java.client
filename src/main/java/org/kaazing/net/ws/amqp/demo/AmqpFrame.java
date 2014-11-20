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
