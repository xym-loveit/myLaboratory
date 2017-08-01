package com.xym.laboratory;

import java.util.concurrent.CountDownLatch;

/**
 * Created by Administrator on 2017/8/1.
 */
public class TestThreadLocal {

    public static void main(String[] args) throws InterruptedException {

        /**
         * 虽然共享了一份ThreadLocal类型的变量
         *但每个线程独享一份副本，互不影响
         */
        TestThreadLocal threadLocal = new TestThreadLocal();
        CountDownLatch downLatch = new CountDownLatch(3);

        MyThread myThread1 = new MyThread(threadLocal, downLatch);
        MyThread myThread2 = new MyThread(threadLocal, downLatch);
        MyThread myThread3 = new MyThread(threadLocal, downLatch);

        myThread1.start();
        myThread2.start();
        myThread3.start();

        downLatch.await();
        System.out.println("子线程都已执行完毕，主线程结束");

        System.out.println(Thread.currentThread().getId() + "--" + Thread.currentThread().getName() + threadLocal.getNextNum());


    }

    private static ThreadLocal<Integer> seqNum = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

    public int getNextNum() {
        seqNum.set(seqNum.get() + 1);
        return seqNum.get();
    }

}

class MyThread extends Thread {

    private TestThreadLocal threadLocal;

    private CountDownLatch downLatch;

    public MyThread(TestThreadLocal threadLocal, CountDownLatch downLatch) {
        this.threadLocal = threadLocal;
        this.downLatch = downLatch;
    }

    public void run() {
        for (int i = 0; i < 3; i++) {
            System.out.println(Thread.currentThread().getId() + "--" + Thread.currentThread().getName() + "--" + threadLocal.getNextNum());
        }
        downLatch.countDown();
    }
}