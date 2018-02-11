package com.xym.laboratory.performanceTuning;

/**
 * 通过pidstat观察应用程序占用CPU及占用CPU的线程
 *
 * @author xym
 */
public class HoldCPUMain {
    public static void main(String[] args) {
        /**
         * 开启空闲线程和占用CPU的线程
         */
        new Thread(new HoldCPUTask()).start();
        new Thread(new LazyTask()).start();
        new Thread(new LazyTask()).start();
        new Thread(new LazyTask()).start();
    }

    public static class HoldCPUTask implements Runnable {
        public void run() {
            while (true) {
                /**
                 * 占用CPU
                 */
                double d = Math.random() * Math.random();
            }
        }
    }

    public static class LazyTask implements Runnable {
        public void run() {
            try {
                while (true) {
                    /**
                     *  空闲线程
                     */
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
