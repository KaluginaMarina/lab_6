package server;

import manage.*;
import client.util.CommandType;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.NoSuchElementException;


public class Answer extends Thread {
    private client.util.Command command;
    private Socket client;

    public Answer(client.util.Command command, Socket client) {
        this.command = command;
        this.client = client;
    }

    @Override
    public void run() {
        try {
            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            ObjectOutputStream oos = new ObjectOutputStream(out);

            if (command.commandType == CommandType.REMOVE_LAST) {
                Command.removeLast();
                oos.writeObject(Command.heroes);
            } else if (command.commandType ==  CommandType.LOAD) {
                Command.load();
                oos.writeObject(Command.heroes);
            } else if (command.commandType == CommandType.INFO) {
                oos.writeObject(Command.info());
            } else if (command.commandType == CommandType.REMOVE_GREATER) {
                Command.remove_greater(command.personage);
                oos.writeObject(Command.heroes);
            } else if (command.commandType == CommandType.ADD_IF_MAX) {
                Command.addIf("add_if_max", command.personage);
                oos.writeObject(Command.heroes);
            } else if (command.commandType == CommandType.ADD_IF_MIN) {
                Command.addIf("add_if_min", command.personage);
                oos.writeObject(Command.heroes);
            } else if (command.commandType == CommandType.ADD) {
                Command.add(command.personage);
                oos.writeObject(Command.heroes);
            } else if (command.commandType == CommandType.PRINT) {
                oos.writeObject(Command.heroes);
            } else if (command.commandType == CommandType.QUIT){
                oos.writeObject(Command.heroes);
                oos.flush();
                out.flush();
                return;
            } else if (command.commandType == CommandType.HELP){
                oos.flush();
                out.flush();
            }
            else {
                oos.writeObject(Command.heroes);
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