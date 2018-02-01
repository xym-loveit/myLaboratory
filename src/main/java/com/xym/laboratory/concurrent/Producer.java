package com.xym.laboratory.concurrent;


import java.util.Collections;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 生产者
 *
 * @author xym
 */
public class Producer implements Runnable {
    private volatile boolean isRunning = true;
    /**
     * 内存缓冲区
     */
    private BlockingQueue<PCData> blockingQueue;
    /**
     * 总数，原子操作
     */
    private static AtomicInteger count = new AtomicInteger();

    private static final int SLEEPTIME = 1000;

    public Producer(BlockingQueue<PCData> blockingQueue) {
        this.blockingQueue = blockingQueue;

    }

    public void run() {
        PCData pcData = null;
        Random r = new Random();
        System.out.println("start producer id " + Thread.currentThread().getId());

        while (isRunning) {
            try {
                Thread.sleep(r.nextInt(SLEEPTIME));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            /**
             * 构造任务数据
             */
            pcData = new PCData(count.incrementAndGet());
            System.out.println(pcData + " is put into queue");
            try {
                if (!blockingQueue.offer(pcData, 2, TimeUnit.SECONDS)) {
                    System.out.println("failed to put data: " + pcData);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }

    }

    public void stop() {
        isRunning = false;
    }
}
