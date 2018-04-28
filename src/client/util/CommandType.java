package client.util;

import java.io.Serializable;

public enum CommandType implements Serializable {
    ADD, REMOVE_LAST, LOAD, INFO, PRINT, QUIT, HELP;
}
