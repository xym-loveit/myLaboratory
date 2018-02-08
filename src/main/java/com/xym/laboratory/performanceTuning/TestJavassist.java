package com.xym.laboratory.performanceTuning;

import javassist.ClassPool;
import javassist.CtClass;

/**
 * 使用Javassist字节码生成工具测试持久代溢出情况
 * <p>
 * 第一次设置持久代参数-XX:PermSize=4M -XX:MaxPermSize=4M
 * 第二次设置持久代参数-XX:PermSize=11M -XX:MaxPermSize=11M
 *
 * @author xym
 */
public class TestJavassist {

    public static void main(String[] args) {
        for (int i = 1; i <= Integer.MAX_VALUE; i++) {
            CtClass ctClass = ClassPool.getDefault().makeClass("Geym" + i);
            try {
                ctClass.setSuperclass(ClassPool.getDefault().get("com.xym.laboratory.performanceTuning.JavaBeanObject"));
                Class aClass = ctClass.toClass();
                /**
                 * 生成动态类实例
                 */
                JavaBeanObject beanObject = (JavaBeanObject) aClass.newInstance();
            } catch (Throwable e) {
                System.out.println("Create new class count " + i);
                e.printStackTrace();
            }
        }
    }

}
