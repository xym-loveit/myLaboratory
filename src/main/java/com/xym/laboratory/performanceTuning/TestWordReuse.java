package com.xym.laboratory.performanceTuning;

/**
 * 可以使用jclasslib查看局部变量容量，发现test1方法为   2+1=3
 * test2方法为    2（long类型占用2个字，每个字为32位）+2（long类型占用2个字，每个字为32位）+1（this变量）=5
 *
 * @author xym
 */
public class TestWordReuse {
    public static void main(String[] args) {

    }

    private void test1() {
        {
            long a = 0;
        }
        long b = 0;
    }

    private void test2() {
        long a = 0;
        long b = 0;
    }
}
