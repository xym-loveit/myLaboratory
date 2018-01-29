package com.xym.laboratory.designPattern.singleton;


/**
 * 比较优越的一个单例模式
 *
 * @author xym
 */
public class StaticSingleton {

    private StaticSingleton() {
        System.out.println("StaticSingleton is create");
    }

    private static class StaticSingletonHolder {
        private static StaticSingleton singleton = new StaticSingleton();
    }

    public static StaticSingleton getInstance() {
        return StaticSingletonHolder.singleton;
    }

    public static void main(String[] args) {
        StaticSingleton.getInstance();
    }
}
