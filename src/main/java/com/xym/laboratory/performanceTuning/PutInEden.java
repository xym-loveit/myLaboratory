package com.xym.laboratory.performanceTuning;

/**
 * 通过实例观察堆空间使用情况
 * -XX:+PrintGCDetails -Xms15M -Xmx15M
 * 手动分配足够大的新生代空间，防止FullGC
 * -XX:+PrintGCDetails -Xms15M -Xmx15M -Xmn6M
 *
 * @author xym
 */
public class PutInEden {
    public static void main(String[] args) {
        byte[] b1, b2, b3, b4;
        /**
         * 每个分配1M堆空间，观察堆空间使用情况
         */
        b1 = new byte[1024 * 1024];
        b2 = new byte[1024 * 1024];
        b3 = new byte[1024 * 1024];
        b4 = new byte[1024 * 1024];
    }
}
