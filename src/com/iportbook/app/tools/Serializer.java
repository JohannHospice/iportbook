package com.iportbook.app.tools;

import java.io.*;
import java.util.ArrayList;

public class Serializer {
    public static void write(String filename, ArrayList<?> yourArrayList) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
        out.writeObject(yourArrayList);
        out.flush();
        out.close();
    }

    public static ArrayList<?> read(String filename) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
        ArrayList<?> myArrayList = (ArrayList<?>) in.readObject();
        in.close();
        return myArrayList;
    }
}
