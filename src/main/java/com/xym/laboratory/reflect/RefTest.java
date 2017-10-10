package com.xym.laboratory.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * desc
 *
 * @author xym
 */
public class RefTest {
    public static void main(String[] args) {

        //获取类装载器
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Class<?> aClass = contextClassLoader.loadClass("com.xym.laboratory.reflect.Car");
            //反射获取构造函数
            Constructor<?> declaredConstructor = aClass.getDeclaredConstructor(null);

            //反射实例化对象
            Car car = (Car) declaredConstructor.newInstance();

            //反射设置属性
            Method brand = aClass.getMethod("setBrand", String.class);
            Method color = aClass.getMethod("setColor", String.class);
            Method maxSpeed = aClass.getMethod("setMaxSpeed", int.class);

            brand.invoke(car, "红旗");
            color.invoke(car, "黑色");
            maxSpeed.invoke(car, 200);

            car.introduce();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


    }
}