package com.xpz.springbootrabbitmqreceive.receive;

import com.rabbitmq.client.Channel;
import com.xpz.common.Const;
import com.xpz.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class MsgListener {
    /**
     * 通过Queues设置监听队列会产生一个问题
     * 当queue不存在时，启动监听服务会提示找不到queue，导致服务无法启动
     * 解决办法：@RabbitListener(queuesToDeclare=@Queue(Const.TOPIC_QUEUE_ONE))
     * @param msg
     * @param channel
     */
    //@RabbitListener(queues = Const.TOPIC_QUEUE_ONE)
    @RabbitListener(queuesToDeclare=@Queue(Const.TOPIC_QUEUE_ONE))
    public void messageListener(Message msg, Channel channel){
        try {
            // 设置prefech_count=1,这样RabbitMQ就会使得每个Consumer在同一个时间点最多处理一个Message
            channel.basicQos(1);
            // 手动消息应答，只有应答以后，RabbitMQ才会将消息删除
            channel.basicAck(msg.getMessageProperties().getDeliveryTag(), true);
            log.info("手动确认消息监听队列:{},收到的消息为:{}", Const.TOPIC_QUEUE_ONE , new String(msg.getBody()));
        } catch (IOException e){
            log.error("队列{}消息接收失败,消息内容:{}", Const.TOPIC_QUEUE_ONE, new String(msg.getBody()));
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = Const.TOPIC_QUEUE_ONE)
    public void messageListener(String msg){
        log.info("监听队列:{},收到的消息为:{}", Const.TOPIC_QUEUE_ONE , msg);
    }

    @RabbitListener(queues = Const.TOPIC_QUEUE_TWO)
    public void ObjectListener(User user){
        log.info("监听队列:{},收到的消息为:{}", Const.TOPIC_QUEUE_TWO , user);
    }
}
