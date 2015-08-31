// CS 6421 - Simple Message Board Client in Java
// T. Wood
// Compile with: javac MsgClient
// Run with:     java MsgClient

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MsgClient {
    public static void main(String[] args) {
        String host = "twood02.koding.io";
        int portnum = 5555;
        String name = "YOUR NAME";
        String msg = "YOUR MESSAGE";
        Socket s;
        try {
            // create socket and output stream
            s = new Socket(host, portnum);
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);

            // send my name
            out.println(name);
            // send my message
            out.println(msg);

            // Make sure messages are sent
            out.flush();

            // clean up
            out.close();
            s.close();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
