package com.iportbook.core.tools.message;

class MessageParseException extends Exception {
    MessageParseException() {
        super("error while parsing a string to message");
    }
}
