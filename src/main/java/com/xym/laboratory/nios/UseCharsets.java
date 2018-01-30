package com.xym.laboratory.nios;// $Id$

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;

/**
 * 编码器和解码器处理文本数据
 *
 * @author xym
 */
public class UseCharsets {
    static public void main(String args[]) throws Exception {
        String inputFile = "samplein.txt";
        String outputFile = "sampleout.txt";

        RandomAccessFile inf = new RandomAccessFile(inputFile, "r");
        RandomAccessFile outf = new RandomAccessFile(outputFile, "rw");
        long inputLength = new File(inputFile).length();

        FileChannel inc = inf.getChannel();
        FileChannel outc = outf.getChannel();

        MappedByteBuffer inputData =
                inc.map(FileChannel.MapMode.READ_ONLY, 0, inputLength);
        Charset latin1 = Charset.forName("ISO-8859-1");
        CharsetDecoder decoder = latin1.newDecoder();
        CharsetEncoder encoder = latin1.newEncoder();
        /**
         * 直接转为字符
         */
        CharBuffer cb = decoder.decode(inputData);
        // Process char data here
        /**
         * 字符转为字节
         */
        ByteBuffer outputData = encoder.encode(cb);
        outc.write(outputData);

        inf.close();
        outf.close();
    }
}
