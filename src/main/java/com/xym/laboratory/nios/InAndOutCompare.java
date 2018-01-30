package com.xym.laboratory.nios;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 几种操作文件的方式对比
 * <p>
 * 1、传统io流
 * 2、nio缓冲区
 * 3、文件直接映射内存
 *
 * @author xym
 */
public class InAndOutCompare {
    private static final int numOfInt = 4000000;

    public static void main(String[] args) {
      /*  try {
            System.out.println(new String(int2Byte(65), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/


        streamProcess();
        nioProcess();
        mappedProcess();
    }

    /**
     * 文件直接映射内存方式
     */
    public static void mappedProcess() {
        long startTime = System.currentTimeMillis();
        /**
         * 写文件
         */
        try {
            RandomAccessFile rw = new RandomAccessFile("mapped.temp", "rw");
            FileChannel channel = rw.getChannel();
            /*文件映射内存方式*/
            MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, numOfInt * 4);
            IntBuffer intBuffer = map.asIntBuffer();
            for (int i = 0; i < numOfInt; i++) {
                intBuffer.put(i);
            }
            if (rw != null) {
                rw.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("文件直接映射内存,写入耗时=" + (endTime - startTime) + " ms");

        /**
         *  读取文件
         */
        startTime = System.currentTimeMillis();
        try {
            FileInputStream inputStream = new FileInputStream(new File("mapped.temp"));
            FileChannel channel = inputStream.getChannel();
            /**
             * 文件映射到内存
             */
            MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            IntBuffer intBuffer = map.asIntBuffer();
            while (intBuffer.hasRemaining()) {
                intBuffer.get();
            }
            if (null != inputStream) {
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        endTime = System.currentTimeMillis();
        System.out.println("文件直接映射内存,读取耗时=" + (endTime - startTime) + " ms");
    }


    /**
     * nio方式
     */
    public static void nioProcess() {
        long startTime = System.currentTimeMillis();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File("nio.temp"));
            FileChannel channel = fileOutputStream.getChannel();
            /**
             * 这里之所以声明大小为numOfInt * 4是因为一个int占用4个字节
             */
            ByteBuffer allocate = ByteBuffer.allocate(numOfInt * 4);
            for (int i = 0; i < numOfInt; i++) {
                allocate.put(int2Byte(i));
            }
            allocate.flip();
            channel.write(allocate);
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("nio写入耗时=" + (endTime - startTime) + " ms");

        startTime = System.currentTimeMillis();
        try {
            FileInputStream inputStream = new FileInputStream(new File("nio.temp"));
            FileChannel channel = inputStream.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(numOfInt * 4);
            buffer.clear();
            channel.read(buffer);
            inputStream.close();
            buffer.flip();
            while (buffer.hasRemaining()) {
                byte2int(buffer.get(), buffer.get(), buffer.get(), buffer.get());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        endTime = System.currentTimeMillis();
        System.out.println("nio读取耗时=" + (endTime - startTime) + " ms");

    }

    /**
     * 整数转为byte数组
     *
     * @param i
     * @return
     */
    private static byte[] int2Byte(int i) {
        byte[] targets = new byte[4];
        targets[3] = (byte) (i & 0xff);
        targets[2] = (byte) ((i >> 8) & 0xff);
        targets[1] = (byte) ((i >> 16) & 0xff);
        targets[0] = (byte) (i >> 24);/*最高位无符号右移*/
        return targets;
    }


    /**
     * byte转为int
     *
     * @param b1
     * @param b2
     * @param b3
     * @param b4
     * @return
     */
    private static int byte2int(byte b1, byte b2, byte b3, byte b4) {
        return (b1 & 0xff) << 24 | (b2 & 0xff) << 16 | (b3 & 0xff) << 8 | (b4 & 0xff);
    }

    /**
     * 传统io
     */
    public static void streamProcess() {
        try {
            /**
             * 传统流写文件
             */
            long startTime = System.currentTimeMillis();
            DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("stream.temp")));

            for (int i = 0; i < numOfInt; i++) {
                dataOutputStream.writeInt(i);
            }

            if (null != dataOutputStream) {
                dataOutputStream.close();
            }
            long endTime = System.currentTimeMillis();
            System.out.println("传统写入耗时=" + (endTime - startTime) + " ms");

            /**
             * 传统流读文件
             */
            startTime = System.currentTimeMillis();
            DataInputStream inputStream = new DataInputStream(new BufferedInputStream(new FileInputStream("stream.temp")));
            for (int i = 0; i < numOfInt; i++) {
                inputStream.readInt();
            }
            if (null != inputStream) {
                inputStream.close();
            }
            endTime = System.currentTimeMillis();
            System.out.println("传统读取耗时=" + (endTime - startTime) + " ms");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
