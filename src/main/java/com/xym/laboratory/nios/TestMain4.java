package com.xym.laboratory.nios;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;

/**
 * 分散通道ScatteringByteChannel
 * 聚集通道GatheringByteChannel
 *
 * @author xym
 */
public class TestMain4 {

    public static void main(String[] args) {
        byte[] msg = "123456789".getBytes();
        ByteBuffer buffer1 = ByteBuffer.allocate(16);
        ByteBuffer buffer2 = ByteBuffer.allocate(20);

        ByteInputStream inputStream = new ByteInputStream(msg, msg.length);
        ScatteringByteChannel scatteringByteChannel = null;
        GatheringByteChannel gatheringByteChannel = null;
        try {
            /**
             * 将数据分散读取至2个缓冲区中
             * 分散读取会自动找到有空间接受数据的第一个缓冲区。在这个缓冲区填满后，它就会移动到下一个缓冲区
             */
            scatteringByteChannel.read(new ByteBuffer[]{buffer1, buffer2});
            /**
             * 将分散的2个缓冲区中的数据聚集起来组装为单个数据流
             *
             * 聚集写入来自动将网络消息的各个部分组装为单个数据流，以便跨越网络传输消息
             */
            gatheringByteChannel.write(new ByteBuffer[]{buffer1, buffer2});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
