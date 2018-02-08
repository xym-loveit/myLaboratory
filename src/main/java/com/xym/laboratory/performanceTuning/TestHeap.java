package com.xym.laboratory.performanceTuning;

import java.util.Vector;

/**
 * 测试-Xmx，最大对内存参数
 *
 * @author xym
 */
public class TestHeap {
    public static void main(String[] args) {
        Vector<Object> objects = new Vector<Object>();
        for (int i = 1; i <= 10; i++) {
            /**
             *   每个循环分配1M内存
             */
            byte[] bytes = new byte[1024 * 1024];
            /**
             * 强引用使gc不能释放空间
             */
            objects.add(bytes);

            if (i % 3 == 0) {
                objects.clear();
            }

            System.out.println(i + " M is allocated");

        }

        /**
         * maxMemory获取系统可用最大堆
         */
        System.out.println("Max memory :" + (Runtime.getRuntime().maxMemory() / 1024 / 1024) + " M ");

    }
}
