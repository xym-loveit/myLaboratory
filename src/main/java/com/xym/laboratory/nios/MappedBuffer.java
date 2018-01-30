package com.xym.laboratory.nios;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 文件映射到内存
 *
 * @author xym
 */
public class MappedBuffer {
    public static void main(String[] args) {
        try {
            RandomAccessFile raf = new RandomAccessFile("mapped.txt", "rw");
            FileChannel channel = raf.getChannel();
            /**
             * 将文件映射到内存中
             */
            MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, raf.length());
            while (map.hasRemaining()) {
                System.out.print(map.get());
            }
            /**
             * 修改内存中数据会持久化到磁盘中
             */
            map.put(0, (byte) 65);
            channel.close();
            raf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
