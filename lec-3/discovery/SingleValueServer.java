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

public class SingleValueServer {
    
    public static String value = "";
    

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
        //--TODO: add your converting functions here, msg = func(userInput);
        
        String[] input = userInput.split(" ");
        
        
        
        
        if(input.length != 2 && input.length > 0){
            if(input[0].equals("get")) out.println(value);
            else out.println("Sorry, we can't understand that command");
        } else {
            if(!input[0].equals("set") && !input[0].equals("get")){
                out.println("Sorry, we can only accept set <value> or get <value>");
            } else {
                if(input[0].equals("set")){
                    value = input[1];
                    out.println("Value Saved!");
                } else {
                    out.println("Sorry, we can only accept set <value> or get");
                }
            }
        }

        // close IO streams, then socket
        out.close();
        in.close();
        clientSocket.close();
    }

    public static void main(String[] args) throws Exception {

        //check if argument length is invalid
        if(args.length != 1) {
            System.err.println("Usage: java ConvServer port");
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
