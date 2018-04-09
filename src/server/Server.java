package server;

import manage.Command;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Server {
    public static void run() {
        Command cm = new Command();
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            Socket client = serverSocket.accept();
            System.out.println("**Connection accepted.");
            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            DataInputStream in = new DataInputStream(client.getInputStream());
            while(!client.isClosed()){
                System.out.println("**Server reading from channel...");
                String entry = in.readUTF();
                System.out.println(entry);
                if(entry.equalsIgnoreCase("quit")){
                    out.writeUTF("quit");
                    out.flush();
                    break;
                }
                Answer answer = new Answer(entry, cm, client);
                answer.start();
            }
            in.close();
            out.close();
            client.close();
        } catch (SocketException e){
            System.out.println("**Конец передачи.");
        } catch (EOFException e){
            System.out.println("**Конец передачи.");
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
