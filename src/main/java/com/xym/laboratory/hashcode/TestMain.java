package com.xym.laboratory.hashcode;

import java.util.HashMap;

/**
 * desc
 *
 * @author xym
 */
public class TestMain {
    public static void main(String[] args) {
       /* People p1 = new People("Jack", 12);
        System.out.println(p1.hashCode());

        HashMap<People, Integer> hashMap = new HashMap<People, Integer>();
        hashMap.put(p1, 1);

        System.out.println(hashMap.get(new People("Jack", 12)));*/


        People p1 = new People("Jack", 12);
        System.out.println(p1.hashCode());

        HashMap<People, Integer> hashMap = new HashMap<People, Integer>();
        hashMap.put(p1, 1);

        p1.setAge(13);//hashcode依赖age属性，get操作时hashcode已发生改变

        System.out.println(hashMap.get(p1));
    }
}