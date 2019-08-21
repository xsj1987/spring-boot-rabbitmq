### 关于生产者消息确认机制
1. 确认消息已推送到exchange，以属性ack为准，true表示到达
    * 实现RabbitTemplate.ConfirmCallback接口
    * 设置spring.rabbitmq.publisher-confirms=true
2. 确认消息已推送到queue，如果消息没有正确到达时触发回调
    * 实现RabbitTemplate.ReturnCallback接口
    * 设置spring.rabbitmq.publisher-returns=true
    
### 死信队列
