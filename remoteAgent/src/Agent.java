import com.playgames.remote.SocketManager;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by quikr on 4/1/16.
 */
public class Agent {


    public static void main(String[] args){
        int port = 9000;

        if(args.length > 1){
            System.err.println("Invalid arguments. Usage : Agent <port>");
            System.exit(1);
        }
        try {
            port = Integer.parseInt(args[0]);
        }catch (NumberFormatException e){
            System.err.println("Invalid port number.");
        }
        SocketManager socketManager = null;
        try{
            socketManager = new SocketManager(port);
        }catch (IOException e){
            System.err.println("Error opening the socket " + e.getMessage());
        }
        try {
            socketManager.run();
        } catch (IOException e) {
            System.err.println("Error while running the command " + e.getMessage());
        }

    }
}
