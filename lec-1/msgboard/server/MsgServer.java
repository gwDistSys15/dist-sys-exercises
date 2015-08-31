// CS 6421 - Simple Message Board
// T. Wood
// Compile with: javac MsgServer
// Run with:     java MsgServer
// The server runs on port 5555

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;


class MsgServerThread implements Runnable {
        private Socket sock;

        public MsgServerThread(Socket s) {
                this.sock = s;
        }

        public void run() {
                try {
                        PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                        System.out.println("Got connection from " + sock.getInetAddress());

                        // read in the  name and message
                        String n = in.readLine();
                        String m = in.readLine();
                        System.out.println(n + ": " + m);

                        // clean things up
                        out.close();
                        in.close();
                        sock.close();

                } catch (IOException e) {
                        e.printStackTrace();
                }
        }
}

public class MsgServer {

        public MsgServer() {
                ServerSocket serverSocket = null;
                boolean listening = true;

                try {
                        InetAddress addr = InetAddress.getLocalHost();

                        // Get IP Address
                        byte[] ipAddr = addr.getAddress();

                        // Get hostname
                        String hostname = addr.getHostAddress();
                        System.out.println("Server IP = " + hostname);
                } catch (UnknownHostException e) {}


                try {
                        serverSocket = new ServerSocket(5555);
                } catch (IOException e) {
                        System.err.println("Could not listen on port: 5555.");
                        System.exit(-1);
                }

                System.out.println("Waiting for connections on port 5555...");

                while (listening) {
                        try {
                                // wait for a connection
                                MsgServerThread job = new MsgServerThread(serverSocket.accept());
                                // start a new thread to handle the connection
                                Thread t = new Thread(job);
                                t.start();
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }

                try {
                        serverSocket.close();
                } catch (IOException e) {
                        e.printStackTrace();
                }

        }

        public static void main(String[] args) {
                new MsgServer();
        }

}
