package manage;

import model.*;
import model.Reader;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

import static java.lang.Math.toIntExact;


abstract class CollectionManage {
    public static ConcurrentLinkedDeque<Personage> heroes = new ConcurrentLinkedDeque<>();
    static Date createDate;
    static Date changeDate;

    //private final String fileName = "materials/Heroes.csv";
    //private final String fileNameClosing = "materials/HeroesClosing.csv";
    private static final String fileName = System.getenv("FILENAME");
    //private static final String fileNameClosing = System.getenv("FILENAMECLOSE");
    private static final String fileNameClosing = System.getenv("FILENAME");

    public ConcurrentLinkedDeque<Personage> getHeroes() {
        return heroes;
    }

    /**
     * Метод для чтения текста из файла fileName
     * @return String heroesJson - строка-содержимое файла
     */
    public static String read(String fileName){
        String heroesJson = "";
        try (FileInputStream fis = new FileInputStream(fileName);
             InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
             BufferedReader br = new BufferedReader(isr)
        )
        {
            String line;

            while ((line = br.readLine()) != null) {
                heroesJson += line + "\n";
            }
        } catch (Exception e) {
            System.out.println("Неправильный путь для FILENAME.");
        }
        return heroesJson;
    }

    /**
     * Метод, создающий коллекцию Personage по данным из файла fileName
     * @return true, false
     */
    public static boolean collectionCreater(){
        String heroesJson = read(fileName);
        Scanner sc = new Scanner(heroesJson);
        sc.useDelimiter("[,\n]");
        sc.useLocale(Locale.ENGLISH);
        try {
            while(sc.hasNext()){
                String type = sc.next();
                switch (type){
                    case "Читатель": {
                        Reader reader = new Reader(sc.next());
                        reader.height = sc.nextInt();
                        reader.force = sc.nextInt();
                        if (!reader.setMood(sc.next())){
                            throw new Exception();
                        }
                        heroes.add(reader);
                        break;
                    }
                    case "Лунатик": {
                        Moonlighter moonlighter = new Moonlighter(sc.next(), sc.nextDouble(), sc.nextDouble(), sc.nextInt());
                        moonlighter.skillSwear = sc.nextInt();
                        moonlighter.force = sc.nextInt();
                        if (!moonlighter.setMood(sc.next())){
                            throw new Exception();
                        }
                        heroes.add(moonlighter);
                        break;
                    }
                    case "Коротышка": {
                        Shorties shorties = new Shorties(sc.next(), sc.nextDouble(), sc.nextDouble(), sc.nextInt());
                        shorties.skillSwear = sc.nextInt();
                        shorties.force = sc.nextInt();
                        if (!shorties.setMood(sc.next())){
                            throw new Exception();
                        }
                        heroes.add(shorties);
                        break;
                    }
                    default: {
                        throw new Exception();
                    }
                }
            }
            heroes = heroes.stream().sorted().collect(Collectors.toCollection(ConcurrentLinkedDeque::new));
            changeDate = new Date();
            createDate = new Date();
            return true;
        } catch (Exception e){
            System.out.println("Неправильное представление объекта.");
            return false;
        }
    }

    /**
     * Метод, записывающий текущее состояние коллекции в файл fileNameClose
     */
    public static void collectionSave(){
        if (!Files.isWritable(Paths.get(fileNameClosing))){
            System.out.println("Что-то с правами");
            return;
        }

        try (FileWriter file = new FileWriter(fileNameClosing, false)){
            String toCsv = toSCV();
            file.write(toCsv);
        } catch (Exception e){
            System.out.println("Неправильный путь для FILENAMECLOSE." + e.getMessage());
        }
    }

    /**
     * Метод возвращает копию объекта ConcurrentLinkedDeque<Personage>
     * (тут вроде нельзя пользоваться clone)
     * @param c - копируемый объект
     * @return ConcurrentLinkedDeque<Personage> clone
     */
    private static ConcurrentLinkedDeque<Personage> copy (ConcurrentLinkedDeque c){
        ConcurrentLinkedDeque<Personage> res = new ConcurrentLinkedDeque<>();
        ConcurrentLinkedDeque<Personage> tmp = new ConcurrentLinkedDeque<>();
        while (!c.isEmpty()){
            Personage pers = (Personage) c.pollFirst();
            res.add(pers);
            tmp.add(pers);
        }
        heroes = tmp;
        return res;
    }

    /**
     * Метод, переаодящий строку в строку, формата scv
     * @return String - строка, формата scv
     */
    public static String toSCV(){
        return heroes.stream()
                .map(x -> ((x.type.equals("Читатель")) ? x.type + "," + x.name + "," + x.height  + "," + x.force + "," + x.mood + "\n" : x.type + "," + x.name + "," + x.x + "," + x.y + "," + x.height + "," + x.skillSwear + "," + x.force + "," + x.mood + "\n"))
                .collect(Collectors.joining());
    }

    /**
     * Считывает строку из консоли.
     * Считывание происходит до тех пор, пока не встретится пустая строка
     * @return String - считанная строка
     */
    static String readPers(){
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

    /**
     * Метод считывает героя в формате json и создает новый объект персонажа
     * @param next строка, которая посылается пользователем непосредственно за командой (на той же строке)
     * @return объект персонажа
     */
    static Personage newPers(String next) {
        try {
            String heroesJson = readPers();
            heroesJson = next + heroesJson;
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
}