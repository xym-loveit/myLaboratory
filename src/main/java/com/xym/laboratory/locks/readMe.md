# 死锁
### 死锁条件：
1. 互斥条件：一个资源只能被一个线程使用
2. 请求与保持条件：一个线程因请求资源而阻塞时，对已获得的资源保持不放
3. 不剥夺条件：线程已获得的资源，在未使用完之前，不能进行剥夺
4. 循环等待条件：若干线程之间形成一种头尾相接的循环等待资源关系

只要破坏死锁四个条件中的任何一个,死锁问题就会得以解决

### 减小锁的持有时间  
减小锁的锁定作用域（使用锁定代码块）

### 减小锁的粒度  
缩小锁定对象的范围，从而减小锁冲突的可能性，进而提高系统的并发能力（典型例子：ConcurrentHashMap）

### 读写分离锁替换独占锁  
在读多写少的场合，使用读写锁可以有效提升系统的并发能力（例：ReadWriteLock）

### 锁分离  
读写锁思想的延伸就是锁分离。读写锁根据读写操作功能上的不同，进行了有效的锁分离。（例子：LinkedBlockingQueue，LinkedBlockingQueue使用了二把(读取锁、写入锁)锁，隔离了读写操作，真正提高了并发性能）

### 重入锁（ReentrantLock）和内部锁（synchronized）  
重入锁比内部锁功能更为强大，内部锁使用简单，易于维护。在JDK1.6中，二者性能相差不大。在可以正常使用系统功能的情况下，推荐优先选择内部锁。

### 锁粗化（Lock Coarsening）  
虚拟机在遇到一连串连续的对同一锁不断进行请求和释放操作时，便会把所有的锁操作整合成对锁的一次请求，从而减少对锁的请求同步次数，这个操作叫锁的粗化。  

### 自旋锁（Spinning Lock）  
线程的状态和上下文切换是要消耗系统资源的，在多线程并发时，频繁的挂起和恢复线程的操作会给系统带来极大的压力。特别是当访问共享资源仅需花费很小一段CPU时间时，锁的等待可能只需要很短的时间，这段时间可能要比将线程挂起并恢复的时间还要短，因此为了这段时间去做重复的线程切换是不值得的。为此JVM引入了自旋锁。  
JVM虚拟机提供-XX:+UseSpinning参数来开启自旋锁，使用-XX:PreBlockSpin参数来设置自旋锁等待的次数。

### 锁消除（Lock Elimination）  
锁消除是JVM在即时编译时，通过对运行上下文的扫描，去除不可能存在共享资源竞争的锁。通过锁清除，可以节省毫无意义的请求锁时间。JVM虚拟机可以在运行时，基于逃逸分析技术，捕获到不可能存在竞争却有申请锁的代码段，并消除这些不必要的锁从而提高系统性能。

```
public class TestLockEliminate {
    public static String getString(String s1, String s2) {
        StringBuffer sb = new StringBuffer();
        sb.append(s1);
        sb.append(s2);
        return sb.toString();
    }

    public static void main(String[] args) {
        long tsStart = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            getString("TestLockEliminate ", "Suffix");
        }
        System.out.println("一共耗费：" + (System.currentTimeMillis() - tsStart) + " ms");
    }
}
```

getString()方法中的StringBuffer数以函数内部的局部变量，进作用于方法内部，不可能逃逸出该方法，因此他就不可能被多个线程同时访问，也就没有资源的竞争，但是StringBuffer的append操作却需要执行同步操作:

```
 @Override
    public synchronized StringBuffer append(String str) {
        toStringCache = null;
        super.append(str);
        return this;
    }
    
```
   
逃逸分析和锁消除分别可以使用参数-XX:+DoEscapeAnalysis和-XX:+EliminateLocks(锁消除必须在-server模式下)开启。使用如下参数运行上面的程序：  
```
-XX:+DoEscapeAnalysis -XX:-EliminateLocks
```

对锁的请求和释放是要消耗系统资源的，使用锁消除技术可以去掉那些不可能存在多线程访问的锁请求，从而提高系统性能。

### 非阻塞的同步/无锁  
基于比较并交换（Compare And Swap）CAS算法的无锁并发控制方法  
与锁的实现相比，无锁算法的设计和实现都要复杂的多，但由于其非阻塞性，它对死锁问题天生免疫，并且线程间的相互影响也远远比基于锁的方式要小。更为重要的是，使用无锁的方式完全没有锁竞争带来的系统开销，也没有线程间频繁调度带来的开销，因此它要比基于锁的方式拥有更优越的性能。  
CAS算法过程是这样的：它包含三个参数CAS（V,E,N）V表示要更新的变量，E表示预期值，N表示新值。仅当V值等于E值时，才将V的值设为N，如果V值和E值不相同，这说明已经有其他线程做了更新，则当前线程什么都不做。最后CAS返回当前V的真实值。CAS操作是抱着乐观态度进行的，他总是认为自己可以成功完成操作，当多个线程同时使用一个CAS操作变量时，只有一个会胜出，并成功更新，其余均会失败，失败的线程不会被挂起，仅是被告知失败，并且允许再次尝试，当然也允许失败的线程放弃操作。基于这样的原理，CAS操作即使没有锁，也可以发现其他线程对当前线程的干扰，并进行适当的处理。  

### 原子操作（java.util.concurrent.atomic包中的原子类是基于无锁方式实现的，他们的性能要远远优于普通的有锁操作，推荐多使用这些工具）

# Amino框架  
### Amino集合
### Amino树
### Amino图
### Amino调度模式（e.g. Master-Worker模式）

# 协程（Kilim框架）

