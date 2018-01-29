package com.xym.laboratory.designPattern.singleton;

import java.io.*;

/**
 * @author xym
 */
public class TestMain {
    public static void main(String[] args) {
        SerSingleton singleton = SerSingleton.getInstance();
        SerSingleton singleton2 = null;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("SerSingleton.txt");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(singleton);
            objectOutputStream.flush();
            objectOutputStream.close();

            FileInputStream inputStream = new FileInputStream("SerSingleton.txt");
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            singleton2 = (SerSingleton) objectInputStream.readObject();
            System.out.println("singleton.equals=" + (singleton.equals(singleton2)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
