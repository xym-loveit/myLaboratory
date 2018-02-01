package com.xym.laboratory.concurrent;

/**
 * 任务相关数据
 *
 * @author xym
 */
public final class PCData {
    /**
     * 数据
     */
    private final int intData;

    public PCData(int intData) {
        this.intData = intData;
    }

    public PCData(String d) {
        intData = Integer.valueOf(d);
    }

    public int getIntData() {
        return intData;
    }

    @Override
    public String toString() {
        return "PCData{" +
                "intData=" + intData +
                '}';
    }
}
