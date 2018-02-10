package com.xym.laboratory.performanceTuning;

import java.util.HashMap;

/**
 * 观察通过不同类型的垃圾收集器对应用程序性能的影响
 * <p>
 * -Xms512M -Xmx512M -XX:+UseParNewGC 耗时431毫秒
 * -Xms512M -Xmx512M -XX:+UseParallelOldGC -XX:ParallelGCThreads=8 耗时439毫秒
 * -Xms512M -Xmx512M -XX:+UseSerialGC 耗时504毫秒
 * -Xms512M -Xmx512M -XX:+UseConcMarkSweepGC 耗时453毫秒
 *
 * @author xym
 */
public class GCTimeTest {
    static HashMap hashMap = new HashMap();

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            /**
             * 保护内存不溢出
             */
            if (hashMap.size() * 512 / 1024 / 1024 >= 300) {
                hashMap.clear();
                System.out.println("clean map");
            }
            byte[] b1;
            for (int i1 = 0; i1 < 100; i1++) {
                b1 = new byte[512];
                /**
                 * 模拟对内存的消耗
                 */
                hashMap.put(System.nanoTime(), b1);
            }

        }
        System.out.println(System.currentTimeMillis() - startTime + " ms");
    }
}
