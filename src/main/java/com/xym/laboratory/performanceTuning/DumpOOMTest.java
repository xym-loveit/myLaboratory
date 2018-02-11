package com.xym.laboratory.performanceTuning;

import java.util.HashMap;

/**
 * 通过在jvm发生OOM时，导出堆快照进行分析查看问题
 * <p>
 * 实用参数：
 * -Xmx10M -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=c:\m.hprof
 * -Xmx10M：指定最大堆大小
 * -XX:+HeapDumpOnOutOfMemoryError：指定JVM在发生OOM时将当前的堆信息保存到文件中
 * -XX:HeapDumpPath：指定堆快照的保存位置
 *
 * @author xym
 */
public class DumpOOMTest {
    public static void main(String[] args) {

        HashMap<Object, Object> objectObjectHashMap = new HashMap<Object, Object>();

        for (int i = 0; i < 2; i++) {
            byte[] bytes = new byte[1024 * 1024];
            objectObjectHashMap.put(System.nanoTime(), bytes);
            if (objectObjectHashMap.size() > 2) {
                objectObjectHashMap.clear();
            }
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
