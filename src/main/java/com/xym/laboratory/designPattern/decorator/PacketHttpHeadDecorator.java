package com.xym.laboratory.designPattern.decorator;

import com.sun.org.apache.bcel.internal.generic.NEW;

import java.util.Date;

/**
 * @author xym
 */
public class PacketHttpHeadDecorator extends PacketDecorator {

    public PacketHttpHeadDecorator(IPacketCreator packetCreator) {
        super(packetCreator);
    }

    /**
     * 给给定数据加上Http头
     *
     * @return
     */
    public String handlerContent() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Cache-Control:no-cache\n");
        stringBuffer.append("Date:" + new Date() + "\n");
        stringBuffer.append(packetCreator.handlerContent());
        return stringBuffer.toString();
    }
}
