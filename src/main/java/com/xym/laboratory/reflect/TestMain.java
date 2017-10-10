package com.xym.laboratory.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 使用反射 突破java类访问修饰符限制
 *
 * @author xym
 */
public class TestMain {

    public static void main(String[] args) throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Class<?> privateCar = classLoader.loadClass("com.xym.laboratory.reflect.PrivateCar");
        PrivateCar car = (PrivateCar) privateCar.newInstance();

        Field color = privateCar.getDeclaredField("color");
        //取消java访问检查，访问private变量
        color.setAccessible(true);
        color.set(car, "红色");

        Method method = privateCar.getDeclaredMethod("drive", null);
        //取消java访问检查，访问protected方法
        method.setAccessible(true);
        method.invoke(car, null);
    }
}