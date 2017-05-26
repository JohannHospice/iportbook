package com.iportbook.core.tools.io;

import java.io.*;

public class Serializer {
    public static void write(String filename, Object data) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
        out.writeObject(data);
        out.flush();
        out.close();
    }

    public static Object read(String filename) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
        Object data = in.readObject();
        in.close();
        return data;
    }
}
