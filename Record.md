
### 概念
1. 交换机：用来接收生产者生产的消息，并将消息转发到绑定的队列。目前RabbitMQ有4种交换机类型
    * 主题交换机(Topic)：一种带有特殊路由键的交换机，支持模糊匹配
        * \*可以匹配一个标识符
        * \#可以匹配0个或者多个标识符
    * 直连交换机(Direct)：根据路由键全匹配的交换机
    * 扇形交换机(Fanout)：发送到所有绑定的队列，与路由键无关
    * 头部交换机(Header)：根据消息的basicProperties对象中的headers来匹配，队列通过设置x-match来指定匹配规则
        * any：满足任一属性
        * all：满足所有属性
2. 队列：存储消息及推送消息
### 关于生产者消息确认机制
1. 确认消息已推送到exchange，以属性ack为准，true表示到达
    * 实现RabbitTemplate.ConfirmCallback接口
    * 设置spring.rabbitmq.publisher-confirms=true
2. 确认消息已推送到queue，如果消息没有正确到达时触发回调
    * 实现RabbitTemplate.ReturnCallback接口
    * 设置spring.rabbitmq.publisher-returns=true
    
### 死信队列
### 关于ACK
发送者确认模式和消费者没有一点关系，消费者确认也和发送者没有一点关系，两者都是在和RabbitMq打交道，发送者不会管消费者有没有收到，只要消息到了RabbitMq并且已经持久化便会通知生产者，这个ack是RabbitMq本身发出的，和消费者无关。\
ack分两种
1. 发送者ack确认
2. 消费者ack确认