package com.xpz.springbootrabbitmqsend.conf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@Slf4j
public class RabbitConfig {

    @Autowired
    private CachingConnectionFactory connectionFactory;

    @Bean
    public RabbitTemplate rabbitTemplate(){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        // 消息是否成功发送到Exchange
        rabbitTemplate.setConfirmCallback(((correlationData, ack, cause) -> {
            if (ack){
                String msgId = correlationData.getId();
                log.info("消息{}成功发送到Exchange", msgId);
            } else {
                log.error("消息发送到Exchange失败,{},cause:{}", correlationData, cause);
            }
        }));
        // 触发setReturnCallback回调必须设置mandatory=true, 否则Exchange没有找到Queue就会丢弃掉消息, 而不会触发回调
        rabbitTemplate.setMandatory(true);
        // 消息是否从Exchange路由到Queue, 注意: 这是一个失败回调, 只有消息从Exchange路由到Queue失败才会回调这个方法
        rabbitTemplate.setReturnCallback(((message, replyCode, replyText, exchange, routingKey) -> {
            log.error("消息从Exchange路由到Queue失败,Exchange:{},route:{},route:{},replyCode:{},replyText:{},message:{}", exchange, routingKey, replyCode, replyText, message);
        }));
        return rabbitTemplate;
    }

    public Jackson2JsonMessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 确认消息是否已推送到交换机
     * @param correlationData
     * @param ack
     * @param cause
     */
    /*@Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack){
            log.info("消息发送成功:{}", correlationData);
        } else {
            log.info("消息发送失败:{}", cause);
        }
    }*/

    /**
     * 确认消息是否已推送到队列
     * @param message
     * @param replyCode
     * @param replyText
     * @param exchange
     * @param routingKey
     */
    /*@Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("消息主题:{}", new String(message.getBody()));
        log.info("应答码:{}", replyCode);
        log.info("描述{}", replyText);
        log.info("消息使用的交换器 exchange:{}", exchange);
        log.info("消息使用的路由键 routing:{}", routingKey);
    }*/
}
