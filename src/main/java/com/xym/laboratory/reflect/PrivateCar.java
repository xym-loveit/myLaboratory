package com.xym.laboratory.reflect;

/**
 * desc
 *
 * @author xym
 */
public class PrivateCar {

    //私有变量，外界无发访问
    private String color;

    //私有方法，外界无发访问
    protected void drive() {
        System.out.println("drive private car!the color is " + color);
    }

}