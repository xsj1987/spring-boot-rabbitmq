
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
3. 客户端确认消息
    * 设置spring.rabbitmq.listener.simple.acknowledge-mode=manual
    * 在监听方法中，如下设置
```java
@RabbitListener(queuesToDeclare = @Queue(Const.TOPIC_QUEUE_FOUR))
public void topicListenerFour(Message message, Channel channel) throws IOException{
    long tag = message.getMessageProperties().getDeliveryTag();
    // 手动确认消息
    channel.basicAck(tag, true);
}
```
### 关于Ack机制和Nack机制
1. 自动Ack机制导致消息丢失的问题
    在自动ACK机制下，无须等待应答就会丢弃消息，这会导致客户端还未处理完时，出异常或断电导致消息丢失。
    另外，在该机制下，RabbitMQ会不断的推送消息，而不管客户端能否消费完
2. 手动ACK机制下的未及时ACK导致的队列异常
    如果选择手动ACK，在客户端处理消息的过程中，如果处理完成才ACK，如果异常就不做ACK响应，那么消息就会储存在Unacked中，如果这样的消息
    多了，那么占用的内存会越来越大，就会异常。处理的方式就是不管什么情况都进行ACK操作
3. 启用Nack导致的死循环
    在这样一个场景下，客户端处理消息，正常就ack，异常就nack，并等下一次重新消费。这样的场景下会导致Ready消息猛增，一直不减少。
    原因就是出现异常后，消息塞回队列头部->异常->塞回头部 进入死循环
### 关于队列过期时间的设置
```exception
inequivalent arg 'x-message-ttl' for queue 'topic.queue.one' in vhost '/'
解决方案：删除之前的队列，重新创建
```
#### Channel说明
* channel.basicReject(deliveryTag, true);
    拒绝deliveryTag对应的消息，第二个参数表示是否重新进入队列[true-是,false-丢弃或者进入死信队列]
* channel.basicNack(deliveryTag, false, true);
    不确认deliveryTag对应的消息，第二个参数表示是否应用于多消息，第三个参数表示是否重新进入队列[true-是,false-丢弃或者进入死信队列]
* channel.basicRecover(true);
    是否恢复消息到队列，参数表示是否重新进入队列，true-将之前recover的消息投递给其他消费者消费，而不是自己再次消费，false-消息会重新被投递给自己
### Rabbit Config配置
```properties
# base
spring.rabbitmq.host: 服务Host
spring.rabbitmq.port: 服务端口
spring.rabbitmq.username: 登陆用户名
spring.rabbitmq.password: 登陆密码
spring.rabbitmq.virtual-host: 连接到rabbitMQ的vhost
spring.rabbitmq.addresses: 指定client连接到的server的地址，多个以逗号分隔(优先取addresses，然后再取host)
spring.rabbitmq.requested-heartbeat: 指定心跳超时，单位秒，0为不指定；默认60s
spring.rabbitmq.publisher-confirms: 是否启用【发布确认】
spring.rabbitmq.publisher-returns: 是否启用【发布返回】
spring.rabbitmq.connection-timeout: 连接超时，单位毫秒，0表示无穷大，不超时
spring.rabbitmq.parsed-addresses:

# ssl
spring.rabbitmq.ssl.enabled: 是否支持ssl
spring.rabbitmq.ssl.key-store: 指定持有SSL certificate的key store的路径
spring.rabbitmq.ssl.key-store-password: 指定访问key store的密码
spring.rabbitmq.ssl.trust-store: 指定持有SSL certificates的Trust store
spring.rabbitmq.ssl.trust-store-password: 指定访问trust store的密码
spring.rabbitmq.ssl.algorithm: ssl使用的算法，例如，TLSv1.1

# cache
spring.rabbitmq.cache.channel.size: 缓存中保持的channel数量
spring.rabbitmq.cache.channel.checkout-timeout: 当缓存数量被设置时，从缓存中获取一个channel的超时时间，单位毫秒；如果为0，则总是创建一个新channel
spring.rabbitmq.cache.connection.size: 缓存的连接数，只有是CONNECTION模式时生效
spring.rabbitmq.cache.connection.mode: 连接工厂缓存模式：CHANNEL 和 CONNECTION

# listener
spring.rabbitmq.listener.simple.auto-startup: 是否启动时自动启动容器
spring.rabbitmq.listener.simple.acknowledge-mode: 表示消息确认方式，其有三种配置方式，分别是none、manual和auto；默认auto
spring.rabbitmq.listener.simple.concurrency: 最小的消费者数量
spring.rabbitmq.listener.simple.max-concurrency: 最大的消费者数量
spring.rabbitmq.listener.simple.prefetch: 指定一个请求能处理多少个消息，如果有事务的话，必须大于等于transaction数量.
spring.rabbitmq.listener.simple.transaction-size: 指定一个事务处理的消息数量，最好是小于等于prefetch的数量.
spring.rabbitmq.listener.simple.default-requeue-rejected: 决定被拒绝的消息是否重新入队；默认是true（与参数acknowledge-mode有关系）
spring.rabbitmq.listener.simple.idle-event-interval: 多少长时间发布空闲容器时间，单位毫秒

spring.rabbitmq.listener.simple.retry.enabled: 监听重试是否可用
spring.rabbitmq.listener.simple.retry.max-attempts: 最大重试次数
spring.rabbitmq.listener.simple.retry.initial-interval: 第一次和第二次尝试发布或传递消息之间的间隔
spring.rabbitmq.listener.simple.retry.multiplier: 应用于上一重试间隔的乘数
spring.rabbitmq.listener.simple.retry.max-interval: 最大重试时间间隔
spring.rabbitmq.listener.simple.retry.stateless: 重试是有状态or无状态

# template
spring.rabbitmq.template.mandatory: 启用强制信息；默认false
spring.rabbitmq.template.receive-timeout: receive() 操作的超时时间
spring.rabbitmq.template.reply-timeout: sendAndReceive() 操作的超时时间
spring.rabbitmq.template.retry.enabled: 发送重试是否可用
spring.rabbitmq.template.retry.max-attempts: 最大重试次数
spring.rabbitmq.template.retry.initial-interval: 第一次和第二次尝试发布或传递消息之间的间隔
spring.rabbitmq.template.retry.multiplier: 应用于上一重试间隔的乘数
spring.rabbitmq.template.retry.max-interval: 最大重试时间间隔
```
### 死信队列
如果配置了死信队列，那么在三种情况下消息会被推送到死信队列
1. 消息被拒绝（basic.reject/ basic.nack）并且不再重新投递 requeue=false
2. 消息超期 (rabbitmq Time-To-Live -> messageProperties.setExpiration())
3. 队列超载

关于死信队列的配置
在需要配置死信队列的业务队列中，新增参数
```java
Map<String, Object> params = new HashMap<>();
// 声明当前队列绑定的死信交换机
params.put("x-dead-letter-exchange", Const.DLX_EXCHANGE);
// 声明当前队列的死信路由
params.put("x-dead-letter-routing-key", Const.DLX_ROUTE_KEY);
```
正常声明死信队列的交换机、队列和绑定，这样一旦出现需要推送到死信队列的情况，信息就会被推送到死信队列，详情参考代码RouteConfig.java
### 关于ACK
发送者确认模式和消费者没有一点关系，消费者确认也和发送者没有一点关系，两者都是在和RabbitMq打交道，发送者不会管消费者有没有收到，只要消息到了RabbitMq并且已经持久化便会通知生产者，这个ack是RabbitMq本身发出的，和消费者无关。\
ack分两种
1. 发送者ack确认
2. 消费者ack确认

参考文档：\n
Spring Boot + RabbitMQ 配置参数解释：https://www.cnblogs.com/qts-hope/p/11242559.html