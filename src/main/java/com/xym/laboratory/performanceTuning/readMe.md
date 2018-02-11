# JVM调优
### Java虚拟机内存模型  
JVM虚拟机将其内存分为：程序计数器、虚拟机栈、本地方法栈、Java堆、方法区  
* 程序计数器：用于存放下一条运行的指令
* 虚拟机栈和本地方法栈：用于存放函数调用堆栈信息
* Java堆：用于存放Java程序运行时所需的对象数据
* 方法区：用于存放程序的类元数据信息


##### 虚拟机栈中的局部变量表中的字（局部变量表以字为单位进行内存的划分，一个字为32位长度）可能会影响GC回收，如果这个字没有被后续代码复用，那么他所引用的对象不会被GC释放。  

##### 本地方法栈：本地方法栈与Java虚拟机的功能很相似，Java虚拟机栈用于管理Java函数的调用，而本地方法栈用于管理本地方法的调用。本地方法并不是用Java实现，而是使用C语言实现的。在SUN的Hot Spot虚拟机中，不区分本地方法栈和虚拟机栈。因此，和虚拟机栈一样，它也会抛出StackOverflowError和OutOfMemoryError。

# JVM内存分配参数  
* -Xmx（最大堆内存），新生代和老年代大小之和的最大值，它是java应用程序的堆上限。

* -Xms（最小堆内存），设置系统最小堆内存，JVM会试图将系统内存尽可能限制在-Xms中，因此，当内存实际使用量触及-Xms指定的大小时，会触发Full GC,因此把-Xms值设置为-Xmx时，可以在系统运行初期减少GC的次数和耗时。

* -Xmn（新生代大小），新生代大小一般为整个堆空间的1/4到1/3左右。（e.g. -Xmx11M -Xms11M -Xmn2M -XX:+PrintGCDetails的最佳Xmn为3.5M）
-Xmx11M -Xms11M -Xmn2M -XX:+PrintGCDetails 偏小  Minor GC次数将增加  （不要这样做）
-Xmx11M -Xms11M -Xmn3.5M -XX:+PrintGCDetails 最佳  Minor GC次数将最少 （按正常比例配置最佳参数，推荐）

* -XX:MaxPermSize（设置持久代最大值）、-XX:PermSize（设置持久代初始大小），持久代的大小直接决定了系统可以支持多少个类定义和多少常量。对于使用CGLIB或Javassist等动态字节码生成工具的应用程序而言，设置合理的持久代大小有助于维持系统稳定。一般推荐最佳参数为64M/128M

* -Xss（设置线程栈大小），如果系统确实需要大量线程并发执行，那么设置一个较小的堆和较小的栈，有助于提高系统所能承受的最大线程数。

#### 堆的比例分配  
参数-XX:SurvivorRatio是用来设置新生代中，eden空间和s0空间的比例关系。s0和s1空间又分别称为form空间和to空间。它们的大小是相同的，职能也是一样的，并在Minor GC后，会互换角色。  
```
-XX:SurvivorRatio=eden/s0=eden/s1
```
-XX:NewRatio可以设置老年代与新生代的比例,可以使用-XX:+PrintGCDetails参数打印出堆的实际大小。


# 对分配参数总结  
与Java应用程序堆内存相关的JVM参数有：  
-Xms：设置Java应用程序启动时的初始堆大小  
-Xmx：设置Java应用程序能获得的最大堆大小  
-Xss：设置线程栈大小  
-XX:MinHeapFreeRatio：设置堆空间最小空闲比例。当堆空间的空闲内存小于这个数值时，JVM便会扩展堆空间。  
-XX:MaxHeapFreeRatio：设置堆空间最大空闲比例。当堆空间的空闲内存大于这个数值时，便会压缩堆空间，得到一个较小的堆。  
-XX:NewSize：设置新生代的大小。  
-XX:NewRatio：设置老年代与新生代的比例，它等于老年代大小除以新生代大小。  
-XX:SurvivorRatio：新生代中eden区与survivor区的比例。
-XX:MaxPermSize：设置最大的持久区大小  
-XX:PermSize：设置持久区的初始值。  
-XX:TargetSurvivorRatio：设置survivor区的可使用率。当survivor区的空间使用率达到这个数值时，会将对象送入老年代。 

直观参见  ![堆分配参数一览][001]

# 垃圾收集基础  

垃圾收集器要处理的基本问题：  
1. 哪些对象需要回收？
2. 何时回收这些对象？
3. 如何回收这些对象？

### 垃圾回收算法与思想  
#### 引用计数法（Reference Counting）  
由于无法处理循环引用的问题，引用计数法不适合用于JVM的垃圾回收。  

#### 标记清除法（Mark-Sweep）  
标记-清除算法（先标记在清除分为二阶段）先通过根节点标记所有可达对象，然后清除所有不可达对象，完成垃圾回收。标记清除算法的最大问题就是空间碎片。  

#### 复制算法（Copying）  
将原有的内存空间分为二块，每次只使用其中的一块，在垃圾回收时，将正在使用的内存中的存活对象复制到未使用的内存块中，之后，清除正在使用的内存块中的所有对象，交换两个内存的角色，完成垃圾回收。复制算法比较适用于新生代。因为在新生代，垃圾对象通常会多于存活对象，复制算法的效果会比较好。

#### 标记压缩算法（Mark-Compact）  
标记压缩算法是一种老年代的回收算法，它在标记-清除算法的基础上做了一些优化。和标记-清除算法一样，标记-压缩算法也首先需要从根节点开始，对所有可达对象做一次标记，但之后，它并不简单的清理未标记的对象，而是将所有的存活对象压缩到内存的一端，之后清理边界外所有的空间。这种方法避免了碎片的产生，又不需要两块相同的内存空间，因此性价比较高。  

#### 增量算法（Incremental Collecting）
  
#### 分代（Generational Collecting）  
根据每块内存区间的特点，使用不同的回收算法。如：新生代采用复制算法（幸存对象较少）、老年代使用标记-压缩算法（幸存对象占大部分）

### 垃圾收集器类型  
![垃圾收集器类型][002]

#### 新生代串行收集器  
串行收集器是所有垃圾回收器中最古老的的一种，也是JDK种最基本的垃圾收集器之一。串行收集器主要有两个特点：第一，它仅仅使用单线程进行垃圾回收；第二，它是独占式的垃圾回收。
![串行垃圾回收][003]  
在Hot Spot虚拟机中，使用-XX:+UseSerialGC参数可以指定使用新生代串行收集器和老年代串行收集器。当JVM在client模式下运行时，它是默认的垃圾收集器。

#### 老年代串行收集器  
若要启用老年代串行回收器，可以使用以下参数：  
* -XX:+UseSerialGC：新生代、老年代都使用串行回收器
* -XX:+UseParNewGC：新生代使用并行收集器，老年代使用串行收集器
* -XX:+UseParallelGC：策略为新生代使用并行清除，年老代使用单线程Mark-Sweep-Compact的垃圾收集器

#### 并行收集器  
* -XX:+UseParNewGC：新生代使用并行收集器，老年代使用串行收集器
    * -XX:+UseConcMarkSweepGC：新生代使用并行收集器，老年代使用CMS  

并行收集器工作时的线程数量可以使用-XX:ParallelGCThreads参数指定。一般，最好与CPU数量相当，避免过多的线程数，影响垃圾收集性能。在默认情况下，当CPU数量小于8个时，ParallelGCThreads的值等于CPU数量；当CPU数量大于8个时，ParallelGCThreads的值等于```3+[(5*CPU_Count)/8]```。
![并行垃圾回收][004] 


#### 新生代并行回收（Parallel Scavenge）收集器（关注于系统的吞吐量）   
新生代并行回收收集器可以使用如下参数启用：  
* -XX:+UseParallelGC：策略为新生代使用并行清除，年老代使用单线程Mark-Sweep-Compact的垃圾收集器  
* -XX:+UseParallelOldGC：新生代和老年代都使用并行回收收集器  
并行回收处理器提供了2个重要的参数用于控制系统的吞吐量：  
1. -XX:MaxGCPauseMillis：设置最大垃圾收集停顿时间，它的值是一个大于0的整数。收集器在工作时，会调整Java堆大小或其他一些参数，尽可能把停顿时间控制在MaxGCPauseMillis以内。如果希望减小停顿时间，而把这个值设置的很小，为了达到预期的停顿时间，JVM可能会使用一个较小的堆（一个小堆比一个大堆回收快），而这将导致垃圾回收得很频繁，从而增加了垃圾回收总时间，降低了吞吐量。

2. -XX:GCTimeRadio：设置吞吐量大小，它的值时一个0~100之间的整数。假设GCTimeRadio的值为n，那么系统将花费不超过1/(1+n)的时间用于垃圾收集。比如GCTimeRadio等于19（默认值），则系统用于垃圾收集的时间不超过1/(1+19)=5%。默认情况下，它的取值是99，既不超过1/(1+99)=1%的时间用于垃圾收集。  
除此之外，并行回收收集器与并行收集器另一个不同之处在于，它还支持一种自适应的GC调节策略。使用-XX:+UseAdaptiveSizePolicy可以打开自适应GC策略。在这种模式下，新生代大小、eden和survivor的比例、晋升老年代的对象年龄等参数会被自动调整，以达到在堆大小、吞吐量和停顿时间之间的平衡点。在手动调优比较困难的场合，可以直接使用这种自适应的方式，仅指定虚拟机的最大堆、目标吞吐量和停顿时间，让虚拟机自己完成调优工作。

#### 老年代并行回收收集器（关注系统的吞吐量）  
老年代并行回收收集器采用标记压缩算法  
![老年代并行回收收集器示意图][005]  

使用-XX:+UseParallelOldGC可以在新生代和老年代都使用并行回收收集器，这是一对非常关注吞吐量的垃圾收集器组合，在对吞吐量敏感的系统中，可以考虑使用。参数-XX:ParallelGCThreads也可以用于设置垃圾回收时的线程数量。

#### CMS收集器  
与并行收集器不同，CMS收集器主要关注系统停顿时间。CMS是Concurrent Mark Sweep的缩写，意为并发标记清除。
从名称上看可以得知，它使用的是标记-清除算法，同时它又是一个使用多线程并行回收的垃圾收集器。CMS工作时主要步骤：初始标记、并发标记、重新标记、并发清除和并发重置。其中初始标记和重新标记是独占资源的，而并发标记、并发清除和并发重置是可以和用户线程一起执行的。因此，从整体上说，CMS收集不是独占式的，它可以在应用程序运行过程中进行垃圾回收。  
![CMS工作示意图][006]
根据标记-清除算法，初始标记、并发标记和重新标记都是为了标记出需要回收的对象。并发清理，则是在标记完成后，正式回收垃圾对象；并发重置是指在垃圾回收完成后，重新初始化CMS数据结构和数据，为一下次垃圾回收做好准备。并发标记、并发清理和并发重置都是可以和应用程序线程一起执行的。  
CMS收集器在其主要的工作阶段虽然没有暴力地彻底暂停应用程序线程，但是由于它和应用程序线程并发执行，相互抢占CPU，故在CMS执行期间对应用程序吞吐量将造成一定影响。CMS默认启动线程数是```（ParallelGCThreads+3）/4```，ParallelGCThreads是新生代并行收集器的线程数，也可以通过-XX:ParallelCMSThreads参数手工设定CMS的线程数量。当CPU资源比较紧张时，受到CMS收集器线程的影响，应用系统的性能在垃圾回收阶段可能会非常糟糕。  
由于CMS收集器不是独占式的回收器，在CMS回收过程中，应用程序仍然在不停的工作。在应用程序工作过程中，又会不断的产生垃圾。这些新生成的垃圾在当前CMS回收过程中无法清除的。同时，因为应用程序没有中断，故在CMS回收过程中，还应该确保应用程序有足够的内存可用。因此，CMS收集器不会等待堆内存饱和时才进行垃圾回收，而是当堆内存使用率达到某一阈值时，便开始进行回收，以确保应用程序在CMS工作过程中，依然有足够的空间支持 应用程序运行。  
这个回收阈值可以使用-XX:CMSInitiatingOccupancyFraction来指定，默认是68。即，当老年代的空间使用率达到68%时，会执行一次CMS回收。如果应用程序的内存使用率增长很快，在CMS的执行执行过程中，已经出现了内存不足的情况，此时CMS回收就会失败，JVM将启动老年代串行收集器进行垃圾回收。如果这样，应用程序将完全中断，直到垃圾收集完成，这时，应用程序的停顿时间可能很长。  
因此，根据应用程序的特点，可以对-XX:CMSInitiatingOccupancyFraction进行调优。如果内存增长缓慢可以设置一个较大的值，大的阈值可以有效降低CMS的触发频率，减少老年代回收的次数可以较为明显的改善应用程序性能。反之，如果应用程序内存使用率增长很快，则应该降低这个阈值，以避免频繁触发老年代串行收集器。  
CMS是一个基于标记-清除算法的回收器。标记-清除算法将会造成大量内存碎片，离散的可用空间无法分配较大的对象。在这种情况下，即使堆内存仍然有较大的剩余空间，也可能会被迫进行一次垃圾回收，以换取一块可用的连续内存，这种现象对系统性能是相当不利的。为了解决这个问题，CMS收集器还提供了几个用于内存压缩整理的参数。  
```-XX:+UseCMSCompactAtFullCollection```开关可以使CMS在垃圾收集完成后，进行一次碎片整理。内存碎片的整理不是并发进行的。```-XX:CMSFullGCsBeforeCompaction```参数可以用于设定进行多少次CMS回收后，进行一次内存压缩。

#### G1收集器（Garbage First）  
G1收集器采用标记-压缩算法。因此，它不会产生空间碎片，G1收集器还可以进行非常精准的停顿控制，它可以让开发人员指定在长度为M的时间段中，垃圾回收时间不超过N。使用以下参数可以启用G1收集器：  
````-XX:+UnlockExperimentalVMOptions -XX:+UseG1GC```，设置G1回收器的目标停顿时间：```-XX:MaxGCPauseMillis=50 -XX:GCPauseIntervalMillis=200```此参数指定在200ms内，停顿时间不超过50ms，这两个参数是G1回收器的目标，G1回收器并不保证执行他们。

#### 收集器对程序性能的影响  
通过GCTimeTest样例测试程序，观察不同垃圾收集器对应用程序性能的直接影响，测试数据见下表：  

![垃圾收集器对应用程序性能的影响][007]


### GC相关参数总结  
#### 与串行回收器相关的参数
-XX:+UseSerialGC：在新生代和老年代使用串行收集器  
-XX:SurvivorRatio：新生代中eden区与survivor区的比例。  
-XX:PretenureSizeThreshold：设置大对象直接进入老年代的阈值。当对象的大小超过这个值时，将直接在老年代分配。
-XX:MaxTenuringThreshold：设置对象进入老年代的年龄的最大值。每一次MinorGC后，对象年龄就加1。任何大于这个年龄的对象，一定会进入老年代。

#### 与并行GC相关的参数  
-XX:+UseParNewGC：在新生代使用并行收集器  
-XX:+UseParallelOldGC：老年代使用并行回收收集器  
-XX:ParallelGCThreads=8：设置用于垃圾回收的线程数。一般情况下可以和CPU数量相等。但在CPU数量比较多的情况下，设置相对较小的数值也是合理的。
-XX:MaxGCPauseMillis：设置最大垃圾收集停顿时间。它的值是一个大于0的整数。收集器在工作时，会调整Java堆大小或其他参数，尽可能把停顿时间控制在
MaxGCPauseMillis以内。    
-XX:GCTimeRadio：设置吞吐量大小，它的值时一个0~100之间的整数。假设GCTimeRadio的值为n，那么系统将花费不超过1/(1+n)的时间用于垃圾收集。比如GCTimeRadio等于19（默认值），则系统用于垃圾收集的时间不超过1/(1+19)=5%。默认情况下，它的取值是99，既不超过1/(1+99)=1%的时间用于垃圾收集。  
-XX:+UseAdaptiveSizePolicy：可以打开自适应GC策略。在这种模式下，新生代大小、eden和survivor的比例、晋升老年代的对象年龄等参数会被自动调整，以达到在堆大小、吞吐量和停顿时间之间的平衡点。在手动调优比较困难的场合，可以直接使用这种自适应的方式，仅指定虚拟机的最大堆、目标吞吐量和停顿时间，让虚拟机自己完成调优工作。  

#### 与CMS相关的参数  
-XX:+UseConcMarkSweepGC：新生代使用并行收集器，老年代使用CMS+串行收集器。  
-XX:ParallelCMSThreads：设置CMS线程数量  
-XX:CMSInitiatingOccupancyFraction：设置CMS收集器在老年代空间被使用多少后触发，默认为68%。  
-XX:+UseCMSCompactAtFullCollection：开关可以使CMS在垃圾收集完成后，进行一次碎片整理。内存碎片的整理不是并发进行的。  
-XX:CMSFullGCsBeforeCompaction：参数可以用于设定进行多少次CMS回收后，进行一次内存压缩。  
-XX:+CMSClassUnloadingEnabled：允许对类元数据进行回收。  
-XX:+CMSParallelRemarkEnabled：启用并行重标记。
-XX:CMSInitiatingPermOccupancyFraction：当永久区占用率达到这一百分比时，启动CMS回收（前提是-XX:+CMSClassUnloadingEnabled激活了）  
-XX:UseCMSInitiatingOccupancyOnly：表示只在到达阈值的时候，才进行CMS回收。
-XX:+CMSIncrementalMode：设置为增量模式，比较适合单CPU。  

#### 与G1回收器相关的参数  
-XX:+UseG1GC：使用G1回收器  
-XX:+UnlockExperimentalVMOptions：允许使用试验性参数
-XX:MaxGCPauseMillis：设置最大垃圾收集停顿时间  
-XX:GCPauseIntervalMillis：设置停顿间隔时间

#### 其他参数  
-XX:+DisableExplicitGC：禁用显示GC

### 常用调优案例和方法

#### 将新对象预留在新生代  
由于FullGC的成本要远远高于Minor GC，因此尽可能将对象分配在新生代是一种明智的做法。虽然大部分情况下，JVM会尝试在eden区分配对象，但是由于空间紧张等问题，很可能不得不将部分年轻对象提前向老年代压缩。因此，在JVM参数调优中，可以为应用程序分配一个合理的新生代空间，以最大限度避免新对象直接进入老年代的情况。  
参见PutInEden实例。  
结论：由于新生代垃圾回收的速度高于老年代回收，因此，将年轻对象预留在新生代有利于提高整体的GC效率。

#### 大对象进入老年代  
虽然在大部分情况下，将对象分配在新生代是合理的，但是，对于大对象，这种做法却是值得商榷的。因为大对象出现在新生代很可能扰乱新生代GC，并破坏新生代原有的对象结构。因为尝试在新生代分配大对象，很可能导致空间不足，为了足够的空间容纳大对象，JVM不得不将新生代中的年轻对象挪到老年代。因为大对象占用空间多，所以，可能需要移动大量小的年轻对象进入老年代，这对GC来说是相当不利的。  
基于以上原因，可以将大对象直接分配到老年代，保持新生代对象结构的完整性。以提高GC效率。如果非常不幸，一个大对象同时又是短命对象，假设这种情况出现得比较频繁，那么对于GC来说将会是一场灾难，原本应该用于存放永久对象的老年代，被短命的对象填满，这也意味着对堆空间进行了洗牌。扰乱了分代内存回收的基本思路。因此在开发中应尽量避免使用短命大对象。  
可以使用参数-XX:PretenureSizeThreshold设置大对象直接进入老年代的阈值，当对象超过这个值时，将直接在老年代分配。

#### 设置对象进入老年代的年龄  
在堆中，每个对象都有自己的年龄。一般情况下，年轻对象存放在新生代，年老对象存放在老年代。为了做到这点，虚拟机为每个对象都维护一个年龄。  
如果对象在eden区，经过一次GC后还存活，则被移动到survival区中，对象年龄加1。以后，对象每经过一次GC依然活动的，则年龄加1。当对象年龄达到阈值时，就移入老年代，成为老年对象。这个阈值的最大值可以通过参数-XX:MaxTenuringThreshold来设置，它的默认是15 。虽然-XX:MaxTenuringThreshold的值是15或者更大，但这不意味着新对象非要达到这个年龄才能进入老年代。事实上，对象实际进入老年代的年龄是虚拟机在运行时根据内存使用情况动态计算的，这个参数指定的是阈值年龄的最大值。即，实际晋升老年代年龄等于动态计算所得的年龄与-XX:MaxTenuringThreshold中较小的那个。

#### 稳定与震荡的堆大小  
一般来说，稳定的堆大小是对垃圾回收有利的。获得一个稳定的堆大小的方法是使-Xms和-Xmx的大小一致，即最大堆和最小堆一样。如果这样设置，系统在运行时，堆大小是恒定的，稳定的堆空间可以减少GC的次数。因此，很多服务端应用都会将最大堆和最小堆设置为相同的数值。  
但是，一个不稳定的堆也并不是毫无用处。稳定的堆大小虽然可以减少GC次数，但同时也增加了每次GC的时间，让堆大小在一个区间中震荡，在系统不需要使用大内存时，压缩堆空间，使GC应对一个较小的堆，可以加快单次GC的速度，基于这样的考虑，JVM还提供了二个参数用于压缩和扩展堆空间。  
-XX:MinHeapFreeRatio：设置堆空间最小空闲比例。默认为40，当堆空间的空闲内存小于这个数值时，JVM便会扩展堆空间。  
-XX:MaxHeapFreeRatio：设置堆空间最大空闲比例。默认70，当堆空间的空闲内存大于这个数值时，便会压缩堆空间，得到一个较小的堆。
当-Xms和-Xmx相等时，-XX:MinHeapFreeRatio和-XX:MaxHeapFreeRatio是无效的

#### 吞吐量优先案例  
吞吐量优先的方案将会尽可能减少系统的执行垃圾回收的总时间，故可以考虑关注系统吞吐量的并行回收收集器。在拥有4GB内存和32核CPU的计算机上，进行吞吐量优先的优化，可以使用以下参数：  
```
java -Xmx3800m -Xms3800m -Xmn2g -Xss128k -XX:+UseParallelGC -XX:ParallelGCThreads=20 -XX:+UseParallelOldGC
```
* -Xmx3800m -Xms3800m：设置Java堆的最大值和最小值。为了避免堆内存的频繁震荡导致系统性能下降，让最小堆等于最大堆是比较常用的做法，假设将最小堆设为最大堆的一半1900Mb，那么JVM会尽可能在1900Mb堆空间中运行，如果这样，它发生GC的可能性就会比较高。  
* -Xss128k：减小线程栈的大小，这样可以使剩余的系统内存支持更多的线程。  
* -Xmn2g：设置新生代大小  
* -XX:+UseParallelGC：新生代使用并行回收收集器，这是一个关注吞吐量的收集器，可以尽可能减少GC时间。  
* -XX:ParallelGCThreads：设置用于垃圾回收的线程数，通常 情况下可以和CPU数量相等。但在CPU数量较多的情况下，设置相对较小的数值也是合理的。  
* -XX:+UseParallelOldGC：老年代也是用并行回收收集器  

#### 使用大页案例（通过-XX:LargePageSizeInBytes来设置大页大小）  

#### 降低停顿案例  
为了降低应用软件在垃圾回收时的停顿，首先考虑的是使用关注系统停顿的CMS回收器，其次，为了减少FULL GC次数,应尽可能将对象预留在新生代，因为新生代Minor GC的成本远远小于老年代的Full GC。  
```
java -Xms3550m -Xmx3550m -Xmn2g -Xss128k -XX:ParallelGCThreads=20 -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:SurvivorRatio=8 -XX:TargetSurvivorRatio=90 -XX:MaxTenuringThreshold=31
```

-XX:ParallelGCThreads=20：设置20的线程进行垃圾回收  
-XX:+UseParNewGC：新生代使用并行回收器  
-XX:+UseConcMarkSweepGC：老年代使用CMS收集器降低停顿  
-XX:SurvivorRatio=8：设置eden区和survivor区的比例为8:1。稍大的survivor空间可以提高新生代回收较短生命周期对象的可能性（如果survivor不够大，一些短命对象直接进入老年代，这对系统是不利的）  
-XX:TargetSurvivorRatio=90：设置survivor的可使用率，这里设置为90%，则允许90%的survivor空间被使用。默认值为50%。该设置提高了survivor区的使用率。当存放的对象超过这个百分比，则对象会向老年代压缩。因此这个选项更有助于将对象留在新生代。  
-XX:MaxTenuringThreshold=31：设置年轻对象晋升到老年代的年龄。默认值是15次，也就是说经过15次的Minor GC依然存活，则进入老年代。这里设置为31即尽可能将对象保留在新生代。

### 实用JVM参数  

#### JIT编译参数  
参见TestJIT用例  

#### 堆快照  
在性能问题排查中，分析堆快照（Dump）是必不可少的一环。获得程序的堆快照文件有多种方法。介绍一种比较常用的取得堆快照文件的方法，即使用-XX:+HeapDumpOnOutOfMemoryError参数在程序发生OOM时，导出应用程序的当前堆快照。这是非常有用的一种方法，因为当程序发生OOM退出系统时，一些瞬时信息都随着程序的终止而消失，而重现OOM问题往往比较困难或者耗时。  
因此在OOM发生时，通过-XX:+HeapDumpOnOutOfMemoryError选项将当前的堆信息保存到文件中，对于排查当前问题是很有帮助的。通过参数-XX:HeapDumpPath可以指定堆快照的保存位置。
使用以下参数运行Java程序，可以在程序OOM时，导出信息到C盘的m.hprof文件中，``` -Xmx10M -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=c:\m.hprof ```,导出的Dump文件可以通过Visual VM等多种工具查看分析，进而定位问题原因。 参见：DumpOOMTest实例  
****
#### 错误处理  
在系统发生OOM错误时，虚拟机在错误发生时运行一段第三方脚本。比如，当OOM发生时，重置系统：``` -XX:OnOutOfMemoryError=c:\reset.bat ``` 

#### 取得GC信息  
获取GC信息是Java程序调优的重要一环，JVM虚拟机也提供了许多参数帮助开发人员获取GC信息。要获取一段简要的GC信息，可以使用-verbose:gc或者-XX:+PrintGC。
```
[GC 2367K->1592K(9728K), 0.0034555 secs]
[GC 3729K->1640K(9728K), 0.0023539 secs]
[GC 3729K->3680K(9728K), 0.0015296 secs]
[GC 5755K->4736K(9728K), 0.0024195 secs]

```
这段输出中显示了GC前的堆栈情况，已经GC后的堆栈大小和堆栈的总大小，最后，显示了这次GC的耗时。  
如果要获得更加详细的信息，可以使用-XX:+PrintGCDetails。它的一段典型输出如下：  
```
[GC [PSYoungGen: 2367K->504K(3072K)] 2367K->1608K(9728K), 0.0024248 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
[GC [PSYoungGen: 2641K->488K(3072K)] 3745K->1632K(9728K), 0.0010172 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
[GC [PSYoungGen: 2560K->488K(3072K)] 3704K->3680K(9728K), 0.0040908 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
[GC [PSYoungGen: 2569K->488K(3072K)] 5761K->4728K(9728K), 0.0009823 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
[GC [PSYoungGen: 2558K->488K(3072K)] 6798K->4728K(9728K), 0.0107948 secs] [Times: user=0.05 sys=0.00, real=0.01 secs] 
[GC [PSYoungGen: 2543K->488K(2048K)] 6783K->6776K(8704K), 0.0031579 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
[Full GC [PSYoungGen: 488K->0K(2048K)] [ParOldGen: 6288K->2581K(6656K)] 6776K->2581K(8704K) [PSPermGen: 2943K->2943K(21504K)], 0.0122574 secs] [Times: user=0.03 sys=0.00, real=0.01 secs] 
[Full GC [PSYoungGen: 1028K->0K(2048K)] [ParOldGen: 5653K->533K(6656K)] 6682K->533K(8704K) [PSPermGen: 2944K->2944K(21504K)], 0.0066235 secs] [Times: user=0.03 sys=0.00, real=0.01 secs] 
Heap
 PSYoungGen      total 2048K, used 1061K [0x00000000ffc80000, 0x0000000100000000, 0x0000000100000000)
  eden space 1536K, 69% used [0x00000000ffc80000,0x00000000ffd896b0,0x00000000ffe00000)
  from space 512K, 0% used [0x00000000fff80000,0x00000000fff80000,0x0000000100000000)
  to   space 1024K, 0% used [0x00000000ffe00000,0x00000000ffe00000,0x00000000fff00000)
 ParOldGen       total 6656K, used 4629K [0x00000000ff600000, 0x00000000ffc80000, 0x00000000ffc80000)
  object space 6656K, 69% used [0x00000000ff600000,0x00000000ffa85698,0x00000000ffc80000)
 PSPermGen       total 21504K, used 2961K [0x00000000fa400000, 0x00000000fb900000, 0x00000000ff600000)
  object space 21504K, 13% used [0x00000000fa400000,0x00000000fa6e44e0,0x00000000fb900000)

```

-XX:+PrintGCDetails的输出显然比之前的两个参数详细许多。它不仅包含了GC的总体情况，还分别给出了新生代、老年代以及永久代各自的GC信息，以及GC消耗的时间。如果还需要在GC发生的时刻打印GC发生的时间，则可以追加使用``` -XX:PrintGCTimeStamps ```选项。打开这个开关之后，将额外输出GC的发生时间，以此，可以知道GC的频率和间隔。

```
0.139: [GC [PSYoungGen: 2367K->488K(3072K)] 2367K->1608K(9728K), 0.0015684 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.141: [GC [PSYoungGen: 2625K->504K(3072K)] 3745K->1624K(9728K), 0.0096287 secs] [Times: user=0.05 sys=0.00, real=0.01 secs] 
0.151: [GC [PSYoungGen: 2595K->488K(3072K)] 3715K->3688K(9728K), 0.0010472 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
```

如果需要查看新生代晋升老年代的阈值，可以使用参数-XX:+PrintTenuringDistribution查看。使用参数-XX:+PrintTenuringDistribution -XX:MaxTenuringThreshold=18运行一段Java程序，它的部分输出可能如下：  
```

0.105: [GC
Desired survivor size 524288 bytes, new threshold 7 (max 18)
 [PSYoungGen: 2367K->504K(3072K)] 2367K->1600K(9728K), 0.0025584 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.108: [GC

```
可以看到，在程序运行时，对象实际晋升老年的年龄是7，最大年龄是18（由-XX:MaxTenuringThreshold=18指定）。如果需要在GC时打印详细的信息，则可以打开```-XX:+PrintHeapAtGC```开关。一旦打开它，那么每次GC时，都将打印堆的使用情况。当然这个输出量将是巨大的。

````
{Heap before GC invocations=1 (full 0):
 PSYoungGen      total 3072K, used 2433K [0x00000000ffc80000, 0x0000000100000000, 0x0000000100000000)
  eden space 2560K, 95% used [0x00000000ffc80000,0x00000000ffee0618,0x00000000fff00000)
  from space 512K, 0% used [0x00000000fff80000,0x00000000fff80000,0x0000000100000000)
  to   space 512K, 0% used [0x00000000fff00000,0x00000000fff00000,0x00000000fff80000)
 ParOldGen       total 6656K, used 0K [0x00000000ff600000, 0x00000000ffc80000, 0x00000000ffc80000)
  object space 6656K, 0% used [0x00000000ff600000,0x00000000ff600000,0x00000000ffc80000)
 PSPermGen       total 21504K, used 3047K [0x00000000fa400000, 0x00000000fb900000, 0x00000000ff600000)
  object space 21504K, 14% used [0x00000000fa400000,0x00000000fa6f9c00,0x00000000fb900000)
Heap after GC invocations=1 (full 0):
 PSYoungGen      total 3072K, used 504K [0x00000000ffc80000, 0x0000000100000000, 0x0000000100000000)
  eden space 2560K, 0% used [0x00000000ffc80000,0x00000000ffc80000,0x00000000fff00000)
  from space 512K, 98% used [0x00000000fff00000,0x00000000fff7e010,0x00000000fff80000)
  to   space 512K, 0% used [0x00000000fff80000,0x00000000fff80000,0x0000000100000000)
 ParOldGen       total 6656K, used 1168K [0x00000000ff600000, 0x00000000ffc80000, 0x00000000ffc80000)
  object space 6656K, 17% used [0x00000000ff600000,0x00000000ff724020,0x00000000ffc80000)
 PSPermGen       total 21504K, used 3047K [0x00000000fa400000, 0x00000000fb900000, 0x00000000ff600000)
  object space 21504K, 14% used [0x00000000fa400000,0x00000000fa6f9c00,0x00000000fb900000)
}

````
以上是使用-XX:+PrintHeapAtGC打印的堆使用情况。它分为两个部分：GC前堆信息和GC后堆信息。这里不仅包括了新生代、老年代和永久代的使用大小和使用率，还包括新生代中eden和survival区的使用情况。如果需要查看GC与应用程序相互执行的耗时，可以使用``` -XX:+PrintGCApplicationStoppedTime ```和``` -XX:+PrintGCApplicationConcurrentTime ```参数。它们将分别显示应用程序在GC发生时的停顿时间和应用程序在GC停顿期间的执行时间。他们输出如下：  
```
Application time: 0.0092123 seconds
[GC 2419K->1608K(9728K), 0.0038183 secs]
Total time for which application threads were stopped: 0.0039265 seconds, Stopping threads took: 0.0000134 seconds
Application time: 0.0005740 seconds

```

为了将以上输出信息保存至文件，可以使用-Xloggc参数指定GC日志的输出位置。如：-Xloggc:c:\gc.log，将GC日志输出到C盘下的gc.log文件中，便于日后分析。

#### 类和对象跟踪  
JVM还提供了一组参数用于获取系统运行时加载、卸载类的信息。``` -XX:+TraceClassloading ```参数用于跟踪类加载情况，``` -XX:+TraceClassUnloading ```用于跟踪类卸载情况。如果需要同时跟踪类的加载和卸载信息，可以同时打开这两个开关。也可以使用``` -verbose:class ```参数。  
除了类的跟踪，JVM还提供了```-XX:+PrintClassHistogram ```开关用于打印运行时实例的信息。

#### 控制GC  
  














[001]:./堆分配参数一览.png '图形化堆分配参数的含义'
[002]:./垃圾收集器的分类.png '垃圾收集器按不同角度的分类'
[003]:./串行回收示意图.png '串行收集器工作示意图'
[004]:./新生代并行收集器示意图.png '新生代并行收集器示意图'
[005]:./老年代并行回收收集器示意图.png '老年代并行回收收集器示意图'
[006]:./CMS工作示意图.png 'CMS工作示意图'
[007]:./不同垃圾收集器对程序性能的影响.png '不同垃圾收集器对程序性能的影响'