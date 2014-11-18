/**
 * Copyright (c) 2007-2013, Kaazing Corporation. All rights reserved.
 */

package com.kaazing.net.ws.demo;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * Modal dialog for login. Captures user name and password.
 */
public class LoginDialog extends JDialog {
    private static final long serialVersionUID = 2140792415496774064L;
    private String username;
    private char[] password;
    private boolean canceled;
    
    public LoginDialog(Frame frame) {
        super(frame, true);
        initialize(frame);
    }

    /**
     * This method initializes this
     * 
     */
    private void initialize(Frame frame) {
        setTitle("Login");
        setSize(new Dimension(277, 137));

        JLabel usernameLabel = new JLabel("User Name");
        final JTextField usernameField = new JTextField(15);
        usernameField
                .setToolTipText("Enter the user name. For Kerberos credentials the user name should be the fully-qualified principal name of the form user@KERBEROSDOMAINNAME e.g.:joe@ATHENA.MIT.EDU.");
        usernameLabel.setLabelFor(usernameField);
        JPanel usernamePanel = new JPanel(new FlowLayout());
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password  ");
        final JPasswordField passwordField = new JPasswordField(15);
        passwordLabel.setLabelFor(passwordField);
        JPanel passwordPanel = new JPanel(new FlowLayout());
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);

        add(usernamePanel, BorderLayout.NORTH);
        add(passwordPanel, BorderLayout.CENTER);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                username = usernameField.getText();
                password = passwordField.getPassword();
                canceled = false;
                LoginDialog.this.setVisible(false);
            }
        });
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                canceled = true;
                LoginDialog.this.setVisible(false);
            }
        });
        JPanel buttonsPanel = new JPanel();
        final FlowLayout fl = new FlowLayout();
        buttonsPanel.setLayout(fl);
        fl.setAlignment(FlowLayout.CENTER);
        buttonsPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        buttonsPanel.add(okButton);
        buttonsPanel.add(cancelButton);
        add(buttonsPanel, BorderLayout.SOUTH);

        pack();
        if (frame != null) {
            Rectangle r = frame.getBounds();
            int x = r.x + (r.width - getSize().width) / 2;
            int y = r.y + (r.height - getSize().height) / 2;
            setLocation(x, y);
        }
        setVisible(true);
    }

    String getUsername() {
        return username;
    }

    char[] getPassword() {
        return password;
    }
    
    boolean isCanceled() {
        return canceled;
    }
} // @jve:decl-index=0:visual-constraint="0,0"
