# Spark Core 


## 问题
1. Spark的部署模式有单机版，Standalone/yarn/mesos/k8s这几种，请问用户的APP如何提交到Spark执行？


2. SparkContext 的组件有哪些？SparkContext是如何初始化的，SparkContext 提供了哪些功能？



3. SparkContext 各个主键是什么关系，具有什么功能，是如何初始化的？


4. 如何使用SparkContext 进行运算




# SparkContext组件概述

## SparkContext的功能
![](/Users/hhl/Documents/sparkCore.jpg)

```SCALA
/**
 * Main entry point for Spark functionality. A SparkContext represents the connection to a Spark
 * cluster, and can be used to create RDDs, accumulators and broadcast variables on that cluster.
 *
 * Only one SparkContext may be active per JVM.  You must `stop()` the active SparkContext before
 * creating a new one.  This limitation may eventually be removed; see SPARK-2243 for more details.
 *
 * @param config a Spark Config object describing the application configuration. Any settings in
 *   this config overrides the default configs as well as system properties.
 */
```

SparkContext是整个Spark功能的入口，在2.0以后，在Sql包中封装了SparkSession,并通过DataFrame向外提供API.
SparkContext 表示了与Spark集群的连接。不论这个集群是Local/yarn/mesos/k8s，所以与集群连接、管理的细节都在SparkContext中封装。用户APP，只需要创建SparkContext,通过SparkConf告诉sc 计算在哪儿执行，然后创建rdd,编写业务计算逻辑，就可以使用Spark提供的分布式弹性数据集的特性，同时，还有一站式开发，基于RDD的图计算，机器学习算法，SQL查询等。

通过SparkContext 可以

- 创建 RDD ,并执行计算
- 累积并在集群上广播变量
- 提交并运行Job（基于RDD）

SparkContext 有几个模块

- SparkEnv : 负责构建Spark执行的网络环境，内存管理，磁盘管理，网络通信安全管理等等
- 日志和监控 : 负责Spark任务执行的日志和监控
- 调度器: 负责在SparkContext通过runJob接收任务之后，将待执行的Job按照Dag 拆分成不同Stage，再根据Stage划分为不同的Task，再分配刀不同的执行器上执行，同时在执行过程中监控执行器的状态，以达到容错的目的。调度器主要有三块，后台调度器（不同调度模式下后台调度器有不同实现），主要负责在不同集群下申请资源、管理资源。Dag调度器，主要负责在接受到任务之后，将任务按照Dag 模式进行划分为Task. Task调度管理器，主要负责将任务集分配到集群中不同的执行器上执行。
- 用户交互的API， 主要有RDD创建，runJob,广播等功能


## SparkContext 启动

![](/Users/hhl/Documents/sparkCore11.jpg)


Step 1. 复制SparkConf，初始化、验证参数

Step 2. 初始化 LiveListenerBus和AppStatusStore(在SparkEnv之前，因为要监控SparkEnv的创建)

Step 3. 初始化 SparkEnv.

Step 4. 初始化 SparkStatusTracker processBar 和 SparkUI 用于监控Spark运行 

Step 5. 获取 HadoopConfiguration

Stpe 6. 添加jar 包 和文件

Step 7. 配置执行器参数 executorEnv 包含 executor 内存和其他参数

Step 8. 注册心跳接收器 heartbeatReceiever （因为之后调度器要向起注册，通过SparkEnv中的RpcEnv的setUpEndPoint 设置）

Step 9. 创建任务调度器（同时返回了后台调度器和任务调度器，因为不同部署模式创建方式不一样）,创建DAG调度器
		- 创建任务调度器，返回后台调度器和任务调度器
		- 创建Dag调度器
		- 心跳接收器发送任务调度器创建事件
		- 启动任务调度器
		- 任务调度器返回应用唯一ID
		- 任务调度器返回任务尝试ID - applicationAttemptId
		- 向其他组件(SparkConf,SparkEnv.ui,SparkEnv.blockManager) 传入这个应用ID
		
Step 10. 启动度量系统，SparkEnv.MertricsSystem，获取度量系统的 getServletHandlers

Step 11. 初始化 EventLoggingListener,启动Logger,向LiveListenerBus 队列中添加这个logger

Step 12. [可选] 初始化动态executor 管理器

Step 13. 初始化 ContextCleaner,并启动

Step 14. setupAndStartListenerBus-> postApplicationStart -> postApplicationStart 

Step 15. Post init
		- 任务调度器postStartHook
		- 度量系统注册源，dagScheduler 和BlockMananger和Executor 管理器



**SparkContext组件结构**

![](/Users/hhl/Documents/sparkcore2.jpg)


# SparkContext 核心组件

## SparkEnv


```
/**
 * :: DeveloperApi ::
 * Holds all the runtime environment objects for a running Spark instance (either master or worker),
 * including the serializer, RpcEnv, block manager, map output tracker, etc. Currently
 * Spark code finds the SparkEnv through a global variable, so all the threads can access the same
 * SparkEnv. It can be accessed by SparkEnv.get (e.g. after creating a SparkContext).
 *
 * NOTE: This is not intended for external use. This is exposed for Shark and may be made private
 *       in a future release.
 */
```
SparkEnv 封装了Spark运行时的环境。作为分布式计算引擎，运行时环境应该包括：
	- 网络管理和网络通信
	- 存储管理（包括磁盘管理、内存管理）
	- 度量模块
对于Spark来说，通过对阻塞IO大规模并发网络通信框架Netty将网络和网络通信功能封装成RpcEnv,将存储管理根据访问对象不同，分为BlockManager, MemoryStoreMananger,ShuffleManager.广播管理器，安全管理器，序列化管理器等等。




**组件列表和功能**

按照功能分成三类:

- 支持网络通信的: RpcEnv,SecurityManger
- 支持存储管理的:  BlockManger, MemoryManger,ShuflleManager
- 支持序列化的: Serializer,ClosureSerizer,SerializerManager
- 支持输入输出的: MapOutPutTracker,OutPutCommitCoordinator

RpcEnv作为网络通信的核心模块，一方面支持Shufflemanger这种跨节点的数据管理器，还支持Driver 和 executor 之间连接的监控(通过心跳机制)

**SparkEnv的初始化**

 -> sparkContext.createSparkEnv: 传入SparkConf, is_local,LiveListenerBus
 
 -> SparkEnv.createDriverEnv:传入SparkConf,is_local, LiveListenerBus,用户配置的Driver核数
 
 -> SparkEnv.Create:传入SparkConf,driverid,driver绑定地址，port等等
 
 	1. 判断是否是Driver
 	2. 创建SecurityManager,如果是Driver，执行initializeAuth
 	3. 实现IO加密ioEncryptionKey
 	4. 获取systemName(根据是driver调用还是Executor调用)
 	5. 初始化RpcEnv（Driver和Executor实现方式一样）
 	6. 如果是Driver,修改conf的port为用户设置的端口
 	7. 反射的方式创建序列化对象，默认是JavaSerializer
 	8. 创建序列化管理器
 	9. 创建闭包序列化，实际是new一个JavaSerializer
 	10. 初始化广播管理器
 	11. 初始化mapOutputTracker，Driver端创建MapOutputTrackerMaster，Executor端创建MapOutputTrackerWorker。Driver端创建时需要传入广播管理器
 	12. mapOutputTracker 设置Master的Endpoint
 	13. 反射的方式创建ShuffleManger,默认是sort，用户通过spark.shuffle.manager设置(已经不起作用，不论用户是否设置，调用的都是org.apache.spark.shuffle.sort.SortShuffleManager)
 	14. 初始内存管理器。获取useLegacyMemoryManager，默认是False,如果用户配置为真，使用StaticMemoryManager，否则使用UnifiedMemoryManager
 	15. 创建blockManager,先获取blockManagerPort，再通过NettyBlockTransferService创建blockTransferService服务，再创建BlockManagerMaster，最后初始化blockManager，注意只有initialize 之后才可以使用。
 	16. 创建度量系统，并启动
 	17. 创建OutputCommitCoordinator
 	18. 返回SparkEnv实例
 




### 1. RpcEnv和 SecurityManger

```scala
/**
 * An RPC environment. [[RpcEndpoint]]s need to register itself with a name to [[RpcEnv]] to
 * receives messages. Then [[RpcEnv]] will process messages sent from [[RpcEndpointRef]] or remote
 * nodes, and deliver them to corresponding [[RpcEndpoint]]s. For uncaught exceptions caught by
 * [[RpcEnv]], [[RpcEnv]] will use [[RpcCallContext.sendFailure]] to send exceptions back to the
 * sender, or logging them if no such sender or `NotSerializableException`.
 *
 * [[RpcEnv]] also provides some methods to retrieve [[RpcEndpointRef]]s given name or uri.
 */
 /**
 * A RpcEnv implementation must have a [[RpcEnvFactory]] implementation with an empty constructor
 * so that it can be created via Reflection.
 */
```
RpcEnv是一个远程调用环境，Spark的执行是Driver和Executor 模式，也就是一个Driver负责管理所有的Executor.

RpcEnv 的创建也分Driver和 Executor 两种情况。RpcEnv 能做几件事
	- setupEndpoint
	- setupEndpointRef:Ref为远程服务器的引用
	
	一旦 给RpcEnv设置了RpcEndPoint，不论是rpcEndpoint还是rpcEndpointRef,都会返回rpcEndpointRef.
	RpcEndpointRef具有的功能是：
	- ask: 向协同的服务发送一个消息，并返回一个消息
	- send:发送一个消息，不接受返回消息
	- askSync
	- sendSync

	通过消息的发送和处理就可以实现Rpc通信
	
需要注意的是rpcEnv吃的创建过程，对于Driver和Executor 都是通过RpcEnv.create 方法创建，实际调用的是：
```scala
def create(
      name: String,
      bindAddress: String,
      advertiseAddress: String,
      port: Int,
      conf: SparkConf,
      securityManager: SecurityManager,
      numUsableCores: Int,
      clientMode: Boolean): RpcEnv = {
    val config = RpcEnvConfig(conf, name, bindAddress, advertiseAddress, port, securityManager,
      numUsableCores, clientMode)
    new NettyRpcEnvFactory().create(config)
```

NettyRpcEnvFactory是一个工厂类，只有一个create方法，返回RpcEnv对象

在NettyRpcEnvFactory的创建方法中实现Driver和Executor 不同车创建方式

1. 创建NettyRpcEnv 对象
2. 如果是executor，直接返回NettyRpcEnv 对象，如果不是，依次在指定端口启动服务startServer。


而 NettyRpcEnv 的创建就是典型 NettyRpc的创建步骤

- 初始化 Dispatcher
- 创建启动引导
- 创建服务
- registerRpcEndpoint



### 2. 存储管理

### 3. 序列化工具

### 



**组件在SparkContext 中功能**


		
		

	

