package com.xym.laboratory.nios;

import java.nio.ByteBuffer;

/**
 * 只读缓冲区，和源缓冲区共享内存数据，且无法更改，数据更安全
 * 对源缓冲区的数据更改只读缓冲区中可见
 *
 * @author xym
 */
public class ReadOnlyBuffer {
    public static void main(String[] args) {

        /**
         * 创建缓冲区并存入数据
         */
        ByteBuffer allocate = ByteBuffer.allocate(15);
        for (int i = 0; i < 10; i++) {
            allocate.put((byte) i);
        }
        /**
         * 创建只读缓冲区
         */
        ByteBuffer buffer = allocate.asReadOnlyBuffer();
        /**
         * 重置只读缓冲区
         */
        buffer.flip();
        while (buffer.hasRemaining()) {
            System.out.print(buffer.get());
        }
        System.out.println();
        /**
         * 修改原始缓冲区中的数据
         */
        allocate.put(2, (byte) 100);
        buffer.flip();
        while (buffer.hasRemaining()) {
            /**
             * 新的改动在只读缓冲区中可见
             */
            System.out.print(buffer.get());
        }
    }
}
