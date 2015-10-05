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
import java.util.Queue;
import java.util.LinkedList;
import java.util.HashMap;

public class ProxyServer {
    
    // server ip address
    static String lbsConvb = "127.0.0.1 5553";
    static String bConvin = "127.0.0.1 5554";
    static String bConvg = "127.0.0.1 5555";
    static int NUMOFNODES = 4;
    /* convertion table
    *   lbs  b  in  g
    lbs      1
    b   1       1   1
    in       1
    g        1
    */
    static int[][] convTable = {{0, 1, 0, 0},
                            {1, 0, 1, 1},
                            {0, 1, 0, 0},
                            {0, 1, 0, 0}};
                            
    static String[] getLable = {"lbs","b","in","g"};
    static HashMap<String, Integer> getNum;
    static HashMap<String, String> getServer;
    
    /*
     * use BFS to visit all the nodes to find a convertion path
     * return the last node containing the last step of unit to 
     * complete convertion 
     */
    public static Node findPath(int s, int e){
        if(convTable[s][e] == 1){
            Node snode = new Node();
            snode.setNum(s);
            Node enode = new Node();
            enode.setNum(e);
            enode.setPre(snode);
            return enode;    
        }
        //create a queue to visit all the nodes
        Queue<Node> queue = new LinkedList<Node>();
       
		HashMap<Integer, Integer> dist = new HashMap<Integer, Integer>();
		Node snode = new Node();
		snode.setNum(s);
		queue.add(snode);
		dist.put(s, 1);
        while(!queue.isEmpty()){
            Node fnode = queue.poll();
			int head = fnode.getNum();
			int headDist = dist.get(head);
			if(convTable[head][e] == 1){
			    Node enode = new Node();
				enode.setNum(e);
				enode.setPre(fnode);
			    return enode;
			}
			for(int i = 0;i < NUMOFNODES;i++){
				if(convTable[head][i] == 1 && !dist.containsKey(i)){
				    Node cnode = new Node();
					cnode.setNum(i);
					cnode.setPre(fnode);
					queue.add(cnode);
					dist.put(i, headDist + 1);
				}
			}
		}
		return null;
    }   
    
    /*
     * change lable to number in convTable
     */
    public static void init(){
        getNum = new HashMap<String, Integer>();
        getNum.put("lbs", 0);
        getNum.put("b", 1);
        getNum.put("in", 2);
        getNum.put("g", 3);
        getServer = new HashMap<String, String>();
        getServer.put("lbs b", lbsConvb);
        getServer.put("b lbs", lbsConvb);
        getServer.put("b in", bConvin);
        getServer.put("in b", bConvin);
        getServer.put("b g", bConvg);
        getServer.put("g b", bConvg);
    }
    
    public static String[] callServer(String msg, String server) {

        Socket sock = null;
        String[] userInput = null;
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            String[] serverArg = server.split(" ");
            sock = new Socket(serverArg[0], Integer.parseInt(serverArg[1]));    //get the socket and connet to the server
            BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));   //get reader
            PrintWriter out = new PrintWriter(sock.getOutputStream(), true);    //get printer
            out.println(msg + "\n");   //send messages
            userInput = new String[2];
            
            if ((userInput[0] = in.readLine()) == null) {  //get reply
                System.out.println("Error reading message");
                out.close();
                in.close();
                sock.close();
            }

            userInput[1] = in.readLine();
            System.out.println("server info: " + userInput[0] + "; return result: " + userInput[1]);
            out.close();
            in.close();
            sock.close();
        } catch(Exception e) {
            out.println("ERROR:" + e);
        }

        //System.out.println(msg + " sended.");
        return userInput;
    }
    
    public static boolean checkArg(String[] m){
        if(!m[0].equals(m[1])){
            for(int i = 0;i < getLable.length;i++){
                if(m[0].equals(getLable[i])){
                    for(int j = 0;j < getLable.length;j++){
                        if(m[1].equals(getLable[j])){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public static void process (Socket clientSocket) throws IOException {
        // open up IO streams
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        /* Write a welcome message to the client */
        out.println("Welcome, you are connected to a Java-based Proxy Server");

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
        init();
        String[] arg = userInput.split(" ");
        int[] path = new int[NUMOFNODES];
        int j = 0;
        // Exception handler
        if (arg.length != 3){
            out.println("pls input 3 arguements. Usage: eg. lbs g 2 or g lbs 2");
        }else if(!checkArg(arg)){
            out.print("Wrong input. allowed input units: ");
            for(int i = 0;i < getLable.length;i++){
                out.print(getLable[i]+ " ");
            }
            out.print("\n");
        }else{
            // find path, complete convertion, send result
            Node node = findPath(getNum.get(arg[0]), getNum.get(arg[1]));
            if(node == null){
                out.println("ERROR, can not find any convertion path between the two units.");
            }else{
                path[j] = node.getNum(); 
                while(node.getPre() != null){
                    node = node.getPre();
                    j++;
                    path[j] = node.getNum();
                }
                
                String argNum = arg[2];
                for(int i = j; i > 0; i--){
                    String[] result = callServer(getLable[path[i]]+" "+getLable[path[i-1]]+" "+argNum, getServer.get(getLable[path[i]]+" "+getLable[path[i-1]]));
                    out.println(result[0]);
                    argNum = result[1];
                    
                }
                out.println(argNum);
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

class Node{
    int num;
    Node pre;
    
    public void setNum(int n){
        num = n;
    }
    
    public int getNum(){
        return num;
    }
    
    public void setPre(Node p){
        pre = p;
    }
    
    public Node getPre(){
        return pre;
    }
    
}

