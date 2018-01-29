package com.xym.laboratory.designPattern.singleton;

/**
 * @author xym
 */
public class SerSingleton implements java.io.Serializable {

    private String name;

    private SerSingleton() {
        System.out.println("Singleton is create");
        name = "SerSingleton";
    }

    private static SerSingleton singleton = new SerSingleton();

    public static SerSingleton getInstance() {
        return singleton;
    }

    public static void createString() {
        System.out.println("CreateString in SerSingleton");
    }

    private Object readResolve() {
        /**
         * 阻止生成新的示例，总是返回当前对象
         * 如果没有此方法，反序列化之后是一个新对象
         */
        return singleton;
    }

}
