package client.util;

import java.io.Serializable;

public enum CommandType implements Serializable {
    ADD, REMOVE_LAST, LOAD, INFO, PRINT, QUIT, HELP, REMOVE_GREATER, ADD_IF_MIN, ADD_IF_MAX, NOTCOMMAND, NOTPERS, CONNECTION_AGAIN;
}
