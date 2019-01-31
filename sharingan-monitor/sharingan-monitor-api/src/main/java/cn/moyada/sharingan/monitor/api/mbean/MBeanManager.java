package cn.moyada.sharingan.monitor.api.mbean;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.remote.*;
import javax.management.remote.rmi.RMIConnectorServer;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;
import javax.security.auth.Subject;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.Security;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class MBeanManager {

    public static void main(String[] args) throws InterruptedException, MalformedObjectNameException, NotCompliantMBeanException, IOException {
//        ListenerMonitorMBean mBean = new ListenerMonitor("http://localhost:8081");
//        JXMUtil.registerMBean(mBean, "Sharingan", "Monitor");
//
////        DynamicMBean bean = new CustomImpl(new TestMonitor(), Monitor.class);
//        StandardMBeanSupport bean = new StandardMBeanSupport(new HhAitem(), Item.class);
//        JXMUtil.registerMBean(bean, "Sharingan", "bean");
//        Thread.currentThread().join();

        MBeanServer mbs = MBeanServerFactory.createMBeanServer();
        HashMap env = new HashMap();

        Security.addProvider(new com.sun.security.sasl.Provider());

        SslRMIClientSocketFactory csf =
                new SslRMIClientSocketFactory();
        SslRMIServerSocketFactory ssf =
                new SslRMIServerSocketFactory();
        env.put(RMIConnectorServer.
                RMI_CLIENT_SOCKET_FACTORY_ATTRIBUTE,csf);
        env.put(RMIConnectorServer.
                RMI_SERVER_SOCKET_FACTORY_ATTRIBUTE,ssf);
//
//        env.put("jmx.remote.profiles", "TLS SASL/PLAIN");
//        env.put("jmx.remote.x.access.file", "config" + File.separator + "access.properties");

        Registry registry;
        try {
            registry = LocateRegistry.createRegistry(9999);
        } catch (RemoteException e) {
            return;
        }

        JMXServiceURL url = new JMXServiceURL(
                "service:jmx:rmi:///jndi/rmi://127.0.0.1:9999/server");
        JMXConnectorServer cs =
                JMXConnectorServerFactory.newJMXConnectorServer(url,
                        env,
                        mbs);

//        JMXServiceURL url = new JMXServiceURL("rmi", "127.0.0.1", 5555);
////        JMXServiceURL url = new JMXServiceURL("jmxmp", "127.0.0.1", 5555);
////        JMXServiceURL url = new JMXServiceURL("rmi:///jndi/rmi", "127.0.0.1", 5555);
//        JMXConnectorServer cs = JMXConnectorServerFactory.newJMXConnectorServer(url, env, mbs);
        cs.start();
    }

    /**
     *  认证
     * @return
     */
    static class SimpleAuthenticator implements JMXAuthenticator {

        private final Map<String, Auth> authRole;

        public SimpleAuthenticator() {
            authRole = new HashMap<>();
        }

        public boolean addAuth(String username, String password) {
            return addAuth(username, password, true);
        }

        public boolean addAuth(String username, String password, boolean readOnly) {
            if (authRole.containsKey(username)) {
                return false;
            }
            authRole.put(username, new Auth(password, readOnly));
            return true;
        }

        public boolean removeAuth(String username) {
            return authRole.remove(username) != null;
        }

        @Override
        public Subject authenticate(Object credentials) {
            if (!(credentials instanceof String[])) {
                throw new SecurityException("Authentication failed!");
            }
            String[] credentialsInfo = (String[]) credentials;
            if (credentialsInfo.length != 2) {
                throw new SecurityException("Authentication failed!");
            }
            String userName = credentialsInfo[0];
            Auth auth = authRole.get(userName);
            if (auth == null) {
                throw new SecurityException("Invalid Authentication!");
            }
            String password = credentialsInfo[1];
            if (auth.password.equals(password)) {
                return new Subject(auth.readOnly, Collections.singleton(new JMXPrincipal(userName)), Collections.EMPTY_SET, Collections.EMPTY_SET);
            }
            throw new SecurityException("Invalid Authentication!");
        }

        class Auth {
            String password;
            boolean readOnly;

            Auth(String password, boolean readOnly) {
                this.password = password;
                this.readOnly = readOnly;
            }
        }
    }
}
