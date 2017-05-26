package com.iportbook.app.tools;

public class MessageParseException extends Exception {
    MessageParseException(){
        super("error while parsing a string to message");
    }
}
