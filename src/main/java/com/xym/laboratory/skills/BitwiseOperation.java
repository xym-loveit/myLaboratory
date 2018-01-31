package com.xym.laboratory.skills;

/**
 * 使用位运算代替乘除法,提高运算效率
 *
 * @author xym
 */
public class BitwiseOperation {
    public static void main(String[] args) {


        System.out.println(16<<1);



        long startTime = System.currentTimeMillis();
        int a = 100;
        for (int i = 0; i < 10000000; i++) {
            a *= 2;
            a /= 2;
        }
        long endTime = System.currentTimeMillis();
        System.out.println((endTime - startTime) + " ms");
        System.out.println("----------------------------");
        startTime = System.currentTimeMillis();
        int b = 100;
        for (int i = 0; i < 10000000; i++) {
            b <<= 1;/*乘以2*/
            b >>= 1;/*除以2*/
        }
        endTime = System.currentTimeMillis();
        System.out.println((endTime - startTime) + " ms");
    }


}