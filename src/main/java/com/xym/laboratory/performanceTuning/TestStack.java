package com.xym.laboratory.performanceTuning;

/**
 * @author xym
 */
public class TestStack {

    private int count = 0;

    public void recursion() {
        /**
         * 没有出口的递归函数，每次调用count加1
         */
        count++;
        recursion();
    }

    public static void main(String[] args) {
        /**
         * 调用递归，等待溢出
         */
        TestStack testStack = new TestStack();
        try {
            testStack.recursion();
        } catch (Throwable e) {
            /**
             * 打印溢出的深度
             */
            System.out.println("deep of stack is " + testStack.count);
            e.printStackTrace();
        }

        /**
         * 使用-Xss1M调整jvm参数扩大栈空间，观察调用深度
         */
    }

}
