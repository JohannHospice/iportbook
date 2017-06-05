package com.iportbook.core.tools;

import java.util.Scanner;
import java.util.regex.Pattern;

public class TerminalScanner {
    Scanner scanner = new Scanner(System.in);

    public String next() {
        return scanner.next();
    }

    /**
     * get next text wrote on terminal with a specific pattern(regex)
     *
     * @param pattern String
     * @return String
     */
    public String next(String pattern) {
        String next = null;
        while (next == null) {
            if (scanner.hasNext(pattern)) {
                next = scanner.next(pattern);
            } else {
                String dump = scanner.next();
                if (Pattern.compile(pattern).matcher(dump).find())
                    next = dump;
                else {
                    System.out.print("L'entrée doit respecter le pattern suivant \"" + pattern + "\".\n> ");
                    if (scanner.hasNextLine())
                        scanner.nextLine();
                }
            }
        }
        if (scanner.hasNextLine())
            scanner.nextLine();
        return next;
    }


    private String nextLine(int length) {
        String next = null;
        while (next == null) {
            if (scanner.hasNextLine()) {
                next = scanner.nextLine();
                if (next.length() > length || next.length() <= 0) {
                    System.out.print("La taille du champs est comprise entre 0 et " + length + " caracteres.\n> ");
                    next = null;
                }
            }
        }
        return next;
    }

    public String askNext(String question) {
        System.out.print(question);
        return next();
    }

    public String askNext(String question, String pattern) {
        System.out.print(question);
        return next(pattern);
    }

    public String askNextLine(String question, int length) {
        System.out.print(question);
        return nextLine(length);
    }

    public void close() {
        scanner.close();
    }
}
