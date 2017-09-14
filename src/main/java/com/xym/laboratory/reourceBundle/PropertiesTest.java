package com.xym.laboratory.reourceBundle;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 2017/9/14.
 */
public class PropertiesTest {

    private static final String FILE_NAME = "properties";
    private static String name;
    private static int age;


    static {
        try {
            ResourceBundle resourceBundle = ResourceBundle.getBundle(FILE_NAME,Locale.ENGLISH);
            name = resourceBundle.getString("name");
            age = Integer.parseInt(resourceBundle.getString("age"));
        } catch (Exception e) {
            e.printStackTrace();
            name = "xym";
            age = 22;
        }
    }

    private void print() {
        System.out.println("name=" + name + "," + "age=" + age);
    }

    public static void main(String[] args) {
        PropertiesTest propertiesTest = new PropertiesTest();
        propertiesTest.print();
    }

}
