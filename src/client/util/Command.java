package client.util;

import model.*;
import model.Reader;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.Scanner;

import static java.lang.Math.toIntExact;

public class Command implements Serializable {
    public Personage personage;
    public CommandType commandType = null;

    public Command (CommandType commandType, Personage personage){
        this.commandType = commandType;
        this.personage = personage;
    }

    public Command (CommandType commandType){
        this.commandType = commandType;
    }

    private static String readPers(){
        Scanner input = new Scanner(System.in);
        String res = "";
        String str = input.nextLine();
        if(str.equals("\\s+")){
            str = input.nextLine();
        }
        try{
            while (!(str).equals("")){
                res += str;
                str = input.nextLine();
            }
        } catch (Exception e){
            e.printStackTrace();
            return res;
        };
        return res;
    }

    public static Personage createPers (String json){
        try {
            String heroesJson = readPers();
            heroesJson = json + heroesJson;
            if (heroesJson == null){
                return null;
            };
            JSONParser parser = new JSONParser();
            JSONObject ob = (JSONObject) parser.parse(heroesJson);
            switch ((String)ob.get("type")) {
                case "Читатель": {
                    Reader reader = new Reader((String) ob.get("name"));
                    reader.height = toIntExact((long) ob.get("height"));
                    reader.force = toIntExact((long) ob.get("force"));
                    if(!reader.setMood((String) ob.get("mood"))){
                        throw new Exception();
                    }
                    return reader;
                }
                case "Лунатик": {
                    Moonlighter moonlighter = new Moonlighter((String) ob.get("name"), (double) ob.get("x"), (double) ob.get("y"), toIntExact((long) ob.get("height")));
                    moonlighter.skillSwear =  toIntExact((long) ob.get("skillSwear"));
                    moonlighter.force =  toIntExact((long) ob.get("force"));
                    if(!moonlighter.setMood((String) ob.get("mood"))){
                        throw new Exception();
                    }
                    return moonlighter;
                }
                case "Коротышка": {
                    Shorties shorties = new Shorties((String) ob.get("name"), (double) ob.get("x"), (double) ob.get("y"), toIntExact((long) ob.get("height")));
                    shorties.skillSwear =  toIntExact((long) ob.get("skillSwear"));
                    shorties.force =  toIntExact((long) ob.get("force"));
                    if(!shorties.setMood((String) ob.get("mood"))){
                        throw new Exception();
                    }
                    return shorties;
                }
                default: {
                    throw new Exception();
                }
            }
        } catch (Exception e){
            System.out.println("Объект должен быть формата json или введено некорректное представление объекта.");
            return null;
        }

    }

    public static Command readCommand (String command){
        Personage personage;
        command = command.replaceAll("\\s+", "");
        if (command.equals("remove_last")) {
            return new Command(CommandType.REMOVE_LAST);
        } else if (command.equals("load")) {
            return new Command(CommandType.LOAD);
        } else if (command.equals("info")) {
            return new Command(CommandType.INFO);
        } else if (command.length() > 13 && command.substring(0, 14).equals("remove_greater")) {
            personage = createPers(command.substring(14));
            if (personage != null){
                return new Command(CommandType.REMOVE_GREATER, personage);
            } else {
                return new Command(CommandType.NOTPERS);
            }
        } else if ((command.length() > 9 && command.substring(0, 10).equals("add_if_max"))) {
            personage = createPers(command.substring(10));
            if (personage != null){
                return new Command(CommandType.ADD_IF_MAX, personage);
            } else {
                return new Command(CommandType.NOTPERS);
            }
        } else if ((command.length() > 9 && command.substring(0, 10).equals("add_if_min"))){
            personage = createPers(command.substring(10));
            if (personage != null){
                return new Command(CommandType.ADD_IF_MIN, personage);
            } else {
                return new Command(CommandType.NOTPERS);
            }
        } else if (command.length() > 2 && command.substring(0, 3).equals("add")) {
            personage = createPers(command.substring(3, command.length()));
            if (personage != null){
                return new Command(CommandType.ADD, personage);
            } else {
                return new Command(CommandType.NOTPERS);
            }
        } else if (command.equals("print")) {
            return new Command(CommandType.PRINT);
        } else if (command.equals("help") || command.equals("?")){
            return new Command(CommandType.HELP);
        } else if (command.equals("quit")){
            return new Command(CommandType.QUIT);
        } else if (command.equals("connection_again")){
            return new Command(CommandType.CONNECTION_AGAIN);
        }
        else {
            return new Command(CommandType.NOTCOMMAND);
        }
    }

    @Override
    public String toString() {
        return "Command{" +
                ((personage != null) ? "personage=" + personage + ", ": "") +
                "commandType=" + commandType +
                '}';
    }
}
