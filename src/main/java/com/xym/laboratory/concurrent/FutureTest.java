package com.xym.laboratory.concurrent;


import java.util.concurrent.*;

/**
 * Future模式的核心在于除去了主函数的等待时间,并使得原本需要等待的时间段可以用来处理其他业务逻辑，从而充分利用计算机资源
 *
 * @author xym
 */
public class FutureTest {
    public static void main(String[] args) {



        /**
         * 构造FutureTask
         */
        FutureTask<String> futureTask = new FutureTask<String>(new RealDate("a"));
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        /**
         * 开启异步线程执行RealDate的call方法
         */
        Future<?> submit = executorService.submit(futureTask);
        System.out.println("请求完毕");

        try {
            /**
             * 模拟其他的业务操作
             */
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /**
         * 如果此时call方法没有执行完则依然会一直等待
         */
        try {
            System.out.println(futureTask.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (submit.isDone()) {
            executorService.shutdown();
        }

    }
}

class RealDate implements Callable<String> {
    private String para;

    public RealDate(String para) {
        this.para = para;
    }

    public String call() throws Exception {
        /**
         * 这里是真实的业务逻辑其执行可能很慢
         */
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < 10; i++) {
            stringBuffer.append(para);
            Thread.sleep(100);
        }
        return stringBuffer.toString();
    }
}
