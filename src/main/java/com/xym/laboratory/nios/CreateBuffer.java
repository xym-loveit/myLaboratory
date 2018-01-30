package com.xym.laboratory.nios;// $Id$

import java.io.*;
import java.nio.*;
import java.nio.channels.*;

/**
 * 创建缓冲区
 *
 * @author xym
 */
public class CreateBuffer {
    static public void main(String args[]) throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        buffer.put((byte) 'a');
        buffer.put((byte) 'b');
        buffer.put((byte) 'c');

        buffer.flip();

        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());
    }
}
