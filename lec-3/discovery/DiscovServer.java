/******************************************************************************
 *
 *  CS 6421 - Discovery Server
 *  implement a discoverty server store adresses information
 *  @author guoluona
 *  Compilation:  javac DiscovServer.java
 *  Execution:    java DiscovServer
 *
 *  % java DiscovServer portnum
 ******************************************************************************/

package com.discovery.server;

import java.net.*;
import java.io.*;
import java.util.*;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class DiscovServer {
    static Multimap<String, String> discovTable = ArrayListMultimap.create();
    
    public static void add(String[] msg, PrintWriter out){
        if (msg.length != 5){
            System.out.println("Error input");
            out.println("Error input");
            out.println("Please input in format of <add unit1 unit2 ip port>");
            return;
        }
        
        String key = msg[1] + " " + msg[2];
        String value = msg[3] + " " + msg[4];
        Collection<String> values = discovTable.get(key);
        if(values.contains(value)){
        	out.println("This ip and port has already exist!");
        	System.out.println("This ip and port has already exist!");
        	return;
        }else{
	        discovTable.put(key, value);
	        System.out.println("Add successfully!");
	        //System.out.println("values = " + Multimap.get(key) + "n"); 
	        out.println("Add successfully!");
	        return;
        }
    }
    
    public static void remove(String[] msg, PrintWriter out){
        if (msg.length != 3){
            System.out.println("Error input");
            out.println("Error input");
            out.println("Please input in format of <remove unit3 unit4>");
            return;
        }
        
        String values = msg[1] + " " + msg[2];
        discovTable.values().remove(values);
        //System.out.println("the keys are:");
        System.out.println("Remove successfully!");
        out.println("Remove successfully!");
        return;
    }
    
    @SuppressWarnings("unused")
	public static void lookup(String[] msg, PrintWriter out){ 
        if (msg.length != 3){
            System.out.println("Error input");
            out.println("Error input");
            return;
        }
        
        String ipPort = msg[1] + " " + msg[2];
        if (ipPort == null){
            System.out.println("No such conversion exist!");
            out.println("No such conversion exist!");
            return;
        }else{
            out.println("Ip and port number is: " + discovTable.get(ipPort));
        }
        
        return;
    }
    
    public static void process (Socket clientSocket) throws IOException {
        // open up IO streams
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        /* Write a welcome message to the client */
        out.println("Welcome to the DiscovServer!");

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
        String[] msg = userInput.split(" ");
        try{
            switch(msg[0]){
                case "add":
                    add(msg, out);
                    break;
                case "remove":
                    remove(msg, out);
                    break;
                case "lookup":
                    lookup(msg, out);   
                    break;
                default:
                    System.out.println("Error input");
                    break;
            }
        }catch(Exception e){
            out.println("Error:" + e);
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
