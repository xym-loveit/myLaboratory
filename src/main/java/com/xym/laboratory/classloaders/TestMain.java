package com.xym.laboratory.classloaders;

/**
 * 三个类加载器，AppClassLoader负责加载classpath下面类文件，ExtClassLoader负责加载jre扩展目录ext中的jar文件，根加载器不是ClassLoader子类且为c++编写，所以我们看到它（null），根装载器负责加载jre核心类库
 *
 * @author xym
 */
public class TestMain {
    public static void main(String[] args) {


        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();


        //AppClassLoader
        System.out.println("current classloader:" + classLoader);
        //ExtClassLoader
        System.out.println("parent classloader:" + classLoader.getParent());
        //null
        System.out.println("grandparent classloader:" + classLoader.getParent().getParent());
    }
}