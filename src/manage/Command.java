package manage;

import model.Moonlighter;
import model.Personage;
import model.Reader;
import model.Shorties;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.Date;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.toIntExact;

public class Command extends CollectionManage{

    public Command(){
        createDate = new Date();
        changeDate = createDate;
    }

    /**
     * add {element} Метод для добавления элемента в коллекцию в интерактивном режиме
     * Формат задания элемента {element}- json
     * При вводе {element} другого формата или при вводе некорректного представления объекта - бросается исключение
     * @return true - успешное выполнение команды, false - при возникновении ошибки
     */
     public static boolean add(Personage pers) {
        //heroes.add(pers);
        heroes = Stream.concat(heroes.stream(), Stream.of(pers)).sorted().collect(Collectors.toCollection(ConcurrentLinkedDeque::new));
        changeDate = new Date();
        return true;
    }


    /**
     *  load - перечитать коллекцию из файла
     * @return true - успешное выполнение команды, false - при возникновении ошибки
     */
    public static boolean load(){
        heroes = new ConcurrentLinkedDeque<>();
        return (collectionCreater());
    }

    /**
     * remove_greater {element}: удалить из коллекции все элементы, превышающие заданный
     * Формат задания элемента {element}- json
     * При вводе {element} другого формата или при вводе некорректного представления объекта - бросается исключение
     * @return true - успешное выполнение команды, false - при возникновении ошибки
     */
    public static boolean remove_greater(Personage pers){
        if (pers == null) {
            return false;
        };
        heroes = heroes.stream().filter(x -> x.compareTo(pers) < 0).sorted().collect(Collectors.toCollection(ConcurrentLinkedDeque::new));
        changeDate = new Date();
        return true;
    }

    /**
     * add_if_max {element}: добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции
     * add_if_min {element}: добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции
     * Формат задания элемента {element}- json
     * При вводе {element} другого формата или при вводе некорректного представления объекта - бросается исключение
     * @param command "add_if_max" или "add_if_min"
     * @return true - успешное выполнение команды, false - при возникновении ошибки
     */
    public static boolean addIf(String command, Personage pers){
        if (heroes.isEmpty()){
            heroes = Stream.concat(heroes.stream(), Stream.of(pers)).sorted().collect(Collectors.toCollection(ConcurrentLinkedDeque::new));
            System.out.println("Элемент " + pers.toString() + " добавлен");
            changeDate = new Date();
            return true;
        }
        if (pers == null){
            return false;
        }
        if(command.equals("add_if_max")? (pers.compareTo(heroes.getLast()) <= 0) : (pers.compareTo(heroes.getLast()) >= 0)){
            System.out.println("Элемент " + pers.toString() + " не добавлен. Элемент не самый " + (command.equals("add_if_max")? "большой." : "маленький"));
            return true;
        }
        heroes = Stream.concat(heroes.stream(), Stream.of(pers)).sorted().collect(Collectors.toCollection(ConcurrentLinkedDeque::new));
        System.out.println("Элемент " + pers.toString() + " добавлен");
        changeDate = new Date();
        return true;
    }

    /**
     * info: вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, дата изменения, количество элементов)
     */
    public static String info(){
        return "Тип коллекции: " + heroes.getClass() + "\n" + "Количество элементов в коллекци: " + heroes.size() + "\n" + "Дата создания: " + createDate + "\n" + "Дата изменения: " + changeDate + "\n";
    }


    /**
     * remove_last - удаляет последний элемент
     */
    public static void removeLast(){
        if (heroes.isEmpty()){
            System.out.println("Коллекция пуста.");
            return;
        }
        System.out.println("Элемент " + heroes.getLast() + " удален.");
        heroes = heroes.stream().limit(heroes.size() - 1).collect(Collectors.toCollection(ConcurrentLinkedDeque::new));
        changeDate = new Date();
    }
}
