package com.xym.laboratory.finals;

/**
 * desc
 *
 * @author xym
 */
public class Test4 {
    public static void main(String[] args) {
        MyClass2 myClass = new MyClass2();
        StringBuffer buffer = new StringBuffer("hello");
        myClass.changeValue(buffer);
        System.out.println(buffer.toString());
    }
}

class MyClass2 {

    void changeValue(StringBuffer buffer) {
        //buffer.append("world");
        buffer = new StringBuffer("lslslslsl");
    }
}