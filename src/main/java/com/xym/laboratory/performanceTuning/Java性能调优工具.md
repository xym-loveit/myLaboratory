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











[001]:./vmstat命令输出的含义.png 'vmstat命令输出的含义'