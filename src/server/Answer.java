package server;

import manage.*;
import client.util.CommandType;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.NoSuchElementException;


public class Answer extends Thread {
    private Command cm;
    private client.util.Command command;
    private Socket client;

    public Answer(client.util.Command command, Command cm, Socket client) {
        this.cm = cm;
        this.command = command;
        this.client = client;
    }

    @Override
    public void run() {
        try {
            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            ObjectOutputStream oos = new ObjectOutputStream(out);

            if (command.commandType == CommandType.REMOVE_LAST) {
                cm.removeLast();
                oos.writeObject(cm.heroes);
            } else if (command.commandType ==  CommandType.LOAD) {
                cm.load();
                oos.writeObject(cm.heroes);
            } else if (command.commandType == CommandType.INFO) {
                oos.writeObject(cm.info());
            } else if (command.commandType == CommandType.REMOVE_GREATER) {
                cm.remove_greater(command.personage);
                oos.writeObject(cm.heroes);
            } else if (command.commandType == CommandType.ADD_IF_MAX) {
                cm.addIf("add_if_max", command.personage);
                oos.writeObject(cm.heroes);
            } else if (command.commandType == CommandType.ADD_IF_MIN) {
                cm.addIf("add_if_min", command.personage);
                oos.writeObject(cm.heroes);
            } else if (command.commandType == CommandType.ADD) {
                cm.add(command.personage);
                oos.writeObject(cm.heroes);
            } else if (command.commandType == CommandType.PRINT) {
                oos.writeObject(cm.heroes);
            } else if (command.commandType == CommandType.QUIT){
                oos.writeObject(cm.heroes);
                oos.flush();
                out.flush();
                return;
            } else if (command.commandType == CommandType.HELP){
                oos.flush();
                out.flush();
            }
            else {
                oos.writeObject(cm.heroes);
                System.out.println("Команда не найдена.\nhelp / ?: открыть справку");
            }
            oos.flush();
            out.flush();
        } catch (NoSuchElementException e) {
            System.out.println("Где-то ошибка");
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
