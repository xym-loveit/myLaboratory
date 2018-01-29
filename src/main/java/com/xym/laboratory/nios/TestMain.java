package com.xym.laboratory.nios;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


/**
 * @author xym
 */
public class TestMain {
    public static void main(String[] args) {
        try {
            FileOutputStream outputStream = new FileOutputStream("outData.txt");
            FileChannel channel = outputStream.getChannel();
            //缓冲区最小为4个字\节，否则会抛出BufferOverflowException
            ByteBuffer buffer = ByteBuffer.wrap(new byte[4]);
            System.out.printf("第1次,position=%s,limit=%s,capacity=%s\n", buffer.position(), buffer.limit(), buffer.capacity());
            for (int i = 0; i < 100000; i++) {
                /**
                 *1、它将 limit 设置为与 capacity 相同。
                 *2、它设置 position 为 0
                 */
                buffer.clear();
                //System.out.printf("第2次，position=%s,limit=%s,capacity=%s\n", buffer.position(), buffer.limit(), buffer.capacity());
                /**
                 * 一次放入一个字节即为4个byte
                 */
                buffer.putInt(i);
                buffer.mark();//标记position
                buffer.reset();//将当前mark值赋值给position，即position=mark
                //System.out.printf("第3次，position=%s,limit=%s,capacity=%s\n", buffer.position(), buffer.limit(), buffer.capacity());
                buffer.flip();//position=0,limit=position;
                //System.out.printf("第4次，position=%s,limit=%s,capacity=%s\n", buffer.position(), buffer.limit(), buffer.capacity());
                channel.write(buffer);
            }

            System.out.printf("第5次，position=%s,limit=%s,capacity=%s\n", buffer.position(), buffer.limit(), buffer.capacity());
            outputStream.close();
            channel.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
