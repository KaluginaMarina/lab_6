package model;

public class Tree extends Forest {
    String name = "дерево";
    double x = 0;
    double y = 0;
    Forest forest = null;

    public Tree(String name){
        this.name = name;
    }

    public Tree(String name, double x, double y, Forest f){
        this.name = name;
        this.x = x;
        this.y = y;
        this.forest = f;
    }
}


