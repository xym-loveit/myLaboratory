package com.xym.laboratory.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 生产者和消费者模型
 *
 * @author xym
 */
public class ProducerAndConsumerMain {
    public static void main(String[] args) {
        BlockingQueue queue = new LinkedBlockingQueue<PCData>(10);
        /**
         * 创建生产者
         */
        Producer producer1 = new Producer(queue);
        Producer producer2 = new Producer(queue);
        Producer producer3 = new Producer(queue);
        /**
         *创建消费者
         */
        Consumer consumer1 = new Consumer(queue);
        Consumer consumer2 = new Consumer(queue);
        Consumer consumer3 = new Consumer(queue);

        /**
         * 创建线程池
         */
        ExecutorService executorService = Executors.newCachedThreadPool();
        /**
         * 运行生产者
         */
        executorService.submit(producer1);
        executorService.submit(producer2);
        executorService.submit(producer3);
        /**
         * 运行消费者
         */
        executorService.submit(consumer1);
        executorService.submit(consumer2);
        executorService.submit(consumer3);

        try {
            Thread.sleep(1000 * 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /**
         *停止生产者
         */
        producer1.stop();
        producer2.stop();
        producer3.stop();

        try {
            Thread.sleep(3000);
            /**
             * 停止线程池
             */
            executorService.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
