package io.kless.klessui;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.kless.KlessAPIGrpc;
import io.kless.KlessAPIGrpc.KlessAPIBlockingStub;
import io.kless.Klessserver.EventHandlerBuilderInformation;
import io.kless.Klessserver.GetEventHandlerBuildersRequest;

@Path("builder")
public class Builder {
	private static Logger log = Logger.getLogger(Builder.class.getName());

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getBuilders() throws Exception {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(Main.serverHostname, Main.serverPortNumber)
                .usePlaintext(true)
                .build();
        
        JsonArrayBuilder builderArray = Json.createArrayBuilder();
        
        KlessAPIBlockingStub stub = KlessAPIGrpc.newBlockingStub(channel);

        GetEventHandlerBuildersRequest request = GetEventHandlerBuildersRequest.newBuilder().build();
        
        Iterator<EventHandlerBuilderInformation> responseIterator = stub.getEventHandlerBuilders(request);
        
        while (responseIterator.hasNext()) {
        	EventHandlerBuilderInformation eventHandlerBuilderInformation = responseIterator.next();
        		log.info("Event handler builder name = " + eventHandlerBuilderInformation.getEventHandlerBuilderName());
        		
//			string eventHandlerBuilderName = 1;
//			string eventHandlerBuilderURL = 2;
        		  
            JsonObjectBuilder eventHandlerBuilder = Json.createObjectBuilder();
            eventHandlerBuilder.add("eventHandlerBuilderName", eventHandlerBuilderInformation.getEventHandlerBuilderName());
            eventHandlerBuilder.add("eventHandlerBuilderURL", eventHandlerBuilderInformation.getEventHandlerBuilderURL());
            
            builderArray.add(eventHandlerBuilder);
        }
        
        channel.shutdown();
        
        JsonObjectBuilder model = Json.createObjectBuilder();
        model.add("status", "OK");
        model.add("eventHandlerBuilderInformation", builderArray);
        
        StringWriter stWriter = new StringWriter();
        JsonWriter jsonWriter = Json.createWriter(stWriter);
        jsonWriter.writeObject(model.build());
        jsonWriter.close();
         
        String jsonData = stWriter.toString();
        System.out.println(jsonData);
        
        log.info("Response: " + jsonData);
        
        return jsonData;
    }
}
