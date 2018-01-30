package com.xym.laboratory.nios;// $Id$

import java.io.*;
import java.nio.*;
import java.nio.channels.*;

/**
 * 文件锁
 *
 * @author xym
 */
public class UseFileLocks {
    static private final int start = 10;
    static private final int end = 20;

    static public void main(String args[]) throws Exception {
        // Get file channel
        RandomAccessFile raf = new RandomAccessFile("usefilelocks.txt", "rw");
        FileChannel fc = raf.getChannel();

        // Get lock
        System.out.println("trying to get lock");
        /**
         * 获取排它锁
         */
        FileLock lock = fc.lock(start, end, false);
        System.out.println("got lock!");

        // Pause
        System.out.println("pausing");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ie) {
        }

        // Release lock
        System.out.println("going to release lock");
        /**
         * 释放锁
         */
        lock.release();
        System.out.println("released lock");

        raf.close();
    }
}
