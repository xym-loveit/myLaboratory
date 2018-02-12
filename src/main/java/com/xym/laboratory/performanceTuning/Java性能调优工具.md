### Java性能调优工具  
#### top命令  
top命令是Linux下常用的性能分析工具，能够实时显示系统中各个进程的资源占用状况。top命令输出如下：  

```
top - 17:30:05 up 91 days, 16:28,  3 users,  load average: 0.00, 0.03, 0.05
Tasks: 111 total,   1 running, 110 sleeping,   0 stopped,   0 zombie
%Cpu(s):  0.2 us,  0.2 sy,  0.0 ni, 99.6 id,  0.0 wa,  0.0 hi,  0.0 si,  0.0 st
KiB Mem :  8010424 total,   581656 free,  3225296 used,  4203472 buff/cache
KiB Swap:        0 total,        0 free,        0 used.  4437488 avail Mem 

  PID USER      PR  NI    VIRT    RES    SHR S  %CPU %MEM     TIME+ COMMAND                                                                                                                 
 1139 redis     20   0  200244  51592   1552 S   0.4  0.6 166:30.35 redis-server                                                                                                            
 1997 dyqz      20   0 3938108 272816  11692 S   0.4  3.4  61:22.06 java                                                                                                                    
14584 dyqz      20   0 3938044 566488  11516 S   0.4  7.1   0:32.88 java                                                                                                                    
15228 dev       20   0  157692   2192   1540 R   0.4  0.0   0:00.01 top                                                                                                                     
28014 root      20   0   31496   2600   1920 S   0.4  0.0  74:00.54 AliYunDunUpdate                                                                                                         
28531 dyqz      20   0 3942360 563548  12028 S   0.4  7.0   6:34.07 java 

```

top命令分为两部分：前半部分是系统统计信息，后半部分是进程信息。在统计信息中，第一行是任务队列信息，它的结果等同于uptime命令。从左到右依次表示：系统当前时间、系统运行时间、当前登录用户数。最后的 load average表示系统的平均负载，即任务队列的平均长度，这3个值分别表示为1分钟、5分钟、15分钟到现在的平均值。  
第2行是进程统计信息，分别有正在进行的进程数、睡眠进程数、停止进程数、僵尸进程数。第3行是CPU的统计信息，us表示用户空间CPU占用率、sy表示内核空间CPU占用率、ni表示用户进程空间改变过优先级的进程CPU占用率、id表示空闲CPU占用率、wa表示等待输入输出的CPU时间百分比、hi表示硬件中断请求、si表示软件中断请求。在Mem行中，从左到右依次表示：物理内存总量、空闲物理内存、已使用的物理内存、内核缓冲使用量。Swap行依次表示：交换区总量、空闲交换区大小、缓冲交换区大小。  
top命令的第二部分是进程信息区，显示了系统各个进程的资源使用情况，各列使用含义如下：  

* PID：进程ID
* PPID：父进程ID
* RUSER：Real user name
* UID：进程所有者的用户ID
* USER：进程所有者的用户名
* GROUP：进程所有者的组名
* TTY：启动进程的终端名。不是从终端启动的进程则表示为？。
* PR： 优先级
* NI：nice值。负值表示高优先级，正值表示低优先级
* P：最后使用的CPU，仅在多CPU环境下有意义
* %CPU：上次更新到现在的CPU时间占用百分比
* TIME：进程使用CPU时间总计，单位为秒
* TIME+：进程使用CPU时间总计，单位为1/100秒
* %MEM：进程使用的物理内存百分比
* VIRT：进程使用的虚拟内存总量，单位KB，VIRT=SWAP+RES
* SWAP：进程使用的虚拟内存中被换出的大小，单位KB
* RES：进程使用的未被换出的物理内存大小，单位KB。RES=CODE+DATA
* CODE：可执行代码占用的物理内存大小，单位KB
* DATA：可执行代码以外的部分（数据段+栈）占用的物理内存的大小，单位KB
* SHR：共享内存大小，单位KB
* nFLT：页面错误次数
* nDRT：最后一次写入到现在，被修改过的页面数
* S：进程状态。D表示不可中断的睡眠状态；R表示运行；S表示睡眠；T表示跟踪/停止；Z表示僵尸进程
* COMMAND：命令名/命令行
* WCHAN：若该进程在睡眠，则显示睡眠中的系统函数名
* Flags：任务标志，参见sched.h  

在top命令下，按下f建可以进行列的选择，按下o键可以更改列的显示顺序。此外top命令还有一些实用的交互指令：  
* h：显示帮助信息  
* k：终止一个进程
* q：退出程序
* c：切换显示命令名称和完整命令行
* M：根据驻留内存大小进行排序
* P：根据CPU使用百分比大小进行排序
* T：根据时间/累计时间进行排序
* 数字1：显示所有CPU负载情况

使用top命令可以从宏观上观察系统各个进程对CPU的占用情况以及内存使用情况

#### sar命令  
sar命令也是linux系统中重要的性能监测工具之一，它可以周期性地对内存和CPU使用情况进行采样。基本语法如下：  
```sar [options] [<interval>] [<count>] ``` 
interval和count分别表示采样周期和采样数量。options选项可以指定sar命令对哪些性能数据进行采样（不同版本的sar命令，选项可能有所不同，可以通过sar -h命令查看）。  
* -A：所有报告的总和
* -u：CPU利用率
* -d：硬盘使用报告
* -b：I/O的情况
* -q：查看队列长度
* -r：内存使用统计信息
* -n：网络信息统计
* -o：采样结果输出到文件

下例使用sar命令统计CPU使用情况，每秒钟采样一次，共计采样3次：  
```
[dev@xxx~]$ sar -u 1 3
Linux 3.10.0-514.26.2.el7.x86_64 (iZbp1cl4i8oy1rll09fghhZ) 	02/11/2018 	_x86_64_	(4 CPU)

09:57:34 PM     CPU     %user     %nice   %system   %iowait    %steal     %idle
09:57:35 PM     all      0.25      0.00      0.00      0.00      0.00     99.75
09:57:36 PM     all      0.25      0.00      0.00      0.00      0.00     99.75
09:57:37 PM     all      0.00      0.00      0.25      0.00      0.00     99.75
Average:        all      0.17      0.00      0.08      0.00      0.00     99.75

```
获取内存使用情况：  
```
[dev@xxx~]$ sar -r 1 3
Linux 3.10.0-514.26.2.el7.x86_64 (iZbp1cl4i8oy1rll09fghhZ) 	02/11/2018 	_x86_64_	(4 CPU)

10:00:14 PM kbmemfree kbmemused  %memused kbbuffers  kbcached  kbcommit   %commit  kbactive   kbinact   kbdirty
10:00:15 PM    576452   7433972     92.80    288384   3592344   5093652     63.59   5105592   1911476       748
10:00:16 PM    576452   7433972     92.80    288384   3592344   5093652     63.59   5105592   1911476       748
10:00:17 PM    576452   7433972     92.80    288384   3592344   5093652     63.59   5105592   1911476       748
Average:       576452   7433972     92.80    288384   3592344   5093652     63.59   5105592   1911476       748

```

获取I/O信息：  
```
[dev@xxx~]$ sar -b 1 3
Linux 3.10.0-514.26.2.el7.x86_64 (iZbp1cl4i8oy1rll09fghhZ) 	02/11/2018 	_x86_64_	(4 CPU)

10:01:24 PM       tps      rtps      wtps   bread/s   bwrtn/s
10:01:25 PM      0.00      0.00      0.00      0.00      0.00
10:01:26 PM      0.00      0.00      0.00      0.00      0.00
10:01:27 PM      0.00      0.00      0.00      0.00      0.00
Average:         0.00      0.00      0.00      0.00      0.00

```
sar命令可以查看I/O信息、内存信息以及CPU使用情况。  

#### vmstat命令  
vmstat也是一款功能比较齐全的性能监测工具，它可以统计CPU、内存使用情况、swap使用情况等信息。和sar工具类似，vmstat也可以指定采样周期和采样次数。下例每秒采样1次，共计3次：  
```
[dev@xxx~]$ vmstat 1 3
procs -----------memory---------- ---swap-- -----io---- -system-- ------cpu-----
 r  b   swpd   free   buff  cache   si   so    bi    bo   in   cs us sy id wa st
 1  0      0 572152 288384 3925520    0    0     0    10    0    0  0  0 100  0  0
 0  0      0 572136 288384 3925552    0    0     0    28  480  781  0  0 100  0  0
 0  0      0 572136 288384 3925552    0    0     0    64  478  756  0  0 100  0  0

```
输出结果表中，各个列的含义如下：  
![vmstat命令输出含义][001]

vmstat工具可以查看内存、交互分区、I/O操作、上下文切换、时钟中断以及CPU的使用情况。

#### iostat命令  
iostat可以提供详尽的I/O信息。它的基本使用如下：  
```
[dev@xxx~]$ iostat 1 2
Linux 3.10.0-514.26.2.el7.x86_64 (iZbp1cl4i8oy1rll09fghhZ) 	02/11/2018 	_x86_64_	(4 CPU)

avg-cpu:  %user   %nice %system %iowait  %steal   %idle
           0.22    0.00    0.12    0.02    0.00   99.64

Device:            tps    kB_read/s    kB_wrtn/s    kB_read    kB_wrtn
vda               1.18         0.61        37.87    4851177  300650776
vdb               0.21         0.06         2.53     512917   20051592

avg-cpu:  %user   %nice %system %iowait  %steal   %idle
           0.25    0.00    0.00    0.00    0.00   99.75

Device:            tps    kB_read/s    kB_wrtn/s    kB_read    kB_wrtn
vda               0.00         0.00         0.00          0          0
vdb               0.00         0.00         0.00          0          0

```
以上命令显示了CPU使用概况和磁盘I/O的信息。输出信息每1秒采样1次，合计采样2次。如果只需要显示磁盘情况，不需要显示CPU使用情况，则可以使用如下命令：  
```
iostat -d 1 2
```
-d表示输出磁盘使用情况。结果中各个列的含义如下：  
* tps：该设备每秒的传送次数
* kB_read/s：每秒从设备读取的数据量
* kB_wrtn/s：每秒向设备写入的数据量
* kB_read：读取的总数据量
* kB_wrtn：写入的总数据量  

如果需要得到更多统计信息，可以使用-x选项。如：  
```
iostat -x 1 2
```
磁盘I/O很容易成为系统性能瓶颈，通过iostat可以快速定位系统是否产生了大量的I/O操作。


#### pidstat工具  
pidstat是一个功能强大的性能监测工具，它也是Sysstat的组件之一。可以在[http://sebastien.godard.pagesperso-orange.fr/download.html](http://sebastien.godard.pagesperso-orange.fr/download.html)下载这个工具。下载后，通过``` /configure、make、make install ``` 等3个命令即可安装pidstat工具。pidstat的强大之处在于，它不仅可以监视进行的性能情况，也可以监视线程的性能情况。  

##### CPU使用率监控  
参见HoldCPUMain

运行以上程序，要监控该程序的CPU使用率，可以先用jps命令找到Java程序的PID，然后使用pidstat命令输出程序的CPU使用情况。
```
[root@xxx]# jps
53560 HoldCPUMain
53614 Jps

[root@xxx]# pidstat -p 53560 -u 1 3
Linux 3.10.0-693.11.1.el7.x86_64 (xym) 	2018年01月20日 	_x86_64_	(1 CPU)

19时37分45秒   UID       PID    %usr %system  %guest   %wait    %CPU   CPU  Command
19时37分46秒     0     53560  100.00    0.00    0.00    0.00  100.00     0  java
19时37分47秒     0     53560  100.00    0.00    0.00    0.00  100.00     0  java
19时37分48秒     0     53560  100.00    0.00    0.00    0.00  100.00     0  java
平均时间:     0     53560  100.00    0.00    0.00    0.00  100.00     -  java

```

pidstat的参数-p用于指定进程ID，-u表示对CPU使用率的监控。最后的参数1 3表示每秒钟采样一次，合计采样3次。从这个输出中可以看到，该应用程序CPU占用率几乎达100%。pidstat的功能不仅仅限于观察进程信息，它可以进一步监控线程的信息，使用以下命令：  
```
[root@xxx]# pidstat -p 53560 -u 1 3 -t
```
这个命令的部分输出如下：  
```
Linux 3.10.0-693.11.1.el7.x86_64 (xym) 	2018年01月20日 	_x86_64_	(1 CPU)

19时39分05秒   UID      TGID       TID    %usr %system  %guest   %wait    %CPU   CPU  Command
19时39分06秒     0     53560         -   99.00    1.00    0.00    0.00  100.00     0  java
19时39分06秒     0         -     53569    0.00    0.00    0.00    6.00    0.00     0  |__java
19时39分06秒     0         -     53570   99.00    0.00    0.00    1.00   99.00     0  |__java
19时39分06秒     0         -     53573    0.00    0.00    0.00    1.00    0.00     0  |__java

```
-t参数将系统性能的监控细化到线程级别。从这个输出中可以知道，该Java应用程序之所以占用如此之高的CPU，是因为线程53570的缘故。  
使用以下命令可以导出指定Java应用程序的所有线程：  
```
jstack -l PID >/tmp/t.txt
```
在输出的t.txt文件中，可以找到这么一段输出内容：  
```
"Thread-0" #8 prio=5 os_prio=0 tid=0x00007f8f040ed800 nid=0xd142 runnable [0x00007f8f093ff000]
   java.lang.Thread.State: RUNNABLE
        at HoldCPUMain$HoldCPUTask.run(HoldCPUMain.java:25)
        at java.lang.Thread.run(Thread.java:748)

   Locked ownable synchronizers:
        - None
```

从中可以看出，这个线程正是HoldCPUTask类，它的nid（native id）为0xd142，使用python 将10进制线程id转为16进制，``` [root@xxx]# printf "%x\n" 53570 d142 ```，正好是d142。  
通过这个方法，开发人员可以使用pidstat很容易地捕获到在Java应用程序中大量占用CPU的线程。

##### I/O使用监控  
磁盘I/O也是常见的性能瓶颈之一，使用pidstat也可以监控进程内线程的I/O情况。  
参见实例：HoldIOMain  
在程序运行过程中，使用以下命令监控程序I/O使用情况。其中``` 55769 ```是通过jps命令查询到的进程ID，-d参数表明监控对象为磁盘I/O。1 3表示每秒采样一次，合计采样3次。  
```
[root@xxx]# pidstat -p 55769 -d -t 1 3
Linux 3.10.0-693.11.1.el7.x86_64 (xym) 	2018年01月20日 	_x86_64_	(1 CPU)

20时02分34秒   UID      TGID       TID   kB_rd/s   kB_wr/s kB_ccwr/s iodelay  Command
20时02分35秒     0     55769         -      0.00   1392.00      0.00       0  java
20时02分35秒     0         -     55769      0.00      0.00      0.00       0  |__java
20时02分35秒     0         -     55779      0.00   1392.00      0.00       0  |__java
20时02分35秒     0         -     55780      0.00      0.00      0.00       0  |__java
20时02分35秒     0         -     55782      0.00      0.00      0.00       0  |__java

[root@xxx]# jstack -l 55769 >/tmp/t.txt

查找到

"Thread-0" #8 prio=5 os_prio=0 tid=0x00007f4b900ed800 nid=0xd9e3 runnable [0x00007f4b958cb000]
   java.lang.Thread.State: RUNNABLE
        at java.io.FileInputStream.read0(Native Method)
        at java.io.FileInputStream.read(FileInputStream.java:207)
        at HoldIOMain$HoldIOTask.run(HoldIOMain.java:33)
        at java.lang.Thread.run(Thread.java:748)

   Locked ownable synchronizers:
        - None

```
从输出结果中可以看到，进程中的55779（使用python 将10进制线程id转为16进制，``` [root@xxx]# printf "%x\n" 55779 d9e3 ```）线程产生了大量的I/O操作。通过前文中提到的jstack命令，可以导出当前线程堆栈，查找nid为的线程，即可定位到HoldIOTask线程。

##### 内存监控  
使用pidstat命令，还可以监控指定进程的内存使用情况。下例使用pidstat工具对进程ID为的进程进行内存监控。每秒钟刷新一次，共进行5次统计。  
```

[root@xxx]# pidstat -r -p 55833 1 5
Linux 3.10.0-693.11.1.el7.x86_64 (xym) 	2018年01月20日 	_x86_64_	(1 CPU)

20时11分59秒   UID       PID  minflt/s  majflt/s     VSZ     RSS   %MEM  Command
20时12分00秒     0     55833     13.00      0.00 2460728   22872   1.23  java
20时12分01秒     0     55833     11.88      0.00 2460728   22872   1.23  java
20时12分02秒     0     55833     14.00      0.00 2460728   22872   1.23  java
20时12分03秒     0     55833     12.00      0.00 2460728   22872   1.23  java
20时12分04秒     0     55833     13.00      0.00 2460728   23136   1.24  java
平均时间:     0     55833     12.77      0.00 2460728   22925   1.23  java

```

输出结果中各列的含义如下：  
* minflt/s：表示该进程每秒minor faults（不需要从磁盘中调出内存页）的总数。
* majflt/s：表示该进程每秒major faults（需要从磁盘中调出内存页）的总数。
* VSZ：表示该进程使用的虚拟内存大小，单位为KB。
* RSS：表示该进程占用的物理内存大小，单位为KB。
* %MEM：表示占用内存比率。

pidstat工具是一款多合一的优秀工具之一。它不仅可以监控CPU、I/O和内存资源，甚至可以将问题定位到相关线程，方便应用程序的故障排查。

##### windows操作系统下perfmon性能监控工具  
##### windows下Process Explorer工具  
##### windows下pslist命令行工具

#### JDK命令行工具  
在JDK的开发包中，除了大家熟知的java.exe和javac.exe外，还有一系列辅助工具。这些辅助工具可以帮助开发人员很好地解决Java应用程序的一些疑难杂症。这些工具在JDK安装目录下的bin目录中。下图显示了部分辅助工具：  
![JDK部分辅助工具][002]  
虽然乍一看，这些工具都是exe的可执行文件。但事实上，他们只是java程序的一层包装，其真正实现是在tools.jar中，如下图：  
![tools jar中的工具类][003]  
以jps为例，在控制台执行jps命令和java -classpath %JAVA_HOME%/lib/tools.jar sun.tools.jps.Jps命令是等价的，即jps.exe只是这个命令的一层包装。  
##### jps命令  
命令jps类似于Linux下的ps，但它只用于列出Java的进程。直接运行jps不加任何参数，可以列出Java程序进程ID以及Main函数等名称。
```
D:\soft\Java\jdk1.7.0_80\bin>jps
7060 Jps
7020 RemoteMavenServer
9584 Launcher

```
从这个输出中可以看到，当前系统中共存在3个Java应用程序，其中第一个输出Jps就是jps命令本身，这更加证明此命令的本质就是一个Java程序。此外，jps还提供了一系列参数来控制它的输出内容。  
参数-q指定jps只输出进程ID，而不输出类的短名称：  
```
D:\soft\Java\jdk1.7.0_80\bin>jps -q
9752
7020
9584

```

参数-m用于输出传递给Java进程（主函数）的参数：  
```
D:\soft\Java\jdk1.7.0_80\bin>jps -m
7632 Jps -m
7020 RemoteMavenServer
9584 Launcher D:/soft/JetBrains/IntelliJ IDEA 2017.2.5/lib/httpclient-4.5.2.jar;D:/soft/JetBrains/IntelliJ IDEA 2017.2.5/lib/aether-dependency-resolver.jar;D:/soft/JetBrains/IntelliJ IDEA 2017.2.5/lib/jna-platform.jar;D:/soft/JetBrains/IntelliJ IDEA 2017.2.5/lib/nanoxml-2.2.3.jar;D:/soft/JetBrains/IntelliJ IDEA 2017.2.5/lib/asm-all.jar;D:/soft/JetBrains/IntelliJ IDEA 2017.2.5/lib/javac2.jar;D:/soft/JetBrains/IntelliJ IDEA 2017.2.5/lib/slf4j-api-1.7.10.jar;D:/soft/JetBrains/IntelliJ IDEA 2017.2.5/lib/httpcore-4.4.5.jar;D:/soft/JetBrains/IntelliJ IDEA 2017.2.5/lib/jps-builders.jar;D:/soft/JetBrains/IntelliJ IDEA 2017.2.5/lib/maven-aether-provider-3.3.9-all.jar;D:/soft/JetBrains/IntelliJ IDEA 2017.2.5/lib/log4j.jar;D:/soft/JetBrains/IntelliJ IDEA 2017.2.5/lib/protobuf-2.5.0.jar;D:/soft/JetBrains/IntelliJ IDEA 2017.2.5/lib/jps-model.jar;D:/soft/JetBrains/IntelliJ IDEA 2017.2.5/lib/oromatcher.jar;D:/soft/JetBrains/IntelliJ IDEA 2017.2.5/lib/jna.jar;D:/soft/JetBrains/IntelliJ IDEA 2017.2.5/lib/

```

参数-l用于输出主函数的完整路径：  
```
D:\soft\Java\jdk1.7.0_80\bin>jps -l
2880 sun.tools.jps.Jps
7020 org.jetbrains.idea.maven.server.RemoteMavenServer
9584 org.jetbrains.jps.cmdline.Launcher

```

参数-v可以显示传递给JVM的参数：  
```
D:\soft\Java\jdk1.7.0_80\bin>jps -v

9912 Jps -Dapplication.home=D:\soft\Java\jdk1.7.0_80 -Xms8m 

7020 RemoteMavenServer -Djava.awt.headless=true -Didea.version==2017.2.6 -Xmx768m -Didea.maven.embedder.version=3.5.0 -Dfile.encoding=GBK 

9584 Launcher -Xmx700m -Djava.awt.headless=true -Djava.endorsed.dirs="" -Djdt.compiler.useSingleThread=true -Dpreload.project.path=d:/workspace/IdeaProjects/myLaboratory -Dpreload.config.path=C:/Users/xym/.IntelliJIdea2017.2/config/options -Dcompile.parallel=false -Drebuild.on.dependency.change=true -Djava.net.preferIP v4Stack=true -Dio.netty.initialSeedUniquifier=-1025285418659662309 -Dfile.encoding=GBK -Djps.file.types.component.name=FileTypeManager -Duser.language=zh -Duser.country=CN -Didea.paths.selector=IntelliJIdea2017.2 -Didea.home.path=D:\soft\JetBrains\IntelliJ IDEA 2017.2.5 -Didea.config.path=C:\Users\xym\.IntelliJIdea2017.2\config -Didea.plugins.path=C:\Users\xym\.IntelliJIdea2017.2\config\plugins -Djps.log.dir=C:/Users/xym/.IntelliJIdea2017.2/system/log/build-log -Djps.fallback.jdk.home=D:/soft/JetBrains/IntelliJ IDEA 2017.2.5/jre64 -Djps.fallback.jdk.version=1.8.0_152-release -Dio.netty.noUnsafe=true -Djava.io.tmpdir=C:/Users/xym/.IntelliJIdea2017.2/system/compile-server/mylaboratory_84b63e46/_temp_ -Djps.back

```

jps命令类似于ps命令，但是它只列出系统中所有的Java应用程序。通过jps命令可以方便地查看Java进程的启动类、传入参数和JVM参数等信息。  

##### jstat命令  
jstat是一个可以用于观察Java应用程序运行时信息的工具。它的功能非常强大，可以通过它，查看堆信息的详细情况。它的基本使用语法为：  
```
jstat -<option> [-t] [-h<lines>] <vmid> [<interval>] [<count>]

```
选项option可以由以下值构成：  
* -class：显示ClassLoader的相关信息
* -compiler：显示JIT编译的相关信息
* -GC：显示与GC相关的堆信息
* -gccapacity：显示各个代的容量及使用情况
* -gccause：显示垃圾收集相关信息（同-gcutil），同时显示最后一次或当前正在发生的垃圾收集的诱发原因
* -gcnew：显示新生代信息
* -gcnewcapacity：显示新生代大小与使用情况
* -gcold：显示老年代和永久代信息
* -gcoldcapacity：显示老年代的大小
* -gcpermcapacity：显示永久代的大小
* -gcutil：显示垃圾收集信息
* -printcompilation：输出JIT编译的方法信息
* -t：参数可以在输出信息前加一个Timestamp列，显示程序的运行时间
* -h：参数可以在周期性数据输出时，输出多少行数据后，跟着输出一个表头信息
* interval：参数用于指定输出统计数据的周期，单位为毫秒
* count：用于指定一共输出多少次数据

如下所示输出Java进程的ClassLoader相关信息，每秒钟输出一次，一共输出2次：  
```
[root@xxx]# jstat -class -t 56044 1000 2
Timestamp       Loaded  Bytes  Unloaded  Bytes     Time   
           25.4    417   858.7        0     0.0       0.04
           26.4    417   858.7        0     0.0       0.04
           
```
在-class的输出中，Loaded表示载入类的数量，Bytes表示载入类的合计大小，Unloaded表示卸载类的的数量，Bytes表示卸载类的大小，Time表示在加载和卸载类上所花费的时间。  
下例显示JIT编译的信息：  
```
[root@xxx]# jstat -compiler -t 56044
Timestamp       Compiled Failed Invalid   Time   FailedType FailedMethod
          252.0      103      0       0     0.12          0             

```
Compiled表示编译任务执行的次数，Failed表示编译失败的次数，Invalid表示编译不可用的次数，Time表示编译的总耗时，FailedType表示最后一次编译失败的类型，FailedMethod表示最后一次编译失败的类名和方法名。  

下例显示了与GC相关的堆信息输出：  
```
[root@xxx]# jstat -gc 56044
 S0C    S1C    S0U    S1U      EC       EU        OC         OU       MC     MU    CCSC   CCSU   YGC     YGCT    FGC    FGCT     GCT   
1024.0 1024.0 1024.0  0.0    8192.0   6424.4   20480.0     7294.3   4864.0 2672.2 512.0  286.2       2    0.028   0      0.000    0.028

```

各项参数的含义如下：  
* S0C：s0（from）的大小（KB）
* S1C：s1（to）的大小（KB）
* S0U：s0（from）已使用的空间（KB）
* S1U：s1（to）已使用的空间（KB） 
* EC：eden区大小（KB）
* EU：eden区的使用空间（KB）
* OC：老年代大小（KB）
* OU：老年代已经使用的空间（KB）
* PC：永久区大小（KB）
* PU：永久区使用空间（KB）
* YGC：新生代GC次数
* YGCT：新生代GC耗时
* FGC：Full GC次数
* FGCT：Full GC耗时
* GCT：GC总耗时

下例显示了各个代的信息，与-gc相比，它不仅输出了各个代的当前大小，也包含了各个代的最大值和最小值。

```
[root@xxx]# jstat -gccapacity 56339
 NGCMN    NGCMX     NGC     S0C   S1C       EC      OGCMN      OGCMX       OGC         OC       MCMN     MCMX      MC     CCSMN    CCSMX     CCSC    YGC    FGC 
 10240.0 155648.0  10240.0 1024.0 1024.0   8192.0    20480.0   311296.0    20480.0    20480.0      0.0 1056768.0   4480.0      0.0 1048576.0    384.0      0     0
 
```
各项参数的函数如下：  
* NGCMN：新生代最小值（KB）
* NGCMX：新生代最大值（KB）
* NGC：当前新生代大小（KB）
* OGCMN：老年代最小值（KB）
* OGCMX：老年代最大值（KB）
* PGCMN：永久代最小值（KB）
* PGCMX：永久代最大值（KB）

下例显示了最近一次GC的原因以及当前GC的原因：  
```
[root@xxx]# jstat -gccause 56339
  S0     S1     E      O      M     CCS    YGC     YGCT    FGC    FGCT     GCT    LGCC                 GCC                 
100.00   0.00  22.95  35.66  54.92  55.89      2    0.036     0    0.000    0.036 Allocation Failure   No GC

```
各项参数含义如下：  
* LGCC：上一次GC的原因
* GCC：当前GC的原因

本例显示，最近一次GC是由于对象分配空间失败导致，当前时刻未进行GC。  

-gcnew参数可以用于查看新生代的一些详细信息：  
```
[root@xxx]# jstat -gcnew 56339
 S0C    S1C    S0U    S1U   TT MTT  DSS      EC       EU     YGC     YGCT  
1024.0 1024.0    0.0 1024.0  1  15  512.0   8192.0   4298.3      3    0.047

```
各项参数含义如下：  
* TT：新生代对象晋升到老年代对象的年龄
* MTT：新生代对象晋升到老年代对象的年龄最大值
* DSS：所需的survivor区大小

-gcnewcapacity参数可以详细输出新生代各个区的大小信息：  
```
[root@xxx]# jstat -gcnewcapacity 56339
  NGCMN      NGCMX       NGC      S0CMX     S0C     S1CMX     S1C       ECMX        EC      YGC   FGC 
   10240.0   155648.0    10240.0  15552.0   1024.0  15552.0   1024.0   124544.0     8192.0     4     0

```
各项参数的含义如下：  
* S0CMX：s0区的最大值（KB）    
* S1CMX：s1区的最大值（KB）
* ECMX：eden区的最大值（KB）

-gcold用于展现老年代GC的概况：  
```
[root@xxx]# jstat -gcold 56339
   MC       MU      CCSC     CCSU       OC          OU       YGC    FGC    FGCT     GCT   
  4864.0   2673.2    512.0    286.2     20480.0     18408.0      5     0    0.000    0.077
  
```

-gcoldcapacity用于展现老年代容量信息：  
```
[root@xxx]# jstat -gcoldcapacity 56339
   OGCMN       OGCMX        OGC         OC       YGC   FGC    FGCT     GCT   
    20480.0    311296.0     22504.0     22504.0     6     1    0.053    0.166
    
```

-gcpermcapacity用于展示永久区使用情况。

-gcutil：用于展示GC回收相关信息：  
```
[root@xxx]# jstat -gcutil 56339
  S0     S1     E      O      M     CCS    YGC     YGCT    FGC    FGCT     GCT   
100.00   0.00  81.73  97.24  54.96  55.89      8    0.137     1    0.053    0.191

```
* S0：s0区使用的百分比
* S1：s1区使用的百分比
* E：eden区使用的百分比
* O：Old区使用的百分比
* P：永久区使用的百分比

jstat命令可以非常详细地查看Java应用程序的堆使用情况以及GC情况。

##### jinfo命令  
jinfo命令可以用来查看正在运行的Java应用程序的扩展参数，甚至支持在运行时修改部分参数。它的基本语法为：  
``` jinfo <option> <pid> ```  
其中option可以为以下信息：  
* -flag <name>：打印指定JVM的参数值 
* -flag [+|-]<name>：设置指定JVM参数的布尔值  
* -flag <name>=<value>：设置指定JVM参数的值  

在很多情况下，Java应用程序不会指定所有的JVM参数。而此时开发人员可能不知道某一个具体JVM参数的默认值。在这种情况下，可能需要通过查找文档获取某个参数的默认值。这个查找过程可能是非常艰难的，但有了jinfo工具，开发人员可以很方便地找到JVM参数的当前值。  
比如，下例显示了新生代对象晋升到老年代对象的最大年龄。在应用程序启动时，并没有指定这个参数，但通过jinfo，可以查看这个参数的当前值：  
```
[root@xxx]# jinfo -flag MaxTenuringThreshold 56339
-XX:MaxTenuringThreshold=15

```  
显示是否打印GC详细信息：  
```
[root@xxx]# jinfo -flag PrintGCDetails 56339
-XX:-PrintGCDetails

```
除了查找参数的值，jinfo也支持修改部分参数的数值，当然，这个修改能力是极其有限的。下例显示了通过jinfo对PrintGCDetails参数的修改，它可以在Java程序运行时，关闭或打开这个开关。
```

[root@xxx]# jinfo -flag PrintGCDetails 56339
-XX:-PrintGCDetails
[root@xxx]# jinfo -flag +PrintGCDetails 56339
[root@xxx]# jinfo -flag PrintGCDetails 56339
-XX:+PrintGCDetails

```

jinfo不仅可以查看运行时某一个JVM参数的实际取值，甚至可以在运行时修改部分参数，并使之立即生效。

##### jmap命令  
jmap可以生成Java应用程序的堆快照和对象的统计信息。下例使用jmap生成PID为56339的Java程序的对象统计信息，并输出到s.txt文件中：  
```
[root@xxx]# jmap -histo 56339 >/tmp/s.txt

```

输出文件有如下结构：  

```
 num     #instances         #bytes  class name
----------------------------------------------
   1:        214586        6866752  java.io.FileDescriptor
   2:        158887        6355480  java.lang.ref.Finalizer
   3:        214636        3434176  java.lang.Object
   4:        107294        3433408  java.io.FileOutputStream
   5:        107292        3433344  java.io.FileInputStream
   6:          1152          95696  [C
   7:          1778          74544  [B
   8:           483          55096  java.lang.Class

```
可以看到，这个输出显示了内存中的实例数量和合计。另一个更为重要的功能是得到Java程序的当前堆快照：  
```
[root@xxx]# jmap -dump:format=b,file=/tmp/heap.bin 56339
Dumping heap to /tmp/heap.bin ...
Heap dump file created

```

可以通过多种工具分析改文件，比如jhat工具，Visual VM工具等。  
jmap可用于导出Java应用程序的堆快照。  

##### jhat命令  
使用jhat工具可以用于分析Java应用程序的堆快照内容。以上例中jmap输出的堆文件heap.bin为例：  
```

[root@xxx]# jhat /tmp/heap.bin 
Reading from /tmp/heap.bin...
Dump file created Sun Jan 21 00:34:31 CST 2018
Snapshot read, resolving...
Resolving 524884 objects...
Chasing references, expect 104 dots........................................................................................................
Eliminating duplicate references........................................................................................................
Snapshot resolved.
Started HTTP server on port 7000
Server is ready.

```

jhat在分析完成后，使用Http服务器展示其分析结果。在浏览器中访问http://127.0.0.1:7000即可。  
jhat命令可以对堆快照文件进行分析。它启动一个HTTP服务器，开发人员可以通过浏览器，浏览Java堆快照。  

##### jstack命令  
jstack可用于导出Java应用程序的线程堆栈。语法为：  
```
jstack -l <pid>
```
-l选项用于打印锁的附加信息。jstack工具会在控制台输出程序中所有的锁信息，可以使用重定向将输出保存到文件中，如：  
```
[root@xxx]# jstack -l 57068 >/tmp/threadStack.txt

```
通过jstack工具不仅可以得到线程堆栈，它还能自动进行死锁检查，输出找到的死锁信息。  

##### jstatd命令  







[001]:./vmstat命令输出的含义.png 'vmstat命令输出的含义'
[002]:./JDK部分辅助工具.png 'JDK部分辅助工具'
[003]:./toolsjar中的工具类.png 'tools jar中的工具类'