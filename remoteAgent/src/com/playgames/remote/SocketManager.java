package com.playgames.remote;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by quikr on 4/1/16.
 */
public class SocketManager {

    private ServerSocket serverSocket;

    public SocketManager(int port) throws  IOException{
        serverSocket = new ServerSocket(port);
    }

    public void run() throws IOException {
        while(true){
            Socket server = null;
            DataInputStream in = null;
            DataOutputStream out = null;
            String output = null;
            try {
                server = serverSocket.accept();
                in = new DataInputStream(server.getInputStream());
                String message = in.readUTF();
                output = Shell.run(message);
                out = new DataOutputStream(server.getOutputStream());
            } catch (IOException e) {
                output = e.getMessage();
            } finally {
                if(server != null){
                    if (out != null && output != null) {
                        out.writeUTF(output);
                    }
                    server.close();
                }
            }

        }
    }




}
