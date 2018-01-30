package com.xym.laboratory.nios;

import java.nio.ByteBuffer;

/**
 * 测试mark和reset使用
 *
 * @author xym
 */
public class TestMarkAndReset {
    public static void main(String[] args) {
        /**
         * 实例化字节缓冲区
         */
        ByteBuffer allocate = ByteBuffer.allocate(15);
        for (int i = 0; i < 10; i++) {
            allocate.put((byte) i);
        }
        /**
         * 切换为读模式
         */
        allocate.flip();
        for (int i = 0; i < allocate.limit(); i++) {
            System.out.print(allocate.get());
            if (i == 4) {
                /**
                 * 在第四个位置作mark
                 */
                allocate.mark();
                System.out.print("(mark at " + i + ")");
            }
        }

        /**
         * 回到mark位置4，并处理后续数据
         * position=mark=4
         */
        allocate.reset();
        System.out.println("\nreset to mark ");
        while (allocate.hasRemaining()) {
            System.out.println(allocate.get());
        }
        System.out.println();
    }
}
