package manage;

import model.Personage;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Command extends CollectionManage{

    public Command(){
        createDate = new Date();
        changeDate = createDate;
    }
    /**
     * Метод, сортирующий коллекцию по возрастанию
     */
    @Override
    public void sort(){
        List<Personage> list = toList();
        Collections.sort(list, new Comparator<Personage>() {
            public int compare(Personage p1, Personage p2) {
                return p1.compare(p2);
            }
        });
        toArrayDedue(list);
        changeDate = new Date();
    }

    /**
     * remove_greater {element}: удалить из коллекции все элементы, превышающие заданный
     * Формат задания элемента {element}- json
     * При вводе {element} другого формата или при вводе некорректного представления объекта - бросается исключение
     * @param next -строка, которая подается пользователем после команды
     * @return true - успешное выполнение команды, false - при возникновении ошибки
     */
    @Override
    public boolean remove_greater(String next){
        if (!add(next)) {
            return false;
        };
        List<Personage> list = toList();
        for (int i = 0; i < list.size() - 1; ++i){
            if (list.get(i).compare(list.get(list.size() - 1)) > 0){
                list.remove(i);
            }
        }
        list.remove(list.get(list.size() - 1));
        toArrayDedue(list);
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
    @Override
    public boolean addIf(String command, String next){
        if (heroes.isEmpty()){
            System.out.println("Коллекция пуста.");
            return false;
        }
        if (!add(next)){
            return false;
        }
        Personage p = heroes.removeLast();
        List<Personage> list = toList();
        Collections.sort(list, new Comparator<Personage>() {
            public int compare(Personage p1, Personage p2) {
                return p1.compare(p2);
            }
        });
        if(command.equals("add_if_max")? (p.compare(list.get(list.size()-1)) <= 0) : (p.compare(list.get(0)) >= 0)){
            System.out.println("Элемент не добавлен.");
            return true;
        }
        heroes.add(p);
        System.out.println("Элемент добавлен");
        changeDate = new Date();
        return true;
    }

    /**
     * info: вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, дата изменения, количество элементов)
     */
    @Override
    public void info(){
        System.out.println("Тип коллекции: " + heroes.getClass());
        System.out.println("Количество элементов в коллекци: " + heroes.size());
        System.out.println("Дата создания: " + createDate);
        System.out.println("Дата изменения: " + changeDate);
    }
}
