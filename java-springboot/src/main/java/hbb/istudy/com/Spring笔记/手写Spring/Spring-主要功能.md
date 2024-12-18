# Spring 六个版本


IoC、DI、MVC、AOP、JDBC...

## V1：实现IoC、DI、MVC
简单的实现Spring
1. 配置阶段
2. 初始化阶段
3. 运行阶段
4. 
### 课后小测

1. Spring为什么轻量级？
三个维度：
侵入性越小越轻
需要的资源越少越轻
开发越便捷越轻

2. Spring最核心的模块：IoC

3. Spring中单例意味者每个**Context**只有一个实例


## V2：用30个类实现IoC
工厂如何把对象创建出来，交给用户（工厂模式、原型、单例）
1. 容器Map
2. 上下文context
3. 工厂 factory
4. bean
5. BeanDefinition元信息、配置文件（xml、yml、properties...）
6. Reader解析器
## V3：用30个类实现DI
怎么给对象自动赋值、解决循环依赖注入（组合复用原则）

## V4：用30个类实现MVC
用户输入URL如何与Spring关联，MVC的九大组件（委派、策略）

## V5：用30个类实现AOP
面向切面设计、解耦、通知回调（责任链、动态代理）

## V6：用30个类实现JDBC
基于Spring JDBC 实现一个ORM框架（模板方法模式、建造者模式）

看源码的方法：
1. 先猜测后验证


