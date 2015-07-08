-   [Home](../../index.md)
-   [Documentation](../index.md)
-   Troubleshoot Your Java Client

Troubleshoot Your Java Client
=============================

**Note:** To use the Gateway, a KAAZING client library, or a KAAZING demo, fork the repository from [kaazing.org](http://kaazing.org).

This procedure provides troubleshooting information for the most common issue that occurs when using KAAZING Gateway Java clients.

Before You Begin
----------------

This procedure is part of [Build Java WebSocket Clients](o_dev_java.md):

1.  [Set Up Your Development Environment](p_dev_java_setup.md)
2.  [Use the Java WebSocket API](p_dev_java_websocket.md)
3.  [Use the Java EventSource API](p_dev_java_eventsource.md)
4.  [Migrate WebSocket and ByteSocket Applications to KAAZING Gateway 5.0](p_dev_java_migrate.md)
5.  [Secure Your Java and Android Clients](p_dev_java_secure.md)
6.  [Display Logs for the Java Client](p_dev_java_logging.md)
7.  **Troubleshoot Your Java Client**

What Problem Are You Having?
----------------------------

-   [Kerberos challenge handler not working](#kerberos-challenge-handler-not-working)

Kerberos challenge handler not working
-------------------------------------------------------------

**Cause:** [Kerberos challenge handlers](https://github.com/kaazing/java.client/blob/develop/ws/doc/p_dev_java_secure.md#creating-kerberos-challenge-handlers) might not work for one or more of the following reasons:

-   The client cannot connect to the Kerberos Domain Controller (KDC).

    **Solution:** Ping the KDC from the computer running the client and the server hosting the Gateway. Also, ensure that you can Telnet to Kerberos port number 88 from both computers (`telnet> open KDC-server-name 88`).

-   The client cannot obtain a Kerberos ticket.

    **Solution:** Test ticket acquisition by executing the following commands to ensure that the KDC is accessible and able to issue service tickets:

    **For Linux:**

    `$ kinit -t /etc/keytab-name.keytab -S service-instance-name username@KDC-server-name`

    **For Windows:**

    `$ kinit username@KDC-server-name`

    The output will be:

    `Please enter the password for username@KDC-server-name:`

    Enter the password, and then enter:

    `$ klist`

    The ticket cache is displayed along with each ticket's expiration date.

-   Service name is in the incorrect format in the Kerberos challenge handler code.

    **Solution:** The service name should be in the format: `HTTP/servergw.hostname.com`. See [Creating Kerberos Challenge Handlers](https://github.com/kaazing/java.client/blob/develop/ws/doc/p_dev_java_secure.md#creating-kerberos-challenge-handlers) for examples.

-   The pop-up dialog in the client used to obtain user credentials does not ensure that the username format is correct.

    **Solution:** Ensure that the result of the pop-up dialog used to obtain user credentials is formatted as
    `username@KDC-server-name`.

Next Step
---------

You have completed the Java client checklist. For more information on client API development, see the [Java Client API](../apidoc/client/java/gateway/index.md).


