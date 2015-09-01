# simple-jmx-agent
In the Java HotSpot Virtual Machine, serveral ports are opened when using the out-of-box remote jmx, and some of these ports are dynamic, which make the firewall very sad.

The following shows this painful thing:

	java \
		 -Djava.net.preferIPv4Stack=true \
		 -Dcom.sun.management.jmxremote=true \
		 -Dcom.sun.management.jmxremote.port=${port} \
		 -Dcom.sun.management.jmxremote.authenticate=false \
		 -Dcom.sun.management.jmxremote.ssl=false \
		 -classpath ${classpath} \
		 ${main_class}

This agent provides a jmx server with a single fixed port. 

## How to Build It?
* git clone https://github.com/woshilaiceshide/simple-jmx-agent.git
* cd ./simple-jmx-agent
* sbt package

## How to Use It?
You can use it as following: 

	java \
		 -javaagent:/path/to/simple-jmx-agent-${version}.jar \
		 -Djava.rmi.server.hostname=app1.host1.com \
		 -Djmx.agent.listen.address=0.0.0.0 \
		 -Djmx.agent.port=${port} \
		 -Djava.rmi.server.logCalls=true \
		 -classpath ${classpath} \
		 ${main_class}

When connecting to the jmx server, use the following url: 


	service:jmx:rmi://app1.host1.com:${port}/jndi/rmi://app1.host1.com:${port}/jmxrmi


## How to Use It with sbt-native-packager?
Add the following into build.sbt: 

	libraryDependencies += "woshilaiceshide" % "simple-jmx-agent" % "1.1"
	
	bashScriptExtraDefines += """addJava "-javaagent:${lib_dir}/woshilaiceshide.simple-jmx-agent-1.1.jar""""
	bashScriptExtraDefines += """addJava "-Djava.rmi.server.hostname=app1.host1.com""""
	bashScriptExtraDefines += """addJava "-Djmx.agent.listen.address=0.0.0.0""""
	bashScriptExtraDefines += """addJava "-Djmx.agent.port=7777""""

Maybe the repository https://dl.bintray.com/woshilaiceshide/maven should be added.

## Key Options in This Agent
1.
**java.rmi.server.hostname**

This is the NAT-ed address of your server, or the public domain of your server.

2.
**jmx.agent.listen.address**

The jmx server will listen on this address, such as 127.0.0.1, or 192.168.1.1. This is the address printed on your machine's interface.

3.
**jmx.agent.port**

The jmx server will listen on this port.


## References
* http://docs.oracle.com/javase/tutorial/rmi/index.html
* http://docs.oracle.com/javase/tutorial/jmx/
* http://docs.oracle.com/javase/8/docs/technotes/guides/jmx/
* http://docs.oracle.com/javase/8/docs/technotes/guides/jmx/overview/JMXoverviewTOC.html
* http://docs.oracle.com/javase/8/docs/technotes/guides/management/agent.html
* http://docs.oracle.com/javase/8/docs/technotes/guides/jndi/jndi-rmi.html
* http://docs.oracle.com/javase/8/docs/api/javax/management/remote/rmi/RMIConnection.html

## Some Key Source
1. 
javax.management.remote.rmi.RMIJRMPServerImpl.makeClient(String, Subject)
2. 
javax.management.remote.rmi.RMIConnectorServer.start()
3. 
sun.management.jmxremote.ConnectorBootstrap.startRemoteConnectorServer(String portStr, Properties props)

## License ##
http://opensource.org/licenses/MIT

## Enjoy It
Any feedback is expected.


