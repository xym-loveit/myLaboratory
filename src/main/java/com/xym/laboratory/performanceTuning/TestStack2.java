package com.xym.laboratory.performanceTuning;

/**
 * @author xym
 */
public class TestStack2 {

    private int count = 0;

    public void recursion(long a, long b, long c) {
        long d = 0, e = 0, f = 0;//占用了栈空间（栈帧中的局部变量表）
        /**
         * 没有出口的递归函数，每次调用count加1
         */
        count++;
        recursion(a, b, c);
    }

    public static void main(String[] args) {
        /**
         * 调用递归，等待溢出
         */
        TestStack2 testStack = new TestStack2();
        try {
            /**
             * 递归调用与没有参数的递归调用相比，相同栈大小的情况下，调用次数会减少
             */
            testStack.recursion(1, 2, 3);
        } catch (Throwable e) {
            /**
             * 打印溢出的深度,统计调用次数
             */
            System.out.println("deep of stack is " + testStack.count);
            e.printStackTrace();
        }

        /**
         * 使用-Xss1M调整jvm参数扩大栈空间，观察调用深度
         */
    }

}
