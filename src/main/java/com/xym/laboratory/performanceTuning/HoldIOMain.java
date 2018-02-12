package com.xym.laboratory.performanceTuning;

import java.io.*;

/**
 * 模拟大量的IO写操作，通过pidstat观察IO情况
 *
 * @author xym
 */
public class HoldIOMain {
    public static void main(String[] args) {
        new Thread(new HoldIOTask()).start();
        new Thread(new LazyTask()).start();
        new Thread(new LazyTask()).start();
        new Thread(new LazyTask()).start();
    }

    public static class HoldIOTask implements Runnable {
        public void run() {
            try {
                while (true) {
                    FileOutputStream outputStream = new FileOutputStream(new File("temp"));
                    /**
                     * 模拟大量的写操作
                     */
                    for (int i = 0; i < 10000; i++) {
                        outputStream.write(i);
                    }
                    outputStream.close();
                    FileInputStream inputStream = new FileInputStream(new File("temp"));
                    /**
                     * 大量读操作
                     */
                    while (inputStream.read() != -1) {
                    }
                    inputStream.close();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static class LazyTask implements Runnable {
        public void run() {
            try {
                /**
                 * 一个空闲的线程
                 */
                while (true) {
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
