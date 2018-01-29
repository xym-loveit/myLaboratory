package com.xym.laboratory.designPattern.decorator;

import java.io.*;

/**
 * jdk中流使用了经典的装饰者模式
 *
 * @author xym
 */
public class TestMain {

    public static void main(String[] args) {
        PacketHttpHeadDecorator phd = new PacketHttpHeadDecorator(new PacketHtmlDecorator(new PacketBodyCreator()));
        System.out.println(phd.handlerContent());
    }



   /* public static void main(String[] args) {
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("testStream.txt")));
//            DataOutputStream dataOutputStream = new DataOutputStream((new FileOutputStream("testStream.txt")));
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < 1000000; i++) {
                dataOutputStream.writeLong(i);
            }
            System.out.println("spend=" + (System.currentTimeMillis() - startTime));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
