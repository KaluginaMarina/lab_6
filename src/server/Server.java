package server;

import manage.Command;
import model.Personage;
import client.util.CommandType;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Server implements Runnable{
    private Socket client;

    public Server (Socket client){
        this.client = client;
    }

    @Override
    public void run() {
        Command cm = new Command();
        try /*(ServerSocket serverSocket = new ServerSocket(8080))*/ {
            //Socket client = serverSocket.accept();
            System.out.println("**Connection accepted.");
            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            DataInputStream in = new DataInputStream(client.getInputStream());
            client.util.Command entry = null;
            while(!client.isClosed()){
                ObjectInputStream ois = new ObjectInputStream(in);
                Object ob = ois.readObject();
                try {
                    client.util.Command tmp = (client.util.Command) ob;
                    entry = tmp;
                    System.out.println("\n**Получено...");
                    System.out.println(entry);
                } catch (ClassCastException e) {
                    System.out.println("Ошибка. Команда не команда");
                }
                if(entry != null && entry.commandType == CommandType.QUIT){
                    out.flush();
                    break;
                }
                Answer answer = new Answer(entry, client);
                answer.run();
            }
            in.close();
            out.close();
            client.close();
        } catch (SocketException e){
            System.out.println("**Конец передачи.");
            System.out.println("socket exception");
            e.printStackTrace();
        } catch (EOFException e){
            System.out.println("**Конец передачи.");
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}