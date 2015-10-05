/******************************************************************************
 *
 *  CS 6421 - Single Value Server -- adapted from ConvServer
 *  Compilation:  javac SingleValueServer.java
 *  Execution:    java SingleValueServer port
 *
 *  % java SingleValueServer portnum
 ******************************************************************************/

import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;

public class SingleValueServer {
    
    public static Hashtable<String,ArrayList<String>> value = new Hashtable<String,ArrayList<String>>(); 
    

    public static void process (Socket clientSocket) throws IOException {
        // open up IO streams
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        /* Write a welcome message to the client */
        out.println("Welcome, you are connected to a Java-based SingleValueServer");

        /* read and print the client's request */
        // readLine() blocks until the server receives a new line from client
        String userInput;
        if ((userInput = in.readLine()) == null) {
            System.out.println("Error reading message");
            out.close();
            in.close();
            clientSocket.close();
        }

        System.out.println("Received message: " + userInput);
        
        String[] input = userInput.split(" ");
        
        // set unit1 unit2 IP Port 
        
        // key is unit1 unit 2
        //value is IP PORT 
        
        
        if(input.length == 5){
        	ArrayList<String> current = value.get(input[1] + " " + input[2]);
        	
        	if(current == null) current = new ArrayList<String>(); 
        	
        	current.add(input[3] + " " + input[4]);
        	
        	value.put(input[1] + " " + input[2], current);
        	
        	value.put(input[2] + " " + input[1], current);
        	
        	System.out.println("Discovery Table is now: " + value.toString());
        	
        }
        
        
      
        // close IO streams, then socket
        out.close();
        in.close();
        clientSocket.close();
    }

    public static void main(String[] args) throws Exception {

        //check if argument length is invalid
        if(args.length != 1) {
            System.err.println("Usage: java SingleValueServer port");
            System.exit(0);
        }
        // create socket
        int port = Integer.parseInt(args[0]);
        ServerSocket serverSocket = new ServerSocket(port);
        System.err.println("Started server on port " + port);

        // wait for connections, and process
        try {
            while(true) {
                // a "blocking" call which waits until a connection is requested
                Socket clientSocket = serverSocket.accept();
                System.err.println("\nAccepted connection from client");
                process(clientSocket);
            }

        }catch (IOException e) {
            System.err.println("Connection Error");
        }
       System.exit(0);
    }
}
