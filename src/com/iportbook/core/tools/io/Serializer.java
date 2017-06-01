package com.iportbook.core.tools.io;

import java.io.*;

public class Serializer {
    public static void write(String filename, Object data) throws IOException {
        FileOutputStream fos = new FileOutputStream(filename);
        ObjectOutputStream oos = new ObjectOutputStream(fos );
        oos.writeObject(data);
        oos.close();
        fos.close();
    }

    public static Object read(String filename) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(filename);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object data = ois.readObject();
        ois.close();
        fis.close();
        return data;
    }
}
