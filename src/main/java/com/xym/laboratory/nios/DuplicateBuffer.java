package com.xym.laboratory.nios;

import java.nio.ByteBuffer;

/**
 * 复制缓冲区和源缓冲区共享内存数据
 * so
 * 复制缓冲区的数据变动将引起源缓冲区的数据变动
 *
 * @author xym
 */
public class DuplicateBuffer {
    public static void main(String[] args) {
        /**
         * 分配15个字节的缓冲区
         */
        ByteBuffer allocate = ByteBuffer.allocate(15);
        for (int i = 0; i < 10; i++) {
            /**
             * 向缓冲区存入数据
             */
            allocate.put((byte) i);
        }

        /**
         * 复制当前缓冲区
         */
        ByteBuffer duplicate = allocate.duplicate();
        System.out.println("缓冲区复制后...");
        System.out.println(allocate);
        System.out.println(duplicate);
        /**
         * 切换复制缓冲区为读模式
         */
        duplicate.flip();
        System.out.println("复制缓冲区重置后");
        /**
         * 由此可以发现，复制缓冲区和源缓冲区各自维护自身的limit、position、mark参数
         */
        System.out.println(allocate);
        System.out.println(duplicate);
        /**
         * 向复制缓冲区中存入数据，由于复制缓冲区当前的position指为0
         * 所以放入的数据自然是被存入0位置
         */
        duplicate.put((byte) 100);
        System.out.println("复制缓冲区存入数据后...");
        System.out.println(allocate);
        System.out.println(duplicate);
        System.out.println(allocate.get(0));
        System.out.println(duplicate.get(0));
    }
}
