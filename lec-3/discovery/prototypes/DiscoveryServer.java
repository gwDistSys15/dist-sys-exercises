/******************************************************************************
 *
 *  CS 6421 - Simple Conversation
 *  Compilation:  javac DiscoveryServer.java
 *  Execution:    java DiscoverServer port
 *
 *  % java DiscoveryServer portnum -- based on given ConvServer file
 ******************************************************************************/

import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Random;

public class DiscoveryServer {
    
    private static  PrintWriter out;
    
    private static Hashtable<String, LinkedList<String>> convTable =
    new Hashtable<String, LinkedList<String>>();
    
    private static Random r = new Random(System.currentTimeMillis());
    
    private static boolean set(String unit1, String unit2, String ip, String port){
   
        LinkedList<String> current = convTable.get(unit1 + " " + unit2);
        if(current == null){
            // if there is no lists of ips and ports create it
            current = new LinkedList<String>();
        }
        else if(current.indexOf(ip + " " + port) != -1){
            // if this exact entry already exists fail
            return false;
        }
        
        current.add(ip + " " + port);
        
       // out.println("Num Servers: " + current.size());
        
        convTable.put(unit1 + " " + unit2, current);
        convTable.put(unit2 + " " + unit1, current);
        
        return true;
    }
    
    /*
     Goes into the LinkedList values and memoves any that have this IP and Port, returning true if it removes
     at least one and returning false if there are none
     */
    private static boolean remove(String ipAndPort){
        boolean hasRemoved = false;
        for(LinkedList l : convTable.values()){
            if(l.contains(ipAndPort)){
                //out.println(l.toString());
                l.remove(ipAndPort);
               // out.println(l.toString());
                hasRemoved = true;
            }
        }
        return hasRemoved;
    }
    
    private static String get(String unit1, String unit2){
        LinkedList<String> servers = convTable.get(unit1 + " " + unit2);
        if(servers != null && servers.size() > 0)
            return servers.get(r.nextInt(servers.size()));
        else
            return "No registered servers";
    }
    
    public static void process (Socket clientSocket) throws IOException {
        // open up IO streams
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        
        String userInput;
        if ((userInput = in.readLine()) == null) {
            System.out.println("Error reading message");
            out.close();
            in.close();
            clientSocket.close();
        }
        
        String tokens[] = userInput.trim().toLowerCase().split(" ");
        switch(tokens[0]){
            case "add":
                if(tokens.length != 5){
                    out.println("Invalid add command. Usage: add unit1 unit2 ip port");
                    break;
                }
                if(set(tokens[1], tokens[2], tokens[3], tokens[4]))
                    out.println("SUCCESS");
                else
                    out.println("FAILURE EXISTS");
                break;
            case "lookup":
                out.println(get(tokens[1], tokens[2]));
                break;
            case "remove":
                if(remove(tokens[1] + " " + tokens[2])) out.println("SUCCESS");
                else out.println("FAILURE");
                break;
            default:
                out.println("Message not recognized : " + tokens[0]);
                break;
        }
        
        // close IO streams, then socket
        out.close();
        in.close();
        clientSocket.close();
    }
    
    public static void main(String[] args) throws Exception {
        
        //check if argument length is invalid
        if(args.length != 1) {
            System.err.println("Usage: java DiscoveryServer port");
            System.exit(-1);
        }
        
        /* TODO uncomment this block
         String myIP = "161.253.119.173";
         String discoveryServerIP = "127.0.0.1";
         int discoveryServerPort = 1111;
         
         Socket discSock;
         PrintWriter discOut;
         BufferedReader discIn;
         try{
         discSock = new Socket(discoveryServerIP, discoveryServerPort);
         discOut = new PrintWriter(discSock.getOutputStream(),true);
         discIn = new BufferedReader(new InputStreamReader(discSock.getInputStream()));
         }
         catch(Exception e){
         e.printStackTrace();
         System.exit(-1);
         }
         */
        //TODO send message to discovery server
        
        // create socket
        int port = Integer.parseInt(args[0]);
        ServerSocket serverSocket = new ServerSocket(port);
        System.err.println("Started DiscoveryServer on port " + port);
        
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
