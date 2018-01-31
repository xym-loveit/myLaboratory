package com.xym.laboratory.skills;

/**
 * 采用其他方式替换switch,可能提升性能
 *
 * @author xym
 */
public class ChangeSwitch {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        int re = 0;
        for (int i = 0; i < 10000000; i++) {
            /**
             * 调用包含switch语句的函数
             */
            re = switchInt(i);
        }
        long endTime = System.currentTimeMillis();

        System.out.println((endTime - startTime) + " ms");
        System.out.println("-------------------------------");
        startTime = System.currentTimeMillis();
        int re2 = 0;
        for (int i = 0; i < 10000000; i++) {
            /**
             * 调用包含switch语句的函数
             */
            re2 = switchInt2(i);
        }
        endTime = System.currentTimeMillis();
        System.out.println((endTime - startTime) + " ms");
    }


    private static int switchInt2(int z) {
        int[] ay = {0, 3, 6, 7, 8, 10, 16, 18, 44};
        int i = z % 10 + 1;
        /**
         * 模拟switch的default
         */
        if (i > 8 || i < 1) {
            return -1;
        } else {
            return ay[i];
        }
    }

    /**
     * 根据操作数不同返回不同的值
     *
     * @param z
     * @return
     */
    private static int switchInt(int z) {
        int i = z % 10 + 1;
        switch (i) {
            case 1:
                return 3;
            case 2:
                return 6;
            case 3:
                return 7;
            case 4:
                return 8;
            case 5:
                return 10;
            case 6:
                return 16;
            case 7:
                return 18;
            case 8:
                return 44;
            default:
                return -1;
        }
    }
}
