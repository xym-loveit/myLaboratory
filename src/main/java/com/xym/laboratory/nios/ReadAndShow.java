package com.xym.laboratory.nios;// $Id$

import java.io.*;
import java.nio.*;
import java.nio.channels.*;

/**
 * 通过缓冲区读取文件数据并显示
 *
 * @author xym
 */
public class ReadAndShow {
    static public void main(String args[]) throws Exception {
        FileInputStream fin = new FileInputStream("readandshow.txt");
        FileChannel fc = fin.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        fc.read(buffer);

        buffer.flip();

        int i = 0;
        while (buffer.remaining() > 0) {
            byte b = buffer.get();
            System.out.println("Character " + i + ": " + ((char) b));
            i++;
        }

        fin.close();
    }
}
