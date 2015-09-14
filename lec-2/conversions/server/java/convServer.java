/******************************************************************************
 *  
 *  CS 6421 - Simple Conversation
 *  Compilation:  javac convServer.java
 *  Execution:    java convServer port
 *  Dependencies: In.java Out.java
 *
 *
 *  % java convServer portnum
 *
 *
 ******************************************************************************/

import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class convServer {
    
    public static void main(String[] args) throws Exception {
        
        //check if input length is invalid
        if(args.length != 1) {
            System.err.println("Usage: java convServer port");
        }
        // create socket
        int port = Integer.parseInt(args[0]);
        ServerSocket serverSocket = new ServerSocket(port);
        System.err.println("Started server on port " + port);
        
        // wait for connections, and process
        try {
            
            // a "blocking" call which waits until a connection is requested
            Socket clientSocket = serverSocket.accept();
            System.err.println("Accepted connection from client");
            
            // open up IO streams
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            
            // waits for data and reads it in until connection dies
            // readLine() blocks until the server receives a new line from client
            String userInput;
            if ((userInput = in.readLine()) == null) {
                out.close();
                in.close();
                clientSocket.close();
                System.exit(1);
            }
            
            System.out.println("Input Value: " + userInput);
            String msg = "output";
            //--TODO: please perform your processing here: msg = func(userInput)
            
            
            
            
            //msg: output at client side
            out.println(msg);
            // close IO streams, then socket
            System.err.println("Closing connection with client");
            out.close();
            in.close();
            clientSocket.close();
        }catch (IOException e) {
            System.err.println("Could not recognize valid host address");
        }
        System.exit(0);
    }
}
