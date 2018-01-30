package com.xym.laboratory.nios;// $Id$

import java.io.*;
import java.nio.*;
import java.nio.channels.*;

/**
 * 缓冲区分片和源缓冲区共享数据，修改分片数据后对缓冲区中共享数据也将产生影响
 *
 * @author xym
 */
public class SliceBuffer {
    static public void main(String args[]) throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(10);

        for (int i = 0; i < buffer.capacity(); ++i) {
            buffer.put((byte) i);
        }

        buffer.position(3);
        buffer.limit(7);

        ByteBuffer slice = buffer.slice();

        for (int i = 0; i < slice.capacity(); ++i) {
            byte b = slice.get(i);
            b *= 11;
            slice.put(i, b);
        }

        buffer.position(0);
        buffer.limit(buffer.capacity());

        while (buffer.remaining() > 0) {
            System.out.println(buffer.get());
        }
    }
}
