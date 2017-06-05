package com.iportbook.core.tools;

import java.util.Scanner;

public class TerminalScanner {
    private Scanner scanner = new Scanner(System.in);

    public void indent() {
        System.out.print("> ");
    }

    public String next() {
        indent();
        String input = scanner.next();
        scanner.nextLine();
        return input;
    }

    /**
     * get next text wrote on terminal with a specific pattern(regex)
     *
     * @param pattern String
     * @return String
     */
    public String next(String pattern) {
        indent();
        String next = null;
        if (scanner.hasNext(pattern))
            next = scanner.next();
        return next;
    }

    public String ask(String question) {
        System.out.print(question);
        return next();
    }

    public String ask(String question, String pattern) {
        System.out.println(question);
        String next = null;
        while (next == null) {
            indent();
            if (scanner.hasNext(pattern))
                next = scanner.next(pattern);
            scanner.nextLine();
        }
        return next;
    }

    public void close() {
        scanner.close();
    }
}
