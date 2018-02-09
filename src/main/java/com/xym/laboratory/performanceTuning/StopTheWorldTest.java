package com.xym.laboratory.performanceTuning;

import java.util.HashMap;

/**
 * stop the world 测试案例
 * 通过对JVM的垃圾回收产生的stop the world现象
 * 观察stop the world对应用程序暂停的影响
 * <p>
 * 辅助Jvm配置参数
 * -Xmx512M -Xms512M -XX:+UseSerialGC -Xloggc:gc.log -XX:+PrintGCDetails
 *
 * @author xym
 */
public class StopTheWorldTest {
    public static void main(String[] args) {

        MyThread myThread = new MyThread();
        MyPrintThread myPrintThread = new MyPrintThread();

        myPrintThread.start();
        myThread.start();
    }


    private static class MyThread extends Thread {
        HashMap hashMap = new HashMap();

        @Override
        public void run() {
            try {
                while (true) {
                    /**
                     * 防止内存溢出
                     */
                    if (hashMap.size() * 512 / 1024 / 1024 >= 400) {
                        hashMap.clear();
                        System.out.println("clean map");
                    }

                    byte[] b1;
                    for (int i = 0; i < 100; i++) {
                        /**
                         * 模拟内存占用
                         */
                        b1 = new byte[512];
                        hashMap.put(System.nanoTime(), b1);
                        Thread.sleep(1);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 每毫秒打印时间信息
     */
    private static class MyPrintThread extends Thread {
        public static final long startTime = System.currentTimeMillis();

        @Override
        public void run() {
            try {
                while (true) {
                    long t = System.currentTimeMillis() - startTime;
                    System.out.println(t / 1000 + "." + t % 1000);
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

}
