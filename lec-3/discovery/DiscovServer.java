/******************************************************************************
 *
 *  CS 6421 - Discovery Server
 *  implement a discoverty server store adresses information
 * 
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
    	//check if msg valuse is invalid
        if (msg.length != 5){
            System.out.println("Error input");
            out.println("Error input");
            out.println("Please input in format of <add unit1 unit2 ip port>");
            return;
        }
        
        String key = msg[1] + " " + msg[2];
        String value = msg[3] + " " + msg[4];
        Collection<String> values = discovTable.get(key);
        //check if the ip and port has already exist
        if(values.contains(value)){
        	out.println("This ip and port has already exist!");
        	System.out.println("This ip and port has already exist!");
        	return;
        }else{
        	//add this path to discovery table
	        discovTable.put(key, value);
	        System.out.println("Add successfully!");
	        out.println("Add successfully!");
	        return;
        }
    }
    
    public static void remove(String[] msg, PrintWriter out){
    	//check if msg values is invalid
        if (msg.length != 3){
            System.out.println("Error input");
            out.println("Error input");
            out.println("Please input in format of <remove unit3 unit4>");
            return;
        }
        String values = msg[1] + " " + msg[2];
        //check if the ip and port exist in the discovery table
        if(!discovTable.values().contains(values)){
        	out.println("This ip and port doesn't exist!");
        	System.out.println("This ip and port doesn't exist!");
        	return;
        }else{
        	//if exist, remove ip and port from discovery table
	        discovTable.values().remove(values);
	        System.out.println("Remove successfully!");
	        out.println("Remove successfully!");
	        return;
        }
    }
    
    @SuppressWarnings("unused")
	public static void lookup(String[] msg, PrintWriter out){ 
	//check if msg values is invalid
        if (msg.length != 3){
            System.out.println("Error input");
            out.println("Error input");
            return;
        }
        
        String ipPort = msg[1] + " " + msg[2];
        //check if there is such ip and port exit
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
        msg[0] = msg[0].toUpperCase();
        try{
            switch(msg[0]){
                case "ADD":
                    add(msg, out);
                    break;
                case "REMOVE":
                    remove(msg, out);
                    break;
                case "LOOKUP":
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
