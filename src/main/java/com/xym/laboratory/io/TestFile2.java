package com.xym.laboratory.io;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * desc
 *
 * @author xym
 */
public class TestFile2 {
    public static void main(String args[]) throws IOException {
        try {
            System.out.println("please Input from      Keyboard");
            int count, n = 512;
            byte buffer[] = new byte[n];
            count = System.in.read(buffer);
            System.out.println("count=" + count);
            FileOutputStream wf = new FileOutputStream("c:/root/fileOne.txt");
            wf.write(buffer, 0, count);
            wf.close(); // 当流写操作结束时，调用close方法关闭流。
            System.out.println("Save to the write.txt");
        } catch (IOException IOe) {
            System.out.println("File Write Error!");
        }
    }
}