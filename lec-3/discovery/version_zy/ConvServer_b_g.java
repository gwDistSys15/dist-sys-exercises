/******************************************************************************
 *
 *  CS 6421 - Simple Conversation
 *  implement convertion between bananas and grams of potassium
 *  Compilation:  javac ConvServer_b_g.java
 *  Execution:    java ConvServer_b_g port
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
import java.lang.Double;
import java.net.InetAddress; 

public class ConvServer_b_g {
    static String DISCOV_IP = "127.0.0.1";
    static int DISCOV_PORT = 5555;
    static String l_ip;
    static int l_port;
    
    public static void send_to_discov(String msg){
        Socket sock = null;
        try {
            sock = new Socket(DISCOV_IP, DISCOV_PORT);    //get the socket and connet to the server
            PrintWriter out = new PrintWriter(sock.getOutputStream(), true);    //get printer
            out.println(msg);   //send messages
            out.close();
            sock.close();
        } catch(Exception e) {
            System.out.println("ERROR:"+e);
            System.exit(-1); 
        }
    }

    public static void process (Socket clientSocket) throws IOException {
        // open up IO streams
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        /* Write a welcome message to the client */
        out.println("Welcome to the Bananas (b) to Grams of Potassium (g) conversion server!");

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
        String[] arg = userInput.split(" ");
        if (arg.length != 3){
            out.println("pls input 3 arguements. Usage: eg. b g 2 or g b 2");
        }else if(!arg[0].equals("b") && !arg[0].equals("g") || !arg[1].equals("b") && !arg[1].equals("g")){
            out.println("Wrong input. Usage: eg. b g 2 or g b 2");
        }else{
            try{
                if(arg[0].equals("b")){
                    out.println(bToG(arg[2]));
                }
                if(arg[0].equals("g")){
                    out.println(arg[2]);
                }
            }catch(Exception e){
                out.println("wrong input, program ended.");
            }
        }
        // close IO streams, then socket
        out.close();
        in.close();
        clientSocket.close();
    }
    
    /**convert number of bananas to grams of potassium*/
    public static float bToG(String ui) throws Exception{
        float n = 0f;
       
        n = Float.valueOf(ui).floatValue();

        return n * 0.472f;
    }
    
    /**convert grams of potassium to number of bananas*/
    public static float gToB(String ui) throws Exception{
        float n = 0f;
        
        n = Float.valueOf(ui).floatValue();

        return n / 0.472f;
    }

    /*
    * hook thread
    */
    static class CleanWorkThread extends Thread{
        @Override
        public void run() {
            System.err.println("exiting..");
            send_to_discov("remove " + ConvServer_b_g.l_ip + " " + ConvServer_b_g.l_port);
        }
    }

    public static void main(String[] args) throws Exception {

        Runtime.getRuntime().addShutdownHook(new CleanWorkThread());
        
        //check if argument length is invalid
        if(args.length != 1) {
            System.err.println("Usage: java ConvServer port");
        }
        // create socket
        int port = Integer.parseInt(args[0]);
        ServerSocket serverSocket = new ServerSocket(port);
        System.err.println("Started server on port " + port);

        // get local ip
        InetAddress addr = InetAddress.getLocalHost();
        l_ip = addr.getHostAddress();
        l_port = port;
        send_to_discov("add b g " + l_ip + " " + l_port);
        // wait for connections, and process
        try {
            while(true) {
                // a "blocking" call which waits until a connection is requested
                Socket clientSocket = serverSocket.accept();
                System.err.println("\nAccepted connection from client");
                process(clientSocket);
            }

        }catch (Exception e) {
            System.out.println("ERROR:"+e);
        }
        System.exit(0);
    }
}
