package com.xym.laboratory.nios;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 拷贝大文件测试
 *
 * @author xym
 */
public class CopyFileWithNIO {
    public static void main(String[] args) {

        String srcName = "D:\\BaiduNetdiskDownload\\CentOS-7-x86_64-DVD-1511.iso";
        String destName = "D:\\BaiduNetdiskDownload\\CentOS-7-x86_64-DVD-1511_bak.iso";
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        FileChannel readChannel = null;
        FileChannel writeChannel = null;
        long startTime = System.currentTimeMillis();
        try {
            inputStream = new FileInputStream(srcName);
            outputStream = new FileOutputStream(destName);
            /**
             * 读文件通道
             */
            readChannel = inputStream.getChannel();
            /**
             * 写文件通道
             */
            writeChannel = outputStream.getChannel();
            /**
             * 缓冲区，jvm heap内存，数据中转站
             */
            //ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024 * 10);
            /**
             * 采用直接缓冲区拷贝，使用计算机物理内存
             */
            ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 1024 * 10);
            while (true) {
                /**
                 * 为写入做准备
                 * position=0
                 * limit=capacity=数组容量
                 */
                buffer.clear();
                int read = readChannel.read(buffer);
                if (read == -1) {
                    break;
                }
                /**
                 * 为读取做准备
                 * limit=position
                 * position=0
                 */
                buffer.flip();
                writeChannel.write(buffer);
            }
            long endTime = System.currentTimeMillis();
            System.out.printf("耗时=%s\tms", (endTime - startTime));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                /**
                 * 关闭流操作
                 */
                if (null != writeChannel) {
                    writeChannel.close();
                }
                if (null != readChannel) {
                    readChannel.close();
                }
                if (null != outputStream) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
