package server;

import manage.*;

import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Answer extends Thread {
    private Command cm = new Command();
    private String command;
    private Socket client;

    public Answer(String command, Command cm, Socket client) {
        this.cm = cm;
        this.command = command;
        this.client = client;
    }

    @Override
    public void run() {
        try {
            command = command.replaceAll("\\s+", "");
            if (command.equals("remove_last")) {
                cm.removeLast();
            } else if (command.equals("load")) {
                if (cm.load()) {
                    System.out.println("Выполнено.");
                }
            } else if (command.equals("info")) {
                cm.info();
            } else if (command.length() > 13 && command.substring(0, 14).equals("remove_greater")) {
                if (cm.remove_greater(command.substring(14))) {
                    System.out.println("Выполнено.");
                }
                ;
            } else if ((command.length() > 9 && command.substring(0, 10).equals("add_if_max")) || (command.length() > 9 && command.substring(0, 10).equals("add_if_min"))) {
                if (cm.addIf(command.substring(0, 10), command.substring(10))) {
                    System.out.println("Выполнено.");
                }
            } else if (command.length() > 2 && command.substring(0, 3).equals("add")) {
                if (cm.add(command.substring(3, command.length()))) {
                    System.out.println("Персонаж добавлен в коллекцию.");
                }
                ;
            } else if (command.equals("print")) {
                System.out.println(cm.getHeroes());
            } else if (command.equals("exit")) {
                return;
            } else {
                System.out.println("Команда не найдена.\nhelp / ?: открыть справку");
            }
        } catch (NoSuchElementException e) {

            System.out.println("Где-то ошибка");
            return;
        };

        try {
            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            out.writeUTF(cm.toSCV());
            //System.out.println(cm.heroes);
            out.flush();
        } catch (SocketException e){
            System.out.println("**Конец передачи");
        } catch (EOFException e){
            System.out.println("**Конец передачи.");
        }  catch (IOException e) {
            e.printStackTrace();
        }

    }
}
