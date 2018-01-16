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
import io.kless.Klessserver.FrontendTypeInformation;
import io.kless.Klessserver.GetEventHandlerFrontendTypesRequest;

@Path("frontendtype")
public class FrontendType {
	private static Logger log = Logger.getLogger(Builder.class.getName());

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getFrontendTypes() throws Exception {
    		JsonArrayBuilder frontendTypesArray = Json.createArrayBuilder();
    	
        ManagedChannel channel = ManagedChannelBuilder.forAddress(Main.serverHostname, Main.serverPortNumber)
                .usePlaintext(true)
                .build();
        
        KlessAPIBlockingStub stub = KlessAPIGrpc.newBlockingStub(channel);

        GetEventHandlerFrontendTypesRequest request = GetEventHandlerFrontendTypesRequest.newBuilder().build();
        
        Iterator<FrontendTypeInformation> responseIterator = stub.getEventHandlerFrontendTypes(request);
        
        while (responseIterator.hasNext()) {
        		FrontendTypeInformation frontendTypeInformation = responseIterator.next();
        		log.info("Frontend type = " + frontendTypeInformation.getEventHandlerFrontendType());
        		
//			  string eventHandlerFrontendType = 1;
//			  string eventHandlerFrontendTypeURL = 2;
        		
            JsonObjectBuilder frontendTypeBuilder = Json.createObjectBuilder();
            frontendTypeBuilder.add("eventHandlerFrontendType", frontendTypeInformation.getEventHandlerFrontendType());
            frontendTypeBuilder.add("eventHandlerFrontendTypeURL", frontendTypeInformation.getEventHandlerFrontendTypeURL());
            
            frontendTypesArray.add(frontendTypeBuilder);
        }
        
        channel.shutdown();
        
        JsonObjectBuilder model = Json.createObjectBuilder();
        model.add("status", "OK");
        model.add("frontendTypeInformation", frontendTypesArray);
        
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
