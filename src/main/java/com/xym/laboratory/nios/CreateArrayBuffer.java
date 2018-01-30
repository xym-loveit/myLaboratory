package com.xym.laboratory.nios;// $Id$

import java.io.*;
import java.nio.*;
import java.nio.channels.*;

/**
 * 将字节数组包装为缓冲区
 *
 * @author xym
 */
public class CreateArrayBuffer {
    static public void main(String args[]) throws Exception {
        byte array[] = new byte[1024];

        ByteBuffer buffer = ByteBuffer.wrap(array);

        buffer.put((byte) 'a');
        buffer.put((byte) 'b');
        buffer.put((byte) 'c');

        buffer.flip();

        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());

        for (byte b : array) {
            if (0 != b) {
                System.out.println((char) b);
            }
        }
    }
}
