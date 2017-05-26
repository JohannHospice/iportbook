package com.iportbook.app.tools;

import java.io.*;
import java.util.ArrayList;

public class Serializer {
    public static void write(String filename, ArrayList<?> data) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
        out.writeObject(data);
        out.flush();
        out.close();
    }

    public static ArrayList<?> read(String filename) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
        ArrayList<?> data = (ArrayList<?>) in.readObject();
        in.close();
        return data;
    }
}
