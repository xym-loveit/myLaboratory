# 并行程序设计模式
1、Future模式  
future模式的核心在于去除了主函数的等待时间，并使得原本需要等待的时间段用来处理其他业务逻辑，从而充分利用计算机资源

2、Master-Worker模式  
Master-Worker模式是常用的并行模式之一，它的核心思想是：系统有两类进程协作工作，Master进程和Worker进程。Master进程负责接收和分配任务，Worker进程负责处理子任务。当各个Worker进程处理完子任务后，将结果返回至Master进程，由Master进程做归纳和汇总，从而得到系统的最终结果。

3、Guarded Suspension意为保护暂停，其核心思想是仅当服务进程准备好时，才提供服务。设想一种场景，服务器可能会在很短时间内承受大量的客户端请求，客户端请求的数量可能超过服务器本身的即时处理能力，而服务端程序又不能丢弃任何一个客户请求。此时，最佳的处理方案莫过于让客户端请求进行排队，由服务端程序一个接一个处理。这样，既保证了所有客户端请求均不会丢失，同时也避免了服务器由于同时处理过多的请求而奔溃。  

Guarded Suspension模式可以在一定程度上缓解系统压力，它可以将系统的负载在时间轴上均匀的分配。使用该模式后，可以有效降低系统的瞬时负载，对提高系统的抗压力和稳定性有一定的帮助。

4、不变模式  
在并行软件开发过程中，同步操作似乎是必不可少的。当多个线程对同一个对象进行读写操作时，为了保证对象数据的一致性和正确性，有必要对对象进行同步。而同步操作对系统的性能有相当的损耗，为了尽可能去除这些同步操作，提高并行程序性能，可以使用一种不可变的对象，依靠对象的不变性，可以确保在没有进行同步操作的多线程环节中，依然始终保持内部状态的一致性和正确性。 
例如：java的基本类型的包装类：String/Character/Short/Long/Integer/Double/Byte/Boolean/Float

5、生产者、消费者模式  
生产者和消费者模式能够很好地对生产者线程和消费者线程进行解耦，优化了系统整体结构。同时，由于缓冲区的作用，允许生产者线程和消费者线程存在执行上的性能差异，从一定程度上缓解了性能瓶颈对系统性能的影响。

# 并发数据结构
1、在读多写少的高并发环境中，使用 ```CopyOnWriteArrayList```可以提高 系统的性能，但是在写多读少的场合，```CopyOnWriteArrayList```可能不如```Vector```。

2、和List相似，并发```Set```也有一个```CopyOnWriteArraySet```，它实现了Set接口，并且是线程安全的，它的内部实现完全依赖于```CopyOnWriteArrayList```。因此，他的特征与```CopyOnWriteArrayList```完全一致，适用于读多写少的高并发场合，在需要并发写的场合，则可以使用```Collections```的方法：  
```public static <T> Set<T> synchronizedSet(Set<T> s)```
得到一个线程安全的Set。  

3、ConcurrentHashMap是专门为线程并发而设计的HashMap,他的get操作是无锁的,它的put操作的锁粒度又小于同步的HashMap，因此它的性能优于同步的HashMap

4、并发Queue  
如果需要在高并发时仍能保持良好性能的队列，可以使用```ConcurrentLinkedQueue```对象。```LinkedBlockingQueue```为代表的BlockingQueue。与ConcurrentLinkedQueue使用场景不同，BlockingQueue的主要功能并不是在于提升高并发时的队列性能，而在于简化多线程间的数据共享。BlockingQueue的典型使用场景是在生产者-消费者模式中，生产者总是将产品放入BlockingQueue队列，而消费者从队列中取出产品消费，从而实现数据共享。BlockingQueue提供一种读写阻塞等待的机制，即如果消费者速度过快，则BlockingQueue队列可能被清空。此时消费线程再试图从BlockingQueue读取数据时就会被阻塞。反之，如果生产者较快，则BlockingQueue可能会被填满，此时生产者线程再试图向BlockingQueue队列中装入数据时，便会被阻塞等待。
BlockingQueue的核心方法如下： 

offer（object）：将object加入到BlockingQueue里，如果BlockingQueue有足够的空间则返回true，否则返回false（该方法不会阻塞当前执行方法的线程）  

offer（E o,long timeout,TimeUnit unit）:可以设定等待的时间，如果在指定的时间内，还不能往队列BlockingQueue中加入，则返回失败  

put（object）：将object加入到BlockingQueue里，如果BlockingQueue没有足够的空间，则调用此方法的线程被阻塞直到BlockingQueue里面有空闲空间时再继续。 

poll（time）：取走BlockingQueue里面排在首位的对象，若不能立即取出，则可以等time参数规定的时间，取不到时返回null

poll（long timeout,TimeUnit unit）：从BlockingQueue里面取排在首位的对象，如果在指定时间内，队列一旦有数据可取，则立即返回队列中的数据。超时后仍然没有取得数据则返回失败。

take（）：取走BlockingQueue里面排在首位的对象，若BlockingQueue为空则阻塞等待直到BlockingQueue有新的数据被加入

drainTo（）：一次性从BlockingQueue获取所有可用的数据对象（还可以指定获取数据的个数），通过该方法可以提升获取数据的效率，不需要分批多次加锁或释放锁。

BlockingQueue提供了二种主要实现：  
```LinkedBlockingQueue```和```ArrayBlockingQueue```

5、并发双端队列（Double-Ended-Queue 简称Deque）  
Deque允许在队列的头部和尾部进行 出队/入队 操作，双端队列在队列的基础上多了些操作：
参见 java程序性能优化-葛一鸣（page 199）
主要实现：  
ArrayDeque、LinkedList、LinkedBlockingDeque  
LinkedBlockingDeque是一个线程安全的双端队列实现，LinkedBlockingDeque使用链表结构，每一个队列节点都维护一个前驱节点和后驱节点，LinkedBlockingDeque没有进行读写锁的分离，因此同一时间只能有一个线程对其进行操作，在高并发应用中，它的性能表现要远远低于LinkedBlockingQueue，更要低于ConcurrentLinkedQueue。

# 并发控制方法

1、volatile关键字

2、synchronized关键字

3、ReentrantLock(可重入锁)

4、ReadWriteLock（读写分离锁）

5、Condition多线程协调对象（作为Object的wait和notify、notifyAll替代方式）

6、Semaphore（信号量对锁的概念进行了扩展，它可以限定对某一具体资源的最大可访问线程数）

7、ThreadLocal线程局部变量  
ThreadLocal是一种多线程间并发访问变量的解决方案。与synchronized加锁方式不同，ThreadLocal完全不提供锁，而使用以空间换时间的手段，为每个线程提供变量的独立副本，以保证线程安全，因此它不是一种数据共享的解决方案。  


