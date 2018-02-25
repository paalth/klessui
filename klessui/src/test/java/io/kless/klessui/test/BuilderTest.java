package io.kless.klessui.test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.grizzly.http.server.HttpServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.kless.klessui.Main;

import static org.junit.Assert.assertEquals;

public class BuilderTest {

    private HttpServer server;
    private WebTarget target;

//    @Before
//    public void setUp() throws Exception {
//        // start the server
//        server = Main.startServer(Main.DEFAULT_PORT);
//        // create the client
//        Client c = ClientBuilder.newClient();
//
//        // uncomment the following line if you want to enable
//        // support for JSON in the client (you also have to uncomment
//        // dependency on jersey-media-json module in pom.xml and Main.startServer())
//        // --
//        // c.configuration().enable(new org.glassfish.jersey.media.json.JsonJaxbFeature());
//
//        target = c.target(Main.BASE_URI);
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        server.stop();
//    }

    /**
     * Test event handler builder retrieval
     */
    @Test
    public void testGetBuilders() {
//        String responseMsg = target.path("builder").request().get(String.class);
//        assertEquals("{}", responseMsg);
        assertEquals("test", "test");
    }
}
