package com.xym.laboratory.nios;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * desc
 *
 * @author xym
 */
public class InAndOut {

    public static void main(String[] args) {
        //writeData();
        //readAndWrite();
        //warp();
        //slice();分片

        ByteBuffer buffer = ByteBuffer.allocate(10);
        ByteBuffer buffer1 = buffer.asReadOnlyBuffer();
        buffer.put((byte) 12);
        buffer1.put((byte) 12);

    }

    private static void slice() {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.put((byte) i);
        }
        System.out.println("position=" + buffer.position() + "--limit=" + buffer.limit() + "--capacity=" + buffer.capacity());

        buffer.position(3);
        buffer.limit(7);
        ByteBuffer slice = buffer.slice();//缓冲区分片操作后，其本质是使用同一个数组

        System.out.println("1111 position=" + buffer.position() + "--limit=" + buffer.limit() + "--capacity=" + buffer.capacity());

        System.out.println("2222 position=" + slice.position() + "--limit=" + slice.limit() + "--capacity=" + slice.capacity());


        buffer.clear();
        //buffer.position(0);//position=0,limit=capacity（和clear操作等价）---方便遍历
        //buffer.limit(buffer.capacity());

        while (buffer.remaining() > 0) {
            System.out.print(buffer.get());
            System.out.print("\t");
        }

        System.out.println("\n");

        //slice.position(0);
        //slice.limit(slice.capacity());

        slice.clear();//position=0,limit=capacity（和clear操作等价）

        while (slice.remaining() > 0) {
            System.out.print(slice.get());
            System.out.print("\t");
        }

        //尝试改变分片后的子缓冲区元素

        for (int i = 0; i < slice.capacity(); ++i) {
            byte b = slice.get(i);
            b *= 11;
            slice.put(i, b);
        }

        System.out.println("\n改变后——————————————————————");

        buffer.clear();
        //改变后
        while (buffer.remaining() > 0) {
            System.out.print(buffer.get());
            System.out.print("\t");
        }

        System.out.println("\n---------------------------------");

        slice.clear();//position=0,limit=capacity（和clear操作等价）

        while (slice.remaining() > 0) {
            System.out.print(slice.get());
            System.out.print("\t");
        }
        //结论：我们知道缓冲区和子缓冲区（分片缓冲区）共享同一个底层数据数组
    }


    private static void warp() {
        FileChannel channel = null;
        byte[] buffer = new byte[30];
        byte[] message = "abcdef".getBytes();
        try {
            channel = new FileOutputStream("test.txt").getChannel();
            ByteBuffer wrap = ByteBuffer.wrap(buffer);
            wrap.put(message);
            try {
                wrap.flip();
                channel.write(wrap);
                for (byte b : buffer) {
                    System.out.println((char) b);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void readAndWrite() {
        FileInputStream in = null;
        ByteBuffer buffer = ByteBuffer.allocate(1);
        try {
            in = new FileInputStream("out.txt");
            FileChannel channel = in.getChannel();
            FileOutputStream out = new FileOutputStream("copyfile.txt");
            FileChannel fcout = out.getChannel();
            int count = 1;
            try {
                while (true) {
                    System.out.println("read count " + count);
                    buffer.clear();//将position=0，limit为capacity（容量），将容器重置，保证留出位置存放数据
                    int read = channel.read(buffer);
                    if (read == -1) {
                        break;
                    }
                    buffer.flip();
                    fcout.write(buffer);
                    count++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fcout != null) {
                        fcout.close();
                    }
                    if (channel != null) {
                        channel.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void writeData() {
        byte[] message = new String("nio测试").getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        FileOutputStream out = null;
        FileChannel channel = null;
        try {
            out = new FileOutputStream("out.txt");
            channel = out.getChannel();
            try {
                //put引起position的变化（递增）
                buffer.put(message);
                buffer.flip();//这一步很重要，将position设置为0，limit设置为position，position-----limit之间的数据视为有效数据
                channel.write(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (channel != null) {
                    channel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}