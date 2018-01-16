package io.kless.klessui;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.protobuf.ByteString;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import io.kless.KlessAPIGrpc;
import io.kless.KlessAPIGrpc.KlessAPIBlockingStub;
import io.kless.Klessserver.GetServerVersionReply;
import io.kless.Klessserver.GetServerVersionRequest;
import io.kless.Klessserver.GetEventHandlersRequest;
import io.kless.Klessserver.EventHandlerInformation;
import io.kless.Klessserver.CreateEventHandlerRequest;
import io.kless.Klessserver.CreateEventHandlerReply;

@Path("handler")
public class Handler {
	private static Logger log = Logger.getLogger(Handler.class.getName());

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getHandlers() throws Exception {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(Main.serverHostname, Main.serverPortNumber)
                .usePlaintext(true)
                .build();
        
        JsonArrayBuilder eventHandlerArray = Json.createArrayBuilder();
        
        KlessAPIBlockingStub stub = KlessAPIGrpc.newBlockingStub(channel);

        GetEventHandlersRequest request = GetEventHandlersRequest.newBuilder().build();
        
        Iterator<EventHandlerInformation> responseIterator = stub.getEventHandlers(request);
        
        while (responseIterator.hasNext()) {
        		EventHandlerInformation eventHandlerInformation = responseIterator.next();
        		log.info("Event handler name = " + eventHandlerInformation.getEventHandlerName());
        		
//        		string eventHandlerId = 1;
//        		string eventHandlerName = 2;
//        		string eventHandlerNamespace = 3;
//        		string eventHandlerVersion = 4;
//        		string eventHandlerBuilder  = 5;
//        		string eventHandlerBuilderURL  = 6;
//        		string frontend = 7;
        		
            JsonObjectBuilder eventHandler = Json.createObjectBuilder();
            eventHandler.add("eventHandlerId", eventHandlerInformation.getEventHandlerId());
            eventHandler.add("eventHandlerName", eventHandlerInformation.getEventHandlerName());
            eventHandler.add("eventHandlerNamespace", eventHandlerInformation.getEventHandlerNamespace());
            eventHandler.add("eventHandlerVersion", eventHandlerInformation.getEventHandlerVersion());
            eventHandler.add("eventHandlerBuilder", eventHandlerInformation.getEventHandlerBuilder());
            eventHandler.add("eventHandlerBuilderURL", eventHandlerInformation.getEventHandlerBuilderURL());
            eventHandler.add("frontend", eventHandlerInformation.getFrontend());
            
            eventHandlerArray.add(eventHandler);
        }
        
//        GetServerVersionRequest request = GetServerVersionRequest.newBuilder().setClientversion(Main.CLIENT_VERSION).build();
//
//        GetServerVersionReply reply = stub.getServerVersion(request);
//        
//        String serverVersion = reply.getServerversion();
//        
//        log.info("Server version = " + serverVersion);
        
        channel.shutdown();
        
        JsonObjectBuilder model = Json.createObjectBuilder();
        model.add("status", "OK");
        model.add("eventHandlerInformation", eventHandlerArray);
        
        StringWriter stWriter = new StringWriter();
        JsonWriter jsonWriter = Json.createWriter(stWriter);
        jsonWriter.writeObject(model.build());
        jsonWriter.close();
         
        String jsonData = stWriter.toString();
        System.out.println(jsonData);
        
        log.info("Response: " + jsonData);
        
        return jsonData;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String createHandler(String requestBody) throws Exception {
    		log.info("Entering createHandler()");
    		
    		log.info("Request: " + requestBody);
        
    		try {
	        JsonReader jsonReader = Json.createReader(new StringReader(requestBody));
	        JsonObject requestJsonObject = jsonReader.readObject();        
	
	        String name = requestJsonObject.getString("name");
	        String namespace = requestJsonObject.getString("namespace");
	        String builder = requestJsonObject.getString("builder");
	        ByteString sourceCode = ByteString.copyFrom(requestJsonObject.getString("sourceCode"), "UTF-8");
	        String sourceCodeURL = null;
	        String version = requestJsonObject.getString("version");
	        String frontend = requestJsonObject.getString("frontend");
	        ByteString dependencies = ByteString.copyFrom(requestJsonObject.getString("dependencies"), "UTF-8");
	        String dependenciesURL = null;
	                
	        log.info("name = " + name);
	        
	//        string clientversion = 1;
	//        string eventHandlerName = 2;
	//        string eventHandlerNamespace = 3;
	//        string eventHandlerBuilder = 4;
	//        bytes  eventHandlerSourceCode = 5;
	//        string eventHandlerSourceCodeURL = 6;
	//        string eventHandlerVersion = 7;
	//        string eventHandlerFrontend = 8;
	//        bytes  eventHandlerDependencies = 9;
	//        string eventHandlerDependenciesURL = 10;
			
	        ManagedChannel channel = ManagedChannelBuilder.forAddress(Main.serverHostname, Main.serverPortNumber)
	                .usePlaintext(true)
	                .build();
	        
	        KlessAPIBlockingStub stub = KlessAPIGrpc.newBlockingStub(channel);
	        
	        CreateEventHandlerRequest request = CreateEventHandlerRequest.newBuilder().setClientversion(Main.CLIENT_VERSION)
	        		                                                                      .setEventHandlerName(name)
	        		                                                                      .setEventHandlerNamespace(namespace)
	                                                                                  .setEventHandlerBuilder(builder)
	                                                                                  .setEventHandlerSourceCode(sourceCode)
	                                                                                  .setEventHandlerVersion(version)
	                                                                                  .setEventHandlerFrontend(frontend)
	                                                                                  .setEventHandlerDependencies(dependencies)
	                                                                                  .build();
	        
	        CreateEventHandlerReply reply = stub.createEventHandler(request);
	    		
	        String response = reply.getResponse();
	        
	        log.info("Create event handler response = " + response);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
        
        return "{\"status\": \"ok\"}";
    }
    
}
