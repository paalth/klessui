package io.kless.klessui;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.kless.KlessAPIGrpc;
import io.kless.KlessAPIGrpc.KlessAPIBlockingStub;
import io.kless.DeleteEventHandlerReply;
import io.kless.DeleteEventHandlerRequest;
import io.kless.FrontendInformation;
import io.kless.GetEventHandlerFrontendsRequest;

@Path("frontend")
public class Frontend {
	private static Logger log = Logger.getLogger(Builder.class.getName());

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getFrontends() throws Exception {
        JsonArrayBuilder frontendArray = Json.createArrayBuilder();
        
        ManagedChannel channel = ManagedChannelBuilder.forAddress(Main.serverHostname, Main.serverPortNumber)
                .usePlaintext(true)
                .build();

        
        KlessAPIBlockingStub stub = KlessAPIGrpc.newBlockingStub(channel);

        GetEventHandlerFrontendsRequest request = GetEventHandlerFrontendsRequest.newBuilder().build();
        
        Iterator<FrontendInformation> responseIterator = stub.getEventHandlerFrontends(request);
        
        while (responseIterator.hasNext()) {
//        	  string eventHandlerFrontendName = 1;
//        	  string eventHandlerFrontendType = 2;
        	  
        		FrontendInformation frontendInformation = responseIterator.next();
        		log.info("Event handler frontend name = " + frontendInformation.getEventHandlerFrontendName());
        		
            JsonObjectBuilder frontendBuilder = Json.createObjectBuilder();
            frontendBuilder.add("eventHandlerFrontendName", frontendInformation.getEventHandlerFrontendName());
            frontendBuilder.add("eventHandlerFrontendType", frontendInformation.getEventHandlerFrontendType());
            frontendBuilder.add("comment", frontendInformation.getComment());
            
            frontendArray.add(frontendBuilder);
        }
        
        channel.shutdown();
        
        JsonObjectBuilder model = Json.createObjectBuilder();
        model.add("status", "OK");
        model.add("frontendInformation", frontendArray);
        
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
    public String createFrontend(String requestBody) throws Exception {
    		log.info("Entering createFrontend()");
    		
    		log.info("Request: " + requestBody);
    		
    		try {
    			
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
        
        return "{\"status\": \"ok\"}";
    }
    
    @DELETE
    @Path("/{frontendName}")
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteFrontend(@PathParam("frontendName") String frontendName) throws Exception {
    		log.info("Entering deleteHandler()");
    		
    		log.info("Frontend name: " + frontendName);
//    		try {
//    			
//    	        ManagedChannel channel = ManagedChannelBuilder.forAddress(Main.serverHostname, Main.serverPortNumber)
//    	                .usePlaintext(true)
//    	                .build();
//    	        
//    	        KlessAPIBlockingStub stub = KlessAPIGrpc.newBlockingStub(channel);
//    	        
//    	        DeleteEventHandlerRequest request = DeleteEventHandlerRequest.newBuilder().setClientversion(Main.CLIENT_VERSION)
//    	        		                                                                      .setEventHandlerName(handlerName)
//    	        		                                                                      .setEventHandlerNamespace("kless")
//    	        		                                                                      .build();
//    	        
//    	        DeleteEventHandlerReply reply = stub.deleteEventHandler(request);
//    	        
//    	        String response = reply.getResponse();
//    	        
//    	        channel.shutdown();
//    	        
//    	        log.info("Delete event handler response = " + response);
//    		} catch (Exception e) {
//    			e.printStackTrace();
//    		}
        
        return "{\"status\": \"ok\"}";
    }
}
