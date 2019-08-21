package com.xpz.springbootrabbitmqsend.conf;

import com.xpz.common.Const;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Configuration
@Slf4j
public class RabbitConfig implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }

    /**
     * 主题模式，适用于发布/订阅模式
     * 采用模糊匹配
     *      * 匹配一个单词
     *      # 匹配0个或者多个单词
     */
    @Bean
    public Queue topicQueueOne(){
        /**
         * Queue有四个参数
         *      1.name-队列名
         *      2.durable-持久化消息队列，默认为true，表示rabbit重启的时候不需要创建新的队列
         *      3.auto-delete-表示消息队列没有在使用时将被自动删除，默认为false
         *      4.exclusive-表示该消息队列是否只在当前connection生效，默认为false
         */
        return new Queue(Const.TOPIC_QUEUE_ONE);
    }
    @Bean
    public Queue topicQueueTwo(){
        return new Queue(Const.TOPIC_QUEUE_TWO);
    }
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(Const.TOPIC_CHANGE);
    }
    @Bean
    public Binding topicBindOne(){
        return BindingBuilder.bind(topicQueueOne()).to(topicExchange()).with("topic.one.message");
    }
    @Bean
    public Binding topicBindTwo(){
        return BindingBuilder.bind(topicQueueTwo()).to(topicExchange()).with("topic.two.message");
    }

    /**
     * 扇形模式
     * Fanout交换机发送消息，绑定了这个交换机的所有队列都收到这个消息
     */
    @Bean
    public Queue fanoutQueueOne(){
        return new Queue(Const.FANOUT_QUEUE_ONE);
    }
    @Bean
    public Queue fanoutQueueTwo(){
        return new Queue(Const.FANOUT_QUEUE_TWO);
    }
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(Const.FANOUT_CHANGE);
    }
    @Bean
    public Binding fanoutBindOne(){
        return BindingBuilder.bind(fanoutQueueOne()).to(fanoutExchange());
    }
    @Bean
    public Binding fanoutBindTwo(){
        return BindingBuilder.bind(fanoutQueueTwo()).to(fanoutExchange());
    }

    /**
     * 直连模式
     * 与主题模式相似，不过只支持完全匹配
     */
    @Bean
    public Queue directQueueOne(){
        return new Queue(Const.DIRECT_QUEUE_ONE);
    }
    @Bean
    public Queue directQueueTwo(){
        return new Queue(Const.DIRECT_QUEUE_TWO);
    }
    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(Const.DIRECT_CHANGE);
    }
    @Bean
    public Binding directBindOne(){
        return BindingBuilder.bind(directQueueOne()).to(directExchange()).with("direct.one.msg");
    }
    @Bean
    public Binding directBindTwo(){
        return BindingBuilder.bind(directQueueTwo()).to(directExchange()).with("direct.two.msg");
    }

    /**
     * 定制化amqp模版
     *
     * ConfirmCallback接口用于实现消息发送到RabbitMQ交换器后接收ack回调   即消息发送到exchange  ack
     * ReturnCallback接口用于实现消息发送到RabbitMQ 交换器，但无相应队列与交换器绑定时的回调  即消息发送不到任何一个队列中  ack
     */
    /*@Bean
    public RabbitTemplate rabbitTemplate(){
        rabbitTemplate.setMandatory(true);

        // 消息返回，需要配置publisher-returns: true
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routeKey) -> {
            // 发送的消息
            String correlationId = message.getMessageProperties().getCorrelationId();
            log.error("消息：{}发送失败，应答码：{}，原因：{}，交换机：{}，路由键：{}", new Object[]{correlationId, replyCode, replyText, exchange, routeKey});
        });

        // 消息确认，需要配置publisher-confirms: true
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack){
                log.info("消息发送到交换机成功,id:{}", new Object[]{correlationData.getId()});
            } else {
                log.error("消息发送到交换机失败，原因:{}", cause);
            }
        });

        return rabbitTemplate;
    }*/

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack){
            log.info("消息发送成功:{}", correlationData);
        } else {
            log.info("消息发送失败:{}", cause);
        }
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        // 反序列化对象输出
        log.info("消息主题:{}", new String(message.getBody()));
        log.info("应答码:{}", replyCode);
        log.info("描述{}", replyText);
        log.info("消息使用的交换器 exchange:{}", exchange);
        log.info("消息使用的路由键 routing:{}", routingKey);
    }
}
