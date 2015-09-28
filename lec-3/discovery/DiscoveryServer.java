

import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator; 
import java.util.Map; 

public class DiscoveryServer {
    
    public static HashMap<String,String> table = new HashMap<String,String>();

    public static void process (Socket clientSocket) throws IOException {
        // open up IO streams
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        /* Write a welcome message to the client */
        out.println("Welcome, please enter command");

        /* read and print the client's request */
        // readLine() blocks until the server receives a new line from client
        String input;
        input = in.readLine();
        System.out.println("input is " + input);
        String a[] = input.split(" ");
        String command = a[0];
        if(command.equals("ADD") || command.equals("Add") || command.equals("add"))
        {
            //add
            String unit1 = a[1];
            String unit2 = a[2];
            String ip = a[3];
            String port = a[4];
            String key1 = unit1 + "-" + unit2;
            String value1 = ip + "-" + port;
            if(table.containsKey(key1) && table.get(key1).equals(value1)){
                out.println("FAILURE already exists");
                System.out.println("failed add");
            }
            else{
                table.put(key1,value1);
                out.println("SUCCESS");
                System.out.println("success add");
            }
            
        }
        
        if(command.equals("REMOVE") || command.equals("Remove") || command.equals("remove"))
        {
            //remove
            String ip = a[1];
            String port = a[2];
            String value1 = a[1] + "-" + a[2];
            Iterator<Map.Entry<String, String>> it = table.entrySet().iterator();  
            while(it.hasNext())
            { 
                Map.Entry<String, String> entry=it.next();  
                String value2 = entry.getValue();  
                if(value1.equals(value2)){  
                    it.remove();        //OK   
                    out.println("SUCCESS");
                    System.out.println("success remove");
                    out.close();
                    in.close();
                    clientSocket.close();
                    return;
                }  
            }
            out.println("FAILURE no such ip-port pairs");
            System.out.println("failed remove");
        }
        if(command.equals("LOOKUP") || command.equals("Lookup") || command.equals("lookup"))
        {
            String unit1 = a[1];
            String unit2 = a[2];
            String key1 = a[1] + "-" + a[2];
            if(table.containsKey(key1))
            {
                String str = table.get(key1);
                String ss[] = str.split("-");
                out.println(ss[0] + " " + ss[1]);
                System.out.println("success lookup " + ss[0] + " " + ss[1]);
            }
            else
            {
                out.println("none");
                System.out.println("failed lookup");
            }
            
        }
        out.close();
        in.close();
        clientSocket.close();
    }


    public static void main(String[] args){

        //check if argument length is invalid
        if(args.length != 1) {
            System.err.println("port problem occured");
            System.exit(0);
        }
        
        try {
        // create socket
        int port = Integer.parseInt(args[0]);
        ServerSocket serverSocket = new ServerSocket(port);
        System.err.println("Started server on port " + port);

        // wait for connections, and process
            while(true) {
                // a "blocking" call which waits until a connection is requested
                Socket clientSocket = serverSocket.accept();
                System.err.println("\nAccepted connection from client");
                process(clientSocket);
                System.err.println("this while loop is ended");
            }

        }catch (IOException e) {
            System.err.println("Connection Error");
        }
        System.exit(0);
    }
}


