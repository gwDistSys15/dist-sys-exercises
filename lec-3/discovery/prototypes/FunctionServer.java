/******************************************************************************
 *
 *  CS 6421 - Simple Conversation
 *  Compilation:  javac ConvServer.java
 *  Execution:    java ConvServer port
 *
 *  % java ConvServer portnum
 ******************************************************************************/

import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class FunctionServer {

    private static String set(String input){
      return "set function called with message " + input;
    }

    private static String get(String input){
      return "get function called with message " + input;
    }
    
    public static void process (Socket clientSocket) throws IOException {
        // open up IO streams
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        String userInput;
        if ((userInput = in.readLine()) == null) {
            System.out.println("Error reading message");
            out.close();
            in.close();
            clientSocket.close();
        }

        String command = userInput.trim().toLowerCase().split(" ")[0];
        switch(command){
          case "set":
            out.println(set(userInput.substring(command.length()+1)));
            break;
          case "get":
            out.println(get(userInput.substring(command.length()+1)));
            break;
          default:
            out.println("Message not recognized : " + command); 
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
            System.err.println("Usage: java FunctionServer port");
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
