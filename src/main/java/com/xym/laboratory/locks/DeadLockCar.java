package com.xym.laboratory.locks;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 4个方向的行车，互不相让形成的经典死锁问题
 *
 * @author xym
 */
public class DeadLockCar extends Thread {
    protected Object myDirect;

    /**
     * 代表东南西北四个方向
     */

    static ReentrantLock south = new ReentrantLock();
    static ReentrantLock north = new ReentrantLock();
    static ReentrantLock west = new ReentrantLock();
    static ReentrantLock east = new ReentrantLock();

    public static void main(String[] args) {
        DeadLockCar southCar = new DeadLockCar(south);
        DeadLockCar northCar = new DeadLockCar(north);
        DeadLockCar westCar = new DeadLockCar(west);
        DeadLockCar eastCar = new DeadLockCar(east);

        southCar.start();
        northCar.start();
        westCar.start();
        eastCar.start();

        /**
         * 此时四车死锁
         */
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /**
         * 强行剥夺任意小车资源，解除死锁
         */
        westCar.interrupt();

    }

    public DeadLockCar(Object obj) {
        this.myDirect = obj;
        if (myDirect == south) {
            this.setName("south");
        } else if (myDirect == north) {
            this.setName("north");
        } else if (myDirect == west) {
            this.setName("west");
        } else if (myDirect == east) {
            this.setName("east");
        }
    }

    @Override
    public void run() {
        //向南走的小车
        if (myDirect == south) {
            try {
                /**
                 * 占据了向西的路
                 */
                west.lockInterruptibly();
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                /**
                 * 等待向南的路
                 */
                south.lockInterruptibly();
                System.out.println("car to south has passed");
            } catch (InterruptedException e) {
                System.out.println("car to south is killed");
            } finally {
                /**
                 * 如果当前线程持有锁
                 */
                if (west.isHeldByCurrentThread()) {
                    west.unlock();
                }
                /**
                 * 如果当前线程持有锁
                 */
                if (south.isHeldByCurrentThread()) {
                    south.unlock();
                }
            }
        }

        //向北走的小车
        if (myDirect == north) {
            try {
                /**
                 * 占据了向东的路
                 */
                east.lockInterruptibly();
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                /**
                 * 等待向北的路
                 */
                north.lockInterruptibly();
                System.out.println("car to north has passed");
            } catch (InterruptedException e) {
                System.out.println("car to north is killed");
            } finally {
                /**
                 * 如果当前线程持有锁
                 */
                if (east.isHeldByCurrentThread()) {
                    east.unlock();
                }
                /**
                 * 如果当前线程持有锁
                 */
                if (north.isHeldByCurrentThread()) {
                    north.unlock();
                }
            }
        }

        //向西走的小车
        if (myDirect == west) {
            try {
                /**
                 * 占据了向北的路
                 */
                north.lockInterruptibly();
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                /**
                 * 等待向西的路
                 */
                west.lockInterruptibly();
                System.out.println("car to west has passed");
            } catch (InterruptedException e) {
                System.out.println("car to west is killed");
            } finally {
                /**
                 * 如果当前线程持有锁
                 */
                if (north.isHeldByCurrentThread()) {
                    north.unlock();
                }
                /**
                 * 如果当前线程持有锁
                 */
                if (west.isHeldByCurrentThread()) {
                    west.unlock();
                }
            }
        }
        //向东走的小车
        if (myDirect == east) {
            try {
                /**
                 * 占据了向南的路
                 */
                south.lockInterruptibly();
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                /**
                 * 等待向西的路
                 */
                east.lockInterruptibly();
                System.out.println("car to east has passed");
            } catch (InterruptedException e) {
                System.out.println("car to east is killed");
            } finally {
                /**
                 * 如果当前线程持有锁
                 */
                if (south.isHeldByCurrentThread()) {
                    south.unlock();
                }
                /**
                 * 如果当前线程持有锁
                 */
                if (east.isHeldByCurrentThread()) {
                    east.unlock();
                }
            }
        }
    }
}
