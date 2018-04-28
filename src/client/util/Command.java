package client.util;

import model.Shorties;

import java.io.Serializable;

public class Command implements Serializable {
    Shorties shorty;
    CommandType type;

    public Command(Shorties shorty, CommandType type) {
        this.shorty = shorty;
        this.type = type;
    }

    public Command(CommandType type) {
        this.type = type;
    }

    public Shorties getShorty() {
        return shorty;
    }

    public void setShorty(Shorties shorty) {
        this.shorty = shorty;
    }

    public CommandType getType() {
        return type;
    }

    public void setType(CommandType type) {
        this.type = type;
    }
}
