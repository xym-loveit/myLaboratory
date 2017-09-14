package com.xym.laboratory;

/**
 * Created by Administrator on 2017/8/1.
 */
public class TestThreadLocal2 {

    /**
     * 公用同一个线程本地变量,但由于threadlocal的魔力，使得各个线程的值相互独立，互不影响
     */
    private static final ThreadLocal<Integer> localValue = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };


    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            new MyThread2(i).start();
        }
    }

    static class MyThread2 extends Thread {
        int index;

        public MyThread2(int index) {
            this.index = index;
        }

        @Override
        public void run() {
            System.out.println("线程" + index + "的初始value:" + localValue.get());
            for (int i = 0; i < 10; i++) {
                localValue.set(localValue.get() + i);
            }
            System.out.println("线程" + index + "的累加value:" + localValue.get());
        }
    }
}