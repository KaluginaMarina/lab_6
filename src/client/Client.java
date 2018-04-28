package client;

import client.util.Command;
import client.util.CommandType;
import jdk.nashorn.internal.parser.JSONParser;
import model.Moonlighter;
import model.Personage;
import model.Reader;
import model.Shorties;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedDeque;

import static java.lang.Math.toIntExact;

public class Client {
    public static void run(ConcurrentLinkedDeque<Personage> heroes) {
        try (Socket socket = new Socket("localhost", 65527);
             //FileInputStream fis = new FileInputStream("materials/script.txt");
             //InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
             BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
             DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
             DataInputStream dis = new DataInputStream(socket.getInputStream());) {
            System.out.println("**Client connected to socket.");
            String clientCommand;
            while (!socket.isOutputShutdown()) {
                /*if (socket.isOutputShutdown()){
                    break;
                }*/
                if (br.ready()) {
                    System.out.println("**Отправлено...");
                    clientCommand = br.readLine();
                    Command command = readCommand(br);
                    System.out.println(clientCommand + "\n");
                    System.out.println("**Получено...");
                    ObjectOutputStream oos = new ObjectOutputStream(dos);
                    oos.writeObject(clientCommand);
                    oos.flush();
                    dos.flush();
                    if (clientCommand.equalsIgnoreCase("quit")) {
                        if (dis.read() > -1) {
                            String in = dis.readUTF();
                            System.out.println(in);
                        }
                        break;
                    }
                    ObjectInputStream ois = new ObjectInputStream(dis);
                    try {
                        Object ob = ois.readObject();
                        try {
                            ConcurrentLinkedDeque<Personage> tmp = (ConcurrentLinkedDeque<Personage>) ob;
                            heroes = tmp;
                            System.out.println(heroes);
                        } catch (ClassCastException e) {
                            String tmp = (String) ob;
                            System.out.println(tmp);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
            if (dis.read() > -1) {
                System.out.println("reading...");
                String in = dis.readUTF();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден");

        } catch (ConnectException e) {
            System.out.println("**Ошибка соединения.");
            System.out.println("Попробовать еще раз? Y/N");
            while (true) {
                Scanner input = new Scanner(System.in);
                String command = input.nextLine();
                if (command.equals("Y")) {
                    Client.run(heroes);
                    break;
                } else if (command.equals("N")) {
                    break;
                }
                input.close();
            }
        } catch (SocketException e) {
            System.out.println("**Конец передачи.");
        } catch (EOFException e) {
            System.out.println("**Конец передачи");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Command readCommand(BufferedReader br) throws IOException {
        String commandType = br.readLine();
        switch (commandType) {
            case "QUIT": {
                return new Command(CommandType.QUIT);
            }
            case "ADD": {
                Command command = new Command(CommandType.ADD);
                String json = "";
                while (true) {
                    String line = br.readLine();
                    if (line.isEmpty()) {
                        break;
                    }
                    json += line;
                }
                return new Command(CommandType.ADD, )
            }
            //convert JSON string to объект Коротышки\
        }
        return null;
    }


}
