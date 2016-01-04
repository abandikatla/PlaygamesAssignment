package controllers;

import actors.RemoteAgentClient;
import akka.actor.ActorRef;
import akka.actor.Props;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.playgames.dto.request.ShellCommandRequest;
import com.playgames.dto.response.ErrorResponse;
import com.playgames.dto.response.ShellCommandResponse;

import play.Logger;
import play.libs.Akka;
import play.libs.F;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import static akka.pattern.Patterns.ask;

public class Application extends Controller {

    public static Promise<Result> executeCommand() {
        
    	//Create an actor - asynchronous executor
    	ActorRef remoteAgentInstance = Akka.system().actorOf(
                Props.create(RemoteAgentClient.class).withDispatcher(
                		"play.akka.actor.contexts.shell-context"));
        
        ShellCommandRequest request = null;
        //Get the json request
        JsonNode json = request().body().asJson();
        
        //Convert to POJO
        ObjectMapper objectMapper = new ObjectMapper();
        try {
        	request = objectMapper.treeToValue(json, ShellCommandRequest.class);
        }  catch (JsonProcessingException e) {
            e.printStackTrace();
            Logger.error("Unable to convert to request " + json.asText());
        }
        
        //Validate the request
        String errorDescription = null;
    	if(json == null){
            errorDescription = "No input json in the request";
        } else if(request.getTarget() == null ) {
        	errorDescription = "Missing target in the request";
        } else if(request.getCommand() == null ) {
        	errorDescription = "Missing command in the request";
        }
    	
    	//Return error response
    	if(errorDescription != null){
	    	final ErrorResponse errorResponse = new ErrorResponse();
	    	errorResponse.setStatus("FAILURE");
	    	errorResponse.setError(errorDescription);
	    	
    		return F.Promise.promise(new F.Function0<Result>() {
                @Override
                public Result apply() throws Throwable {
                	 ObjectMapper mapper = new ObjectMapper();
                	 JsonNode jsonResponse = mapper
                             .valueToTree((ErrorResponse) errorResponse);
                	  return badRequest(Json.stringify(jsonResponse)); //HTTP code 400
                }
            });
    	}
        
    	//Invoke the Remote Agent asynchronously
     
        return Promise.wrap(
                ask(remoteAgentInstance, request, 300000))
                .map(new Function<Object, Result>() {
                    public Result apply(Object response) {
                        if (response != null) {
                        	if(response instanceof ErrorResponse){
                        		ObjectMapper mapper = new ObjectMapper();
	                            JsonNode json = mapper
	                                    .valueToTree((ErrorResponse) response);
	                            Logger.info(Json.stringify(json));
	                            return internalServerError(Json.stringify(json)); //HTTP code 500
                        	}else { 
	                            ObjectMapper mapper = new ObjectMapper();
	                            JsonNode json = mapper
	                                    .valueToTree((ShellCommandResponse) response);
	                            Logger.info(Json.stringify(json));
	                            return ok(Json.stringify(json)); ////HTTP code 200
                        	}
                        }
                        return ok("{}");
                    }
                });
    }

}
