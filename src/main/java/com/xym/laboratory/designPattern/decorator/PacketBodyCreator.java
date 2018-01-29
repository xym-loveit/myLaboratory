package com.xym.laboratory.designPattern.decorator;

/**
 * @author xym
 */
public class PacketBodyCreator implements IPacketCreator {
    public String handlerContent() {
        return "Content of Packet";
    }
}
