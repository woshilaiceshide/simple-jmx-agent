package woshilaiceshide.jmx.agent;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.util.HashMap;
import java.util.Map;

import javax.management.MBeanServer;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import javax.management.remote.rmi.RMIConnectorServer;

public class Agent {

	private Agent() {
	}

	private static RMIServerSocketFactory getRMIServerSocketFactory(InetAddress bindAddr) {
		return new PlainRMIServerSocketFactory(bindAddr);
	}

	public static String AGENT_LISTEN_ADDRESS = "jmx.agent.listen.address";

	public static InetAddress getAgentListenHost() {
		String raw = System.getProperty(AGENT_LISTEN_ADDRESS);
		InetAddress address = InetAddress.getLoopbackAddress();
		if (null != raw) {
			try {
				address = InetAddress.getByName(raw);
			} catch (java.net.UnknownHostException uhe) {

			} catch (java.lang.SecurityException se) {

			}

		}
		return address;
	}

	public static String AGENT_HOST = "java.rmi.server.hostname";

	public static String getAgentHost() throws UnknownHostException {
		return System.getProperty(AGENT_HOST, InetAddress.getLocalHost().getHostName());
	}

	public static String AGENT_PORT = "jmx.agent.port";
	public static int DEFAULT_AGENT_PORT = 7777;

	public static int getAgentPort() {
		String raw = System.getProperty(AGENT_PORT);
		int port = DEFAULT_AGENT_PORT;
		if (null != raw) {
			try {
				port = Integer.parseInt(raw);
			} catch (NumberFormatException nfe) {

			}
		}
		return port;
	}

	public static void premain(String agentArgs) throws IOException {

		System.setProperty("java.rmi.server.randomIDs", "true");
		Map<String, Object> env = new HashMap<String, Object>();

		final String host = getAgentHost();
		final int port = getAgentPort();

		final RMIClientSocketFactory csf = null;// getRMIClientSocketFactory();
		final RMIServerSocketFactory ssf = getRMIServerSocketFactory(getAgentListenHost());

		LocateRegistry.createRegistry(port, csf, ssf);

		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

		env.put(RMIConnectorServer.RMI_CLIENT_SOCKET_FACTORY_ATTRIBUTE, csf);
		env.put(RMIConnectorServer.RMI_SERVER_SOCKET_FACTORY_ATTRIBUTE, ssf);
		// com.sun.jndi.rmi.registry.RegistryContext.SOCKET_FACTORY
		env.put("com.sun.jndi.rmi.factory.socket", csf);
		// com.sun.jmx.remote.util.EnvHelp.JMX_SERVER_DAEMON
		env.put("jmx.remote.x.daemon", "true");

		JMXServiceURL url = new JMXServiceURL("service:jmx:rmi://" + host + ":" + port + "/jndi/rmi://" + host + ":" + port + "/jmxrmi");

		final JMXConnectorServer cs = JMXConnectorServerFactory.newJMXConnectorServer(url, env, mbs);

		System.out.println("starting jmx/rmi server with url: " + url);
		cs.start();
		System.out.println("Server started at: " + cs.getAddress());

		Runtime.getRuntime().addShutdownHook(new Thread() {

			@Override
			public void run() {
				try {
					cs.stop();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
}