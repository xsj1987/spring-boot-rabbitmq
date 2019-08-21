package com.xpz.springbootrabbitmqreceive.receive;

import com.rabbitmq.client.Channel;
import com.xpz.common.Const;
import com.xpz.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class MsgListener {

    /*@RabbitListener(queues = Const.TOPIC_QUEUE_ONE)
    public void messageListener(Message msg, Channel channel){
        try {
            channel.basicAck(msg.getMessageProperties().getDeliveryTag(), true);
            System.out.println("监听队列" + Const.TOPIC_QUEUE_ONE + ",获取的消息为:" + new String(msg.getBody()));
        } catch (IOException e){
            log.error("队列{}消息接收失败,消息内容:{}", Const.TOPIC_QUEUE_ONE, new String(msg.getBody()));
            e.printStackTrace();
        }
    }*/

    @RabbitListener(queues = Const.TOPIC_QUEUE_ONE)
    public void messageListener(String msg){
        log.info("监听队列:{},收到的消息为:{}", Const.TOPIC_QUEUE_ONE , msg);
    }

    @RabbitListener(queues = Const.TOPIC_QUEUE_TWO)
    public void ObjectListener(User user){
        System.out.println("监听队列:"+Const.TOPIC_QUEUE_ONE+",获取的消息为:"+user);
    }
}
