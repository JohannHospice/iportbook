package com.iportbook.core.tools.message;

class MessageTCPParseException extends Exception {
    MessageTCPParseException() {
        super("error while parsing a string to message");
    }
}