package com.xym.laboratory.nios;


import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 读写缓冲区切换
 *
 * @author xym
 */
public class TestMain2 {
    public static void main(String[] args) {

        try {
            FileInputStream inputStream = new FileInputStream("outData.txt");
            FileChannel channel = inputStream.getChannel();
//            PrintWriter pw = new PrintWriter(System.out);

            FileOutputStream outputStream = new FileOutputStream("outData2.txt");
            FileChannel channel1 = outputStream.getChannel();
            /**
             * 读数据buffer
             */
            ByteBuffer readBuffer = ByteBuffer.allocate(4);
            while (true) {
                /**
                 * limit=capacity=4
                 * position=0
                 * 为填充数据做准备
                 */
                readBuffer.clear();
                int read = channel.read(readBuffer);
                //没有可读数据
                if (read == -1) {
                    break;
                }

                /**
                 * position=limit
                 * position=0
                 *为读取数据做准备
                 */
                readBuffer.flip();
                // pw.write(readBuffer.getInt());
                channel1.write(readBuffer);
            }
            inputStream.close();
            channel.close();
            //pw.close();
            outputStream.close();
            channel1.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
