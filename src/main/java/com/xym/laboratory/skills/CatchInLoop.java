package com.xym.laboratory.skills;

/**
 * 循环中使用try catch对性能会造成影响
 *
 * @author xym
 */
public class CatchInLoop {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        int a = 0;
        for (int i = 0; i < 1000000000; i++) {
            try {
                a++;
            } catch (Exception e) {

            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("expend " + (endTime - startTime) + " ms");
    }
}
