# 从Kylin入门到大数据老司机



一、UML基础
=======
1. 为什么要使用UML
-
对象是面向对象世界的核心。面向对象软件分析和设计，最基本的需求是高效的**识别对象**,完成对象识别之后，**赋予每个对象相应的功能或职责**。在完成以上基本分析工作之后，只需要完成代码设计即可。
作为一种可视化的建模语言，UML能够描述软件系统的需求说明和系统架构，并做到可视化和文档化。

 <font color=#0099ff size=4 face="黑体">------>反思<------</font>

如何看开源软件源码？
>  **整体与局部法，化整为零法** :整体与局部，可以先了解开源软件的功能，一般会提供前端页面，将所有功能试用一遍，画一个功能列表。再了解一下如何安装部署，以及部署的目录结构。还有就是在部署时，分布式部署和单机部署的区别，参数如何配置等等。最后在从git上下载源码，整体先浏览每一个包的功能，为了解耦和结构清晰，一个包会做一件事。最后再从功能出发，了解功能是如何通过面向对象实现的。就是通过识别对象，以及对象的功能和职责，还有就是对象之间的关系。对象的组织方式等等。

有几个问题：

1. 如何实现启动的？这个问题可以从系统生命周期开始，顺序推出整个系统生命周期过程中对象的活动和变化
2. 如何实现关键问题的？比如网络通信、并发控制、缓存同步、任务调度、配置文件的参数读取和管理、分布式服务调用等等
3. 关键模块的实现细节，如何表达对象的功能，使用的什么数据结构，为什么要用这样的数据结构等等。

<font color=#0099ff size=4 face="黑体">------>end<------</font>


2. 面向对象基础
-
### 2.1 一些基本概念
 **对象**

>对象通常包含了数据和操作数据的方法(又称为函数).数据表示对象的状态。这样我们使用一个**类**来描述特定的对象，并形成层次结构来模拟真是世界的系统。这种层次结构表示为继承，或者说是不同需要扩展出不同行为方式。


现实世界的对象有两个共通的特点：他们都具有状态和行为(功能)。识别真实世界中对象的状态和行为是开始面向对象编程思想的起点。

**类**
>在现实世界中，我们通常会发现许多不同个体对象却具有相同的特性。有可能存在上万款自行车，但他们却具有相同的传动结构和零部件。每个自行车都像是以同一张蓝图为基准设计的，并且复用了相同的组件。如果用面向对象的语言描述，自行车对象能够被描述为一种自行车类，而我们骑的每辆自行车都是这种自行车的一个实例。类就是我们为了创建各个实体对象所使用的蓝图。

1. 类是一种样式，模板，或者说是为了构建一种具备相同基础结构的物体(对象)所使用蓝图
2. 我们使用嘞创建物体也称为实例化(或者说创建对象)
3. 类本身包含了创建物体(对象)所需要的组成结构和基于这个结构的行为功能
4. 类也可以被逻辑为包含特定的注册结构的所有对象的集合

**抽象**
> 我们可能会认为嘞总是需要将事物的属性定义完整。然后不可能。实际情况是，类的定义只需要**恰如其分**即可。例如在定义汽车这个对象时，不可能将它的所有属性定义清楚。

**封装**
> 封装是绑定数据和操作的一种机制，并使之不可见。也就是隐藏说绑定的数据结构和代码访问数据的过程(操作)。封装所代表的是一种存储数据和数据相关函数的容器。当一个对象的状态和行为以适当的方式组合在一起即为封装。

**特殊化与继承**
> 不同种类的对象往往具有一定的共同点。例如山地自行车和公路自行车都具有所有自行车的特点(速度、档位)。然而不同自行车还具有不同功能。公路自行车可以有两个作为，山地自行车只有一个座位，却会多一些档位。

1. 在Java编程语言中，每一个允许有一个父类，而每个父类都可以泛化或派生出多个子类
2. 在面向对象语言中，我么讲的特殊化(实例化)指的是对象泛化(派生)过程中**对特征的继承关系**
3. 继承就是一个对象(类)自一个或多个对象获得(获取、接收)特征的过程
4. 一些面向对象只允许**单一基础，如Java**，有些允许**多重继承**，就是说，特殊化能从两个或多个相应的泛化过程中获取特征

**多态性**
> 多态性定义了**一种能够具有多种形式的机制**。 有两种类型的多条性:**编译时(也就是早期绑定）**和 **运行时(也就是后期绑定)**

+ 编译时多态
  1. 对象在编译时知道要实现多态。函数重载是在编译时实现的多态
  2. 编写重载方法时应使用相同的名称和不同的参数。


如有一个方法sum()，分别要计算两个数相加和三个数相加。

```java
sum(int a,int b)
sum(int a,int b,int c)
```
这个sum函数就可以理解为一种编译时多态，一种方法的不同实现。

+ 运行时多态
	1. 在运行时多态中，对象本身并不知道在编译时实现多态性，二十在运行时实现相应的属性和方法。
	2. 多态性是通过对父类函数重写或者基于继承的多态性来实现的。
	
	
例如：如果有一个表示形状的类Shape,派生出三个子类三角形，圆和正方形，Shape类有一个方法Draw,这样它就会被所有子类继承，如果我们什么一个Shape类，并将起初始化为任意一个子类，那么这会调用子类中所集成的方法

**在Java中如果多个类实现同一个接口，可以创建类的实例，但是返回接口类型，这个实例只有接口的方法。这是Java接口多态**
	

关于继承和多态，还有几个需要思考的问题：

1. 声明一个父类，并将其初始化为一个子类的实例，那么这个对象能访问的成员是父类的还是子类的，还是子类实现父类的？
2. 声明一个接口，并将其初始化为一个实现类的实例，那么这个对象能访问的成员是接口的还是接口实现类的，还是接口实现类实现这个接口的方法？

<font color=#0099ff size=4 face="黑体">------>反思<------</font>

如何看待面向对象？
>面向对象是一种编程思维。我要解决一个小问题，比如求一个数组的最大值。求最优解。我要解决如何数组最大值？我要解决数组这个数学问题？我要开发一个计算器，能记忆每次输入？面对的问题不一样，问题的复杂度不一样。当只是一个求数组最大值的问题时，可以告诉计算机怎么算，这只是一个算法问题。当求数组最大值时，这个问题就变成抽象问题，当然，可以通过定义函数，输入数组，这是算法和结构化编程的问题。但是如何开发一个数学工具，或这计算器，或者是一个人机交互系统，涉及的问题复杂度很高时，那么面向对象是一个方法论，这个方法论的核心思想是将自然界中存在的事物识别为对象，在识别的过程要做两件事，第一，要识别对象作为个体具有的特点，有什么，能干什么，区别其他事物的特质是什么？第二，是要抽象，所谓抽象，就是比较这个对象和其他对象的区别。区别可以是共同点，也可以是不同点，共同点可以用于向上抽象，就提炼出来一个能表示这个对象的模板，或者这个对象类似对象的的模板(父类)，不同点，可以用于实例化，也就是说在设计模板是要考虑到的特殊情况。最后再逐渐细化和完善，就能找出解决问题的方法。这种方法论，其实就是按照人的思维模式，归纳和演绎方法去做软件开发和设计。

面向对象的概念很好理解，当什么软件开发还是那么难？
>
对象，类，抽象，继承等等概念都很好理解。当为什么软件开发还是那么难？这是因为，解决待解决的问题是一个问题域。问题域的解决更多是一个知识网的结构。

比如开源 OLAP 的解决方案 Kylin 解决的是使用预计算 Cube 实现实时数据分析。这里涉及到的问题，如果从浅入深看 

+ 第一层: 数据输入，处理，输出，用户交互
+ 第二层: 如何从Hive 读取数据？如何实现 Cube 计算， 如何向用户暴露交互接口(SQL)支持
+ 第三层: hive元数据拉取、缓存、缓存同步，MR、Spark任务、任务接口、任务管理，Calcite定制化实现，Web API

所以，软件开发难度在于每个要解决的问题都是一个问题域。这个问题域只能靠着不断的学习和知识的累计才能去解决。

>
好的地方是：1. 单个问题领域是有限的。2.不同的领域的问题是相通的。也就是你要解决一个问题，那么这个问题的肯定是有边界的，如果没有边界，也就意味这问题无解(时间不等人，不要在无解的问题上花时间)，如果解决了很多问题之后就会发现，再去解决另外一个问题域，其实用到的知识域是相似的，看透了Kylin,再看Spark,可能就是今天见了张三，明天去认识李四一样。

<font color=#0099ff size=4 face="黑体">------>end<------</font>

### 2.2 面向对象的分析与设计
> 面向对象的**分析** 是研究对象的过程。**设计**则意味着运用说识别的对象。面向对象的分析中最重要的目标是识别待设计系统中的对象。

**面向对象的分析与设计目标**
1. 确定目标系统中的对象
2. 确定对象之间的关系
3. 使用面向对象语言完成软件设计
面向对象思想的应用和实施主要有三个步骤:

面向对象的分析 --> 面向对象的设计 --> 通过面向对象的语言实现面向对象

上面三个步骤可以分为三个阶段：
- 第一阶段，最重要的目标是确定对象并以适当的方式描述对象。识别对象的过程应同时是吧出对象的用途。用途对应的是对象中的可执行函数。每个对象都有特定的用途。所有这些用途的结合即为系统可实现的所有功能
- 第二阶段，面向对象的设计。根据对象之间的内在联想将其组织起来，在完成相关逻辑组织构建之后，这一阶段的设计就完成了
- 第三阶段，代码实现。


### UML构造块和符号

> UML 符号是建模最重要的元素。如果对模型的描述是错误的，那么对于软件设计来说建模的结果。。。(方向错了，停下来就是进步)

在我们使用UML描述真实系统时，首先建立基本的概念模型，然后逐渐深入分析和处理，UML概念，有三大块
- UML 事物(对象)
- 构造块链接规则 (关系)
- UML 的基本机制 (视图)

**事物**

UML有四种事物

* 结构事物
* 行为事物
* 分组事物
* 注释事物

**结构事物**

定义了模型中的静态部分，表示物理化或概念化的元素。UML结构事物中符号是最常用的

> 
	- 类
	- 接口
	- 协作
	- 用例
	- 主动类
	- 组件
	- 节点


都有哪些结构事物符号，以及含义？

类符号 - 对象符号 - 接口符号 - 协作符号 - 用例符号 - 参与者符号 - 初始状态符号 - 最终状态符号 - 主动类符号 - 主键符号  - 节点符号

**行为事物**
交互符号 - 状态机符号

**分组符号**
包符号

**注释事物**
注释符号


**关系**
> **关系** 显示了元素之间是如何相互关联的，这种元素之间的联系也反映出软件的整体功能。

1. 模型只有在描述清楚元素之间的关系之后，才是完整的(事物之间的复杂性不仅在于事物本身的特性，还在于事物与其他事物之间的关系)
2. 在UML中关系主要有 **依赖、关联、泛化、扩展**


**依赖**

1. 依赖关系定义了**不同对象之间相互影响的关系**，描述了依赖的对象和依赖关系的方向。
2. 箭头指向的一端代表独立元素，另一端表示依赖元素。依赖关系表示了系统两个元素之间的依赖关系。
3. 依赖关系描述的主要内容是**当前元素之中一个发生变化，另外一个会相应地发生某些变化**
4. 依赖关系用虚箭头表示 ---->
5. 依赖关系是一对一的

**关联**
1. 关联关系描述了UML图中元素的管理方式。简单地说，它描述了多少个元素正在参与运算。
2. 描述类之间关系醉抽象的方式是使用关联关系表示
3. 关联关系体现了一个实例向另一个实例发送消息的能力。通常由一个指针或应用实例变量实现。（也可以作为方法参数或者一个局部变量）
4. 关联关系在类之间通过一个只是关联方向的箭头表示。如果线段两段都是箭头，这表示双向关联关系。同时能够在线上精选编号(1,2,*)来展示多个对象相互关联。
5. 关联关系意味着相互调用

>关联关系和依赖关系的区别
一般来说，当我们将某个类的引用作为当前类特定的一部分特定操作或函数一部分是，通常会使用依赖关系。需要意识的是，依赖关系强调，在引用类中引用这个类的API，一旦发生修改都会破坏这个类。



**泛化**
泛化就是继承关系。使用空心箭头表示。箭头指向父类。


**组合和聚合关系**

组合关系是一种特殊的关联关系类型，它代表了一种强烈的从属关系。使用黑心实体菱形来表示组合关系。组合关系表示类之间的包含关系。

聚合关系中，整体类要比部分类其更重要的作用，但这个两个类不是互相依存的。使用空心菱形表示聚合关系。

组合聚合关系实例：
```java
// 组合关系
//WebServer 类是HttpListener 和RequestProcessor 类的组合
// 在组合类中使用了新的操作符，从而控制了部分类的生命周期
public class WebServer{
	private HttpListener listener;
	private RequestProcessor processor;
	
	public WebServer(){
		this.listener = new HttpListener();
		this.processor = new RequestProcessor;
	}
}
```
在上面的例子中，如果删除WebServer实例，这另外两个实例也不存在，因为，是通过狗仔函数创建出来的。这两个实例是WebServer的

```java
// 聚合关系
//WebServer 类是HttpListener 和RequestProcessor 类的聚合
// 通过参数来设定调用对象
public class WebServer{
	private HttpListener listener;
	private RequestProcessor processor;
	
	public WebServer(HttpListener listener,RequestProcessor processor){
		this.listener = listener;
		this.processor = new processor;
	}
}
```
如上，如果删除WebServer,另外两个实例并不会被删除，因为WebServer是通过构造函数传参创建的。


一个人的心脏，四肢，大脑是组合关系，一个人有车有房是聚合关系。

- 聚合关系是弱连接，对象之间能够彼此独立创建
- 组合关系是强连接，部分对象在其他对象中创建
- 在聚合关系中对象可以彼此独立存在

<font color=#0099ff size=4 face="黑体">------>反思<------</font>

**关于关系**
面向对象系统把自然界中的事物都看做对象。一个复杂系统在运行时，肯定由成百上千个，万个对象。这些对象是什么关系？

对象A和对象B。可能是泛化（或者说继承）关系。B是A的子类，A的所有特性，B都可以具有。
除了泛化。对象A里面，可能还有对象C，对象D，E，F...那么这是什么关系呢？一般来说，如果两个对象，如果A引用了B的某个API，B的变化必定引起A的变化，那么A，B是依赖关系。如果A与B，或B，C，只是大家一起运算，那么就是关联关系。
如果从归属上看，对象A创建的时候，去创建了B，C，那么A，B，C是组合关系
如果只是对象A创建时，传入了B，C对象，则是聚合关系。


泛化(实心箭头)，依赖(虚箭头)，关联(可双向箭头),组合(实心菱形)，聚合（空心菱形）

这些关系定义了，两个类之间(泛化，依赖)，多个多个类之间的相互依存方式。或者从特质三，或者从声明周期上，或者从组织方式上表明了对象之间的关系。


<font color=#0099ff size=4 face="黑体">------>end<------</font>


**图**

UML中有两种类型图：

1. 静态结构图
	- 类图
	- 对象图
	- 组件图
	- 部署图
2. 动态行为图
	- 用例图
	- 顺序图
	- 协作图
	- 状态图
	- 活动图
	
用例图：主要通过参与住和用例来形象化的展示系统的功能。用例是系统向用户提供的服务和功能。用例图一般包含一组用例、参与者及其相互关系。

顺序图：描述一组组件之间消息交互的过程。注重体校消息交互过程中消息的顺序，以及消息在对应的对象的生命线上触发的原因。

**注意：顺序图描述的是对象之间的消息传递的顺序，以及对象的声明周期之间关系
**	
	
状态图：定义对象在其声明周期过程中的不同状态。而这些状态变化是由事件触发的。

活动图描述了系统的空值流程。

