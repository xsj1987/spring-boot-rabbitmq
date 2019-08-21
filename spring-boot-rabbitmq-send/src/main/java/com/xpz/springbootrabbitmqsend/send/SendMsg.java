package com.xpz.springbootrabbitmqsend.send;

import com.xpz.common.Const;
import com.xpz.entity.User;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SendMsg {

    @Autowired
    private RabbitTemplate amqpTemplate;

    /**
     * 推送对象主题消息
     * @param user
     */
    public void sendMsg(User user){
        amqpTemplate.convertAndSend(Const.TOPIC_CHANGE, "topic.two.message", user);
    }

    /**
     * 推送字符串主题消息
     * @param str
     */
    public void sendMsg(String str){
        amqpTemplate.convertAndSend(Const.TOPIC_CHANGE, "topic.one.message", str);
    }
}