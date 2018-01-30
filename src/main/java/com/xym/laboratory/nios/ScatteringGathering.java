package com.xym.laboratory.nios;


import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 散射(读取channel到多个buffer)和聚集（多个buffer写入到channel）
 * 适合操作结构化数据（固定格式，如：当前例子使用 书名+作者 形式）
 * <p>
 * <p>
 * GatheringByteChannel接口提供聚集能力, ScatteringByteChannel提供散射能力
 *
 * @author xym
 */
public class ScatteringGathering {
    public static void main(String[] args) {

        /**
         *
         */
        ByteBuffer bookName = ByteBuffer.wrap("java性能优化技巧".getBytes());
        ByteBuffer author = ByteBuffer.wrap("葛一鸣".getBytes());
        /**
         * 书名长度
         */
        int blen = bookName.limit();
        /**
         *作者长度
         */
        int alen = author.limit();
        ByteBuffer[] bbs = new ByteBuffer[]{bookName, author};
        File file = new File("book.txt");

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream outputStream = new FileOutputStream(file);
            FileChannel channel = outputStream.getChannel();
            /**
             * 聚集写文件（一次写入多个缓冲区）
             */
            channel.write(bbs);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        /**
         * 使用散射读取操作，将文件读取到多个缓冲区中
         *
         */
        //存放书名
        ByteBuffer nameBuffer = ByteBuffer.allocate(blen);
        //存放作者
        ByteBuffer authorBuffer = ByteBuffer.allocate(alen);
        try {
            ByteBuffer book[] = new ByteBuffer[]{nameBuffer, authorBuffer};
            FileInputStream inputStream = new FileInputStream("book.txt");
            FileChannel channel = inputStream.getChannel();
            long read = channel.read(book);
            System.out.println(new String((book[0]).array(), "UTF-8"));
            System.out.println(new String((book[1]).array(), "UTF-8"));
            inputStream.close();
            channel.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
