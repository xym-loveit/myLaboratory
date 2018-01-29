package com.xym.laboratory.finals;

import java.util.Hashtable;

/**
 * desc
 *
 * @author xym
 */
public class Test2 {
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 10;
    static final int MAXIMUM_CAPACITY = 1 << 30;

    public static void main(String[] args) {
        Hashtable hashtable = new Hashtable();
        //hashtable.put(null,111);

       /* String a = "hello2";
        final String b = getHello();

        String c = b + 2;
        System.out.println((a == c));*/


        System.out.println(DEFAULT_INITIAL_CAPACITY);
        System.out.println(MAXIMUM_CAPACITY);

    }

    public static String getHello() {
        return "hello";
    }
}