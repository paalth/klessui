package io.kless.klessui;

import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

/**
 * Main class.
 *
 */
public class Main {
    public static final String CLIENT_VERSION = "kless-ui-0.0.1";
    
    public static final String ENV_KLESS_UI_PORT_NUMBER = "KLESS_UI_PORT";
    public static final String ENV_KLESS_UI_SERVER_HOSTNAME = "KLESS_UI_SERVER_HOSTNAME";
    public static final String ENV_KLESS_UI_SERVER_PORT_NUMBER = "KLESS_UI_SERVER_PORT";
    
    public static final int    DEFAULT_PORT = 7070;
    
    static String serverHostname = null;
    static int serverPortNumber = 0;
    
    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer(int portNumber) {
        // create a resource config that scans for JAX-RS resources and providers
        // in io.kless.klessui package
        final ResourceConfig rc = new ResourceConfig().packages("io.kless.klessui");

        final String baseURI = "http://0.0.0.0:" + portNumber + "/api/";
        
        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(baseURI), rc);
    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
    		int portNumber = DEFAULT_PORT;
    	
    		String portEnvVar = System.getenv(ENV_KLESS_UI_PORT_NUMBER);
    		if (null != portEnvVar) {
    			portNumber = Integer.parseInt(portEnvVar);
    		}
    		
        serverHostname = System.getenv(ENV_KLESS_UI_SERVER_HOSTNAME);
        serverPortNumber = Integer.parseInt(System.getenv(ENV_KLESS_UI_SERVER_PORT_NUMBER));

        System.out.println("Kless UI starting, listening on port = " + portNumber + ". Using Kless server = " + serverHostname + ":" + serverPortNumber + ".");

    		final HttpServer server = startServer(portNumber);
        
        HttpHandler httpHandler = new CLStaticHttpHandler(HttpServer.class.getClassLoader(), "/static/");
        server.getServerConfiguration().addHttpHandler(httpHandler, "/");

        System.out.println("Kless UI started");
                
        while (true) {
        		System.in.read();
        }
        
        //server.stop();
    }
}

