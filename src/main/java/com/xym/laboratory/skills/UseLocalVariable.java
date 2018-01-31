package com.xym.laboratory.skills;

/**
 * 尽量多使用局部变量，少用实例变量或静态变量
 *
 * @author xym
 */
public class UseLocalVariable {
    static int b = 0;

    public static void main(String[] args) {
        /**
         * 局部变量
         */
        long startTime = System.currentTimeMillis();
        int a = 0;/*局部变量*/
        for (int i = 0; i < 1000000000; i++) {
            a++;
        }
        long endTime = System.currentTimeMillis();
        System.out.println((endTime - startTime) + " ms");

        System.out.println("------------------------");
        /**
         * 静态变量
         */
        startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000000000; i++) {
            b++;
        }
        endTime = System.currentTimeMillis();
        System.out.println((endTime - startTime) + " ms");
    }
}
