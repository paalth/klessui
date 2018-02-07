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
}
