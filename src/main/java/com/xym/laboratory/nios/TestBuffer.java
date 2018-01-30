package com.xym.laboratory.nios;

import java.nio.ByteBuffer;

/**
 * 掌握buffer缓冲区的三个参数
 *
 * @author xym
 */
public class TestBuffer {
    public static void main(String[] args) {

        /**
         * 开辟15个容量的jvm 堆缓冲区
         */
        ByteBuffer allocate = ByteBuffer.allocate(15);
        System.out.printf("position=%s,limit=%s,capacity=%s", allocate.position(), allocate.limit(), allocate.capacity());
        for (int i = 0; i < 10; i++) {
            /**
             * 放入10个元素
             */
            allocate.put((byte) i);
        }
        System.out.println();
        System.out.printf("position=%s,limit=%s,capacity=%s", allocate.position(), allocate.limit(), allocate.capacity());
        /**
         *  切换为读模式
         *  limit=position
         *  position=0
         *  capacity不变
         */
        allocate.flip();
        System.out.println();
        System.out.printf("position=%s,limit=%s,capacity=%s", allocate.position(), allocate.limit(), allocate.capacity());
        System.out.println();
        for (int i = 0; i < 5; i++) {
            /**
             * position跟随移动
             */
            System.out.println(allocate.get());
        }
        System.out.println();
        System.out.printf("position=%s,limit=%s,capacity=%s", allocate.position(), allocate.limit(), allocate.capacity());
        /**
         *  切换为读模式
         *  limit=position
         *  position=0
         *  capacity不变
         */
        allocate.flip();
        System.out.println();
        System.out.printf("position=%s,limit=%s,capacity=%s", allocate.position(), allocate.limit(), allocate.capacity());

    }
}
