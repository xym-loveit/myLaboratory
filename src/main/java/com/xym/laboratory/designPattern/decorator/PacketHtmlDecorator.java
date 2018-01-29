package com.xym.laboratory.designPattern.decorator;

/**
 * 具体装饰者
 *
 * @author xym
 */
public class PacketHtmlDecorator extends PacketDecorator {

    PacketHtmlDecorator(IPacketCreator packetCreator) {
        super(packetCreator);
    }

    /**
     * 将给定数据封装成html
     *
     * @return
     */
    public String handlerContent() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<html>");
        stringBuffer.append("<body>");
        stringBuffer.append(packetCreator.handlerContent());
        stringBuffer.append("</body>");
        stringBuffer.append("</html>");
        return stringBuffer.toString();
    }
}
