package actors;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.playgames.dto.request.ShellCommandRequest;
import com.playgames.dto.response.ErrorResponse;
import com.playgames.dto.response.ShellCommandResponse;

import play.Logger;
import play.Play;
import akka.actor.UntypedActor;
/**
 * Created by quikr on 4/1/16.
 */
public class RemoteAgentClient extends UntypedActor {

	@Override
	public void onReceive(Object message) throws Exception {
		

		if (message != null && message instanceof ShellCommandRequest) {
			//Translate input message to POJO
			ShellCommandRequest requestObject = ( ShellCommandRequest )message;
			
			try{
				ShellCommandResponse response = null;	
				//Open socket connection with remote agent
				Socket remoteAgentSocket = new Socket(requestObject.getTarget(), Play.application().configuration().getInt("port."+requestObject.getTarget()));
				
				//Write the command to be executed to the socket stream
				OutputStream outToRemoteAgent = remoteAgentSocket.getOutputStream();
				DataOutputStream out = new DataOutputStream(outToRemoteAgent);
				out.writeUTF(requestObject.getCommand());
				
				//Read the output from the remote agent.
				InputStream inFromRemoteAgent = remoteAgentSocket.getInputStream();
				DataInputStream in = new DataInputStream(inFromRemoteAgent);
				String result = in.readUTF();
				
				//close the socket connection
				remoteAgentSocket.close();
				
				//return response as POJO
				response = new ShellCommandResponse();
				response.setOutput(result);	
				response.setStatus("SUCCESS");
				getSender().tell( response, getSelf() );
			} catch (IOException e){
				ErrorResponse errorResponse = new ErrorResponse();
				errorResponse.setStatus("FAILURE");
				errorResponse.setError(e.getMessage());
				getSender().tell( errorResponse, getSelf() );
			}
			
		}
		Logger.error("Invalid request format");
		unhandled(message);
		
	}
	
}
