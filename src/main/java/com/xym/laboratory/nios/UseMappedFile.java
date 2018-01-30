package com.xym.laboratory.nios;// $Id$

import java.io.*;
import java.nio.*;
import java.nio.channels.*;

/**
 * 内存映射缓冲区
 *
 * @author xym
 */
public class UseMappedFile {
    static private final int start = 0;
    static private final int size = 1024;

    static public void main(String args[]) throws Exception {
        RandomAccessFile raf = new RandomAccessFile("usemappedfile.txt", "rw");
        FileChannel fc = raf.getChannel();

        MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE,
                start, size);

        mbb.put(0, (byte) 97);
        mbb.put(1023, (byte) 122);

        raf.close();
    }
}
