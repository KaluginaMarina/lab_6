package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void run() {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            Socket client = serverSocket.accept();
            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            DataInputStream in = new DataInputStream(client.getInputStream());
            while(!client.isClosed()){
                String entry = in.readUTF();
                if(entry.equalsIgnoreCase("quit")){
                    out.writeUTF("quit");
                    out.flush();
                    break;
                }
                out.writeUTF("answer");
                out.flush();
            }
            in.close();
            out.close();
            client.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
