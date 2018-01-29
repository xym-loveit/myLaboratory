package com.xym.laboratory.nios;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


/**
 * 缓存切片
 *
 * @author xym
 */
public class TestMain3 {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.put((byte) i);
        }
        buffer.position(3);
        buffer.limit(7);
        /**
         * 3/4/5/6 四个数据
         */
        ByteBuffer slice = buffer.slice();
        /**
         * remaining返回position和limit之间的元素个数
         */
        while (slice.remaining() > 0) {
            System.out.println(slice.get());
        }

        /**
         * 分片后的缓存和之前的缓存公用数据，由此我们修改下分片数据
         */
        for (int i = 0; i < slice.capacity(); i++) {
            byte b = slice.get(i);
            b *= 11;
            /**
             * 修改分片缓冲区数据
             */
            slice.put(i, b);
        }

        /**
         * 输出源缓冲区
         * 结果表明只有在子缓冲区窗口中的元素被改变了：
         *
         */
        buffer.position(0);
        buffer.limit(buffer.capacity());
        while (buffer.remaining() > 0) {
            System.out.println(buffer.get());
        }

        /**
         * 转换缓冲区为只读
         */
        ByteBuffer buffer1 = buffer.asReadOnlyBuffer();
        //buffer1.put(0, (byte) 12);
        buffer.put(0, (byte) 12);
    }
}
