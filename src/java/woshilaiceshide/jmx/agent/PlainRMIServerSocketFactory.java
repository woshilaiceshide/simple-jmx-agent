package woshilaiceshide.jmx.agent;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.rmi.server.RMIServerSocketFactory;

public class PlainRMIServerSocketFactory implements RMIServerSocketFactory {

	final private InetAddress bindAddr;

	public PlainRMIServerSocketFactory(InetAddress bindAddr) {
		this.bindAddr = bindAddr;
	}

	public ServerSocket createServerSocket(int port) throws IOException {
		return new ServerSocket(port, 8, bindAddr);
	}
}
