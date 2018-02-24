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
import javax.json.JsonArrayBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.protobuf.ByteString;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import io.kless.KlessAPIGrpc;
import io.kless.KlessAPIGrpc.KlessAPIBlockingStub;
import io.kless.GetEventHandlersRequest;
import io.kless.EventHandlerInformation;
import io.kless.CreateEventHandlerRequest;
import io.kless.CreateEventHandlerReply;
import io.kless.DeleteEventHandlerRequest;
import io.kless.DeleteEventHandlerReply;


@Path("handler")
public class Handler {
	private static Logger log = Logger.getLogger(Handler.class.getName());

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
        		
            JsonObjectBuilder eventHandler = Json.createObjectBuilder();
            eventHandler.add("eventHandlerId", eventHandlerInformation.getEventHandlerId());
            eventHandler.add("eventHandlerName", eventHandlerInformation.getEventHandlerName());
            eventHandler.add("eventHandlerNamespace", eventHandlerInformation.getEventHandlerNamespace());
            eventHandler.add("eventHandlerVersion", eventHandlerInformation.getEventHandlerVersion());
            eventHandler.add("eventHandlerBuilder", eventHandlerInformation.getEventHandlerBuilder());
            eventHandler.add("eventHandlerBuilderURL", eventHandlerInformation.getEventHandlerBuilderURL());
            eventHandler.add("frontend", eventHandlerInformation.getFrontend());
            eventHandler.add("buildStatus", eventHandlerInformation.getBuildStatus());
            eventHandler.add("eventHandlerAvailable", eventHandlerInformation.getEventHandlerAvailable());
            eventHandler.add("comment", eventHandlerInformation.getComment());
            
            eventHandlerArray.add(eventHandler);
        }
        
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
	
	        String name = null;
	        String namespace = "kless";
	        String builder = null;
	        ByteString sourceCode = null;
	        String sourceCodeURL = null;
	        String version = "1.0";
	        String frontend = null;
	        ByteString dependencies = ByteString.EMPTY;
	        String dependenciesURL = "";
	        String comment = "";

	        try {
	        		name = requestJsonObject.getString("name");
	        } catch (Exception e) {
	        	
	        }
	        try {
	        		namespace = requestJsonObject.getString("namespace");
	        } catch (Exception e) {
	        	
	        }
	        try {
	        		builder = requestJsonObject.getString("builder");
	        } catch (Exception e) {
	        	
	        }
	        try {
	        		sourceCode = ByteString.copyFrom(requestJsonObject.getString("sourceCode"), "UTF-8");
	        } catch (Exception e) {
	        	
	        }
	        try {
	        		sourceCodeURL = null;
	        } catch (Exception e) {
	        	
	        }
	        try {
	        		version = requestJsonObject.getString("version");
	        } catch (Exception e) {
	        	
	        }
	        try {
	        		frontend = requestJsonObject.getString("frontend");
	        } catch (Exception e) {
	        	
	        }
	        try {
	        		dependencies = ByteString.copyFrom(requestJsonObject.getString("dependencies"), "UTF-8");
	        } catch (Exception e) {
	        	
	        }
	        try {
	        		dependenciesURL = null;
	        } catch (Exception e) {
	        	
	        }
	        try {
	        		comment = requestJsonObject.getString("comment");
	        } catch (Exception e) {
	        	
	        }
	        
	        log.info("name = " + name);
	        
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
	                                                                                  .setComment(comment)
	                                                                                  .build();
	        
	        CreateEventHandlerReply reply = stub.createEventHandler(request);
	    		
	        String response = reply.getResponse();
	        
	        channel.shutdown();
	        
	        log.info("Create event handler response = " + response);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
        
        return "{\"status\": \"ok\"}";
    }
    
    @DELETE
    @Path("/{handlerName}")
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteHandler(@PathParam("handlerName") String handlerName) throws Exception {
    		log.info("Entering deleteHandler()");
    		
    		log.info("Handler name: " + handlerName);
    		try {
    			
    	        ManagedChannel channel = ManagedChannelBuilder.forAddress(Main.serverHostname, Main.serverPortNumber)
    	                .usePlaintext(true)
    	                .build();
    	        
    	        KlessAPIBlockingStub stub = KlessAPIGrpc.newBlockingStub(channel);
    	        
    	        DeleteEventHandlerRequest request = DeleteEventHandlerRequest.newBuilder().setClientversion(Main.CLIENT_VERSION)
    	        		                                                                      .setEventHandlerName(handlerName)
    	        		                                                                      .setEventHandlerNamespace("kless")
    	        		                                                                      .build();
    	        
    	        DeleteEventHandlerReply reply = stub.deleteEventHandler(request);
    	        
    	        String response = reply.getResponse();
    	        
    	        channel.shutdown();
    	        
    	        log.info("Delete event handler response = " + response);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
        
        return "{\"status\": \"ok\"}";
    }
}
