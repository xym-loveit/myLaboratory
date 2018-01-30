package com.xym.laboratory.nios;// $Id$

import java.io.*;
import java.nio.*;
import java.nio.channels.*;

/**
 * 采用直接缓冲区操作数据，直接操作屋里内存，比较快
 * 一般缓冲区使用jvm堆内存操纵数据，比较慢
 *
 * @author xym
 */
public class FastCopyFile {
    static public void main(String args[]) throws Exception {
        if (args.length < 2) {
            System.err.println("Usage: java FastCopyFile infile outfile");
            System.exit(1);
        }

        String infile = args[0];
        String outfile = args[1];

        FileInputStream fin = new FileInputStream(infile);
        FileOutputStream fout = new FileOutputStream(outfile);

        FileChannel fcin = fin.getChannel();
        FileChannel fcout = fout.getChannel();

        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

        while (true) {
            buffer.clear();
            int r = fcin.read(buffer);
            if (r == -1) {
                break;
            }
            buffer.flip();
            fcout.write(buffer);
        }
    }
}
