package cn.moyada.sharingan.monitor.api.mbean;

import javax.management.*;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

/**
 * @author xueyikang
 * @since 1.0
 **/
public final class JXMUtil {

    private final static String RMI_PORT_PROP = "com.sun.management.jmxremote.rmi.port";
    private final static String JMX_PORT_PROP = "com.sun.management.jmxremote.port";
    private final static String APP_PORT_PROP = "sharingan.rmi.port";

    public final static void registerMBean(Object mbean, String domainName, String type) throws MalformedObjectNameException {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        addMBean(server, mbean,domainName + ":type=" + type );

        int rmiPort = getPort();
        if (rmiPort < 1024) {
            System.err.println(RMI_PORT_PROP +" error");
            return;
        }

        registerConnector(server, domainName, rmiPort);
    }

    private static int getPort() {
        String port = System.getProperty(RMI_PORT_PROP);
        if (null == port) {
            port = System.getProperty(APP_PORT_PROP);
        }
        int rmiPort;
        try {
            rmiPort = Integer.valueOf(port);
        } catch (NumberFormatException e) {
            rmiPort = -1;
        }
        return rmiPort;
    }

    private static void registerConnector(MBeanServer server, String domainName, int rmiPort) {
        Registry registry;
        try {
            registry = LocateRegistry.createRegistry(rmiPort);
        } catch (RemoteException e) {
            return;
        }

        try {
            JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:" + rmiPort + "/sharingan");
            JMXConnectorServer jmxConnector = JMXConnectorServerFactory.newJMXConnectorServer(url, null, server);
            jmxConnector.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addMBean(final MBeanServer beanServer, final Object mbean, String domainName) {
        try {
            final ObjectName objectName;
            try {
                objectName = ObjectName.getInstance(domainName);
            } catch (MalformedObjectNameException e) {
                e.printStackTrace();
                return;
            }
            AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() {
                public Void run() throws MBeanRegistrationException, NotCompliantMBeanException {
                    try {
                        if (beanServer.isRegistered(objectName)) {
                            beanServer.unregisterMBean(objectName);
                        }
                        beanServer.registerMBean(mbean, objectName);
                        return null;
                    } catch (InstanceAlreadyExistsException | InstanceNotFoundException var2) {
                        return null;
                    }
                }
            });
        } catch (PrivilegedActionException e) {
            e.printStackTrace();
        }
    }
}
