package com.iportbook.app;

import java.util.Scanner;

public class TerminalScanner {
    private Scanner scanner = new Scanner(System.in);

    public String nextLine() {
        return scanner.nextLine();
    }

    /**
     * get next text wrote on terminal with a specific pattern(regex)
     *
     * @param pattern String
     * @return String
     */
    public String getNext(String pattern) {
        String next = null;
        while (scanner.hasNext(pattern))
            next = scanner.next();
        return next;
    }

    public String ask(String question) {
        System.out.print(question);
        return nextLine();
    }

    public String ask(String question, String pattern) {
        System.out.println(question);
        System.out.print("> ");
        return getNext(pattern);
    }

    public void close() {
        scanner.close();
    }
}
