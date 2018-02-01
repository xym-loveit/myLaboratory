package com.xym.laboratory.concurrent;

import java.text.MessageFormat;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

/**
 * 消费者
 *
 * @author xym
 */
public class Consumer implements Runnable {
    /**
     * 内存缓冲区
     */
    private BlockingQueue<PCData> blockingQueue;

    private static final int SLEEPTIME = 1000;

    public Consumer(BlockingQueue<PCData> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    public void run() {
        System.out.println("start consumer id：" + Thread.currentThread().getId());
        /**
         * 随机等待时间
         */
        Random random = new Random();
        while (true) {
            try {
                /**
                 * 提取任务
                 */
                PCData take = blockingQueue.take();
                if (take != null) {
                    int re = take.getIntData() * take.getIntData();
                    System.out.println(MessageFormat.format("{0}*{1}={2}", take.getIntData(), take.getIntData(), re));
                    Thread.sleep(random.nextInt(SLEEPTIME));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }
}
