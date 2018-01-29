package com.xym.laboratory.designPattern.decorator;

/**
 * 装饰器
 *
 * @author xym
 */
public abstract class PacketDecorator implements IPacketCreator {

    IPacketCreator packetCreator;

    public PacketDecorator(IPacketCreator packetCreator) {
        this.packetCreator = packetCreator;
    }


}
