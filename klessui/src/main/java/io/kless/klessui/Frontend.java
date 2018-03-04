package io.kless.klessui;

import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.io.StringReader;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonWriter;
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
import io.kless.CreateEventHandlerFrontendRequest;
import io.kless.CreateEventHandlerFrontendReply;
import io.kless.DeleteEventHandlerFrontendRequest;
import io.kless.DeleteEventHandlerFrontendReply;
import io.kless.FrontendInformation;
import io.kless.GetEventHandlerFrontendsRequest;
import io.kless.SubmitEventHandlerFrontendRequestRequest;
import io.kless.SubmitEventHandlerFrontendRequestReply;

@Path("frontend")
public class Frontend {
	private static Logger log = Logger.getLogger(Builder.class.getName());

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getFrontends() throws Exception {
		JsonArrayBuilder frontendArray = Json.createArrayBuilder();

		ManagedChannel channel = ManagedChannelBuilder.forAddress(Main.serverHostname, Main.serverPortNumber)
				.usePlaintext(true).build();

		KlessAPIBlockingStub stub = KlessAPIGrpc.newBlockingStub(channel);

		GetEventHandlerFrontendsRequest request = GetEventHandlerFrontendsRequest.newBuilder().build();

		Iterator<FrontendInformation> responseIterator = stub.getEventHandlerFrontends(request);

		while (responseIterator.hasNext()) {
			// string eventHandlerFrontendName = 1;
			// string eventHandlerFrontendType = 2;

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
	        JsonReader jsonReader = Json.createReader(new StringReader(requestBody));
	        JsonObject requestJsonObject = jsonReader.readObject();        
	
			String frontendName = "";
			String frontendType = "";
			String comment = "";

	        try {
	        		frontendName = requestJsonObject.getString("name");
	        } catch (Exception e) {
	        	
	        }
	        
	        try {
	        		frontendType = requestJsonObject.getString("type");
	        } catch (Exception e) {
	        	
	        }

	        try {
	        		comment = requestJsonObject.getString("comment");
	        } catch (Exception e) {
	        	
	        }
			
			ManagedChannel channel = ManagedChannelBuilder.forAddress(Main.serverHostname, Main.serverPortNumber)
                    .usePlaintext(true)
                    .build();

			KlessAPIBlockingStub stub = KlessAPIGrpc.newBlockingStub(channel);
			
			CreateEventHandlerFrontendRequest request = CreateEventHandlerFrontendRequest.newBuilder()
			                                                   .setClientversion(Main.CLIENT_VERSION)
			                                                   .setEventHandlerFrontendName(frontendName)
			                                                   .setEventHandlerFrontendType(frontendType)
			                                                   .setComment(comment)
			                                                   .build();
			
			CreateEventHandlerFrontendReply reply = stub.createEventHandlerFrontend(request);
			
			String response = reply.getResponse();
			
			log.info("Response: " + response);
			
			channel.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "{\"status\": \"ok\"}";
	}

	@POST
	@Path("/test/{frontendName}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String invokeFrontend(@PathParam("frontendName") String frontendName, String requestBody) throws Exception {
		log.info("Entering invokeFrontend()");

		log.info("Frontend name: " + frontendName);
		log.info("Request: " + requestBody);

		// Get frontend type here
		
		String status = "ERROR";
		String output = "Kless - error: request failed";
		
		try {			
			ManagedChannel channel = ManagedChannelBuilder.forAddress(Main.serverHostname, Main.serverPortNumber)
                    .usePlaintext(true)
                    .build();

			KlessAPIBlockingStub stub = KlessAPIGrpc.newBlockingStub(channel);
			
			SubmitEventHandlerFrontendRequestRequest request = SubmitEventHandlerFrontendRequestRequest.newBuilder()
			                                                   .setClientversion(Main.CLIENT_VERSION)
			                                                   .setEventHandlerFrontendName(frontendName)
			                                                   .setRequest(ByteString.copyFrom(requestBody, "UTF-8"))
			                                                   .build();
			
			SubmitEventHandlerFrontendRequestReply reply = stub.submitEventHandlerFrontendRequest(request);
			
			String response = new String(reply.getResponse().toByteArray());
			
			log.info("Response: " + response);
			
			status = reply.getStatus();
			
			output = response;
			
			channel.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}

		JsonObjectBuilder model = Json.createObjectBuilder();
		model.add("status", status);
		model.add("output", output);

		StringWriter stWriter = new StringWriter();
		JsonWriter jsonWriter = Json.createWriter(stWriter);
		jsonWriter.writeObject(model.build());
		jsonWriter.close();

		String jsonData = stWriter.toString();
		System.out.println(jsonData);

		log.info("Response: " + jsonData);

		return jsonData;
	}

	@DELETE
	@Path("/{frontendName}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteFrontend(@PathParam("frontendName") String frontendName) throws Exception {
		log.info("Entering deleteHandler()");

		log.info("Frontend name: " + frontendName);

		try {
			ManagedChannel channel = ManagedChannelBuilder.forAddress(Main.serverHostname, Main.serverPortNumber)
					                                      .usePlaintext(true)
					                                      .build();

			KlessAPIBlockingStub stub = KlessAPIGrpc.newBlockingStub(channel);

			DeleteEventHandlerFrontendRequest request = DeleteEventHandlerFrontendRequest.newBuilder()
					                                                                     .setClientversion(Main.CLIENT_VERSION)
					                                                                     .setEventHandlerFrontendName(frontendName)
					                                                                     .build();

			DeleteEventHandlerFrontendReply reply = stub.deleteEventHandlerFrontend(request);

			String response = reply.getResponse();

			channel.shutdown();

			log.info("Delete event handler response = " + response);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "{\"status\": \"ok\"}";
	}
}
