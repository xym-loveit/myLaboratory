package com.xym.laboratory.performanceTuning;

/**
 * JIT编译器测试用例
 * <p>
 * -XX:CompileThreshold=1500 -XX:+PrintCompilation -XX:+CITime
 * <p>
 * -XX:+CITime：打印出JIT编译耗时
 * -XX:+PrintCompilation：打印JIT编译信息
 * -XX:CompileThreshold=1500：JIT编译阈值，当函数的调用次数超过CompileThreshold时，JIT编译器就将字节码编译成本地机器码
 *
 * @author xym
 */
public class TestJIT {

    static long i = 0;

    public static void testJIT() {
        i++;
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        for (int i1 = 0; i1 < 1488; i1++) {
            testJIT();
        }
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
    }

}
