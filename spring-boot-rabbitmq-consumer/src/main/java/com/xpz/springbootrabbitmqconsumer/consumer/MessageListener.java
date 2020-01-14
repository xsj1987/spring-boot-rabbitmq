package com.xpz.springbootrabbitmqconsumer.consumer;

import com.rabbitmq.client.Channel;
import com.xpz.common.Const;
import com.xpz.common.MessageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Description TODO
 * @Author xsj
 * @Date 2019/12/18
 * @Version 1.0
 */
@Component
@Slf4j
public class MessageListener {
    /**
     * 通过Queues设置监听队列会产生一个问题
     * 当queue不存在时，启动监听服务会提示找不到queue，导致服务无法启动
     * 解决办法：@RabbitListener(queuesToDeclare=@Queue(Const.TOPIC_QUEUE_ONE))
     * @param msg
     * @param channel
     */
    //@RabbitListener(queues = Const.TOPIC_QUEUE_ONE)
    /*@RabbitListener(queuesToDeclare=@Queue(Const.TOPIC_QUEUE_ONE))
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
    }*/

    @RabbitListener(queuesToDeclare = @Queue(Const.TOPIC_QUEUE_ONE))
    public void topicListenerOne(String msg){
        log.info("监听队列:{},收到的消息为:{}", Const.TOPIC_QUEUE_ONE , msg);
    }

    @RabbitListener(queuesToDeclare = @Queue(Const.TOPIC_QUEUE_TWO))
    public void topicListenerTwo(Message message, Channel channel){
        try{
            String msg = MessageHelper.msgToObj(message, String.class);
            long tag = message.getMessageProperties().getDeliveryTag();
            log.info("监听队列:{},收到的消息为:{}, tag:{}", Const.TOPIC_QUEUE_TWO, msg, tag);
            channel.basicAck(tag, true);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @RabbitListener(queuesToDeclare = @Queue(Const.TOPIC_QUEUE_THREE))
    public void topicListenerThree(Message message, Channel channel){
        try{
            String msg = MessageHelper.msgToObj(message, String.class);
            long tag = message.getMessageProperties().getDeliveryTag();
            log.info("监听队列:{},收到的消息为:{}, tag:{}", Const.TOPIC_QUEUE_THREE, msg, tag);
            channel.basicReject(tag, false);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @RabbitListener(queuesToDeclare = @Queue(Const.TOPIC_QUEUE_FOUR))
    public void topicListenerFour(Message message, Channel channel){
        try{
            String msg = MessageHelper.msgToObj(message, String.class);
            long tag = message.getMessageProperties().getDeliveryTag();
            log.info("监听队列:{},收到的消息为:{}, tag:{}", Const.TOPIC_QUEUE_FOUR, msg, tag);
            channel.basicAck(tag, true);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 监听直连消息1
     * @param message
     * @param channel
     */
    @RabbitListener(queuesToDeclare = @Queue(Const.DIRECT_QUEUE_ONE))
    public void consumer(Message message, Channel channel) throws IOException {
        String msg = MessageHelper.msgToObj(message, String.class);
        long tag = message.getMessageProperties().getDeliveryTag();
        log.info("监听队列：{},接收到消息：msg:{}, tag:{}", Const.DIRECT_QUEUE_ONE, msg, tag);
        channel.basicAck(tag, false);
    }

    /**
     * 监听直连消息2
     * @param message
     * @param channel
     */
    @RabbitListener(queuesToDeclare = @Queue(Const.DIRECT_QUEUE_TWO))
    public void consumer2(Message message, Channel channel) throws IOException {
        String msg = MessageHelper.msgToObj(message, String.class);
        long tag = message.getMessageProperties().getDeliveryTag();
        log.info("监听队列：{},接收到消息：msg:{}, tag:{}", Const.DIRECT_QUEUE_TWO, msg, tag);
        channel.basicAck(tag, false);
    }

    /**
     * 监听扇形队列消息1
     * @param message
     * @param channel
     */
    @RabbitListener(queuesToDeclare = @Queue(Const.FANOUT_QUEUE_ONE))
    public void fanoutListener(Message message, Channel channel){
        try{
            String msg = MessageHelper.msgToObj(message, String.class);
            long tag = message.getMessageProperties().getDeliveryTag();
            log.info("监听队列：{},接收到消息：msg:{}, tag:{}", Const.FANOUT_QUEUE_ONE, msg, tag);
            channel.basicAck(tag, true);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 监听扇形队列消息2
     * @param message
     * @param channel
     */
    @RabbitListener(queuesToDeclare = @Queue(Const.FANOUT_QUEUE_TWO))
    public void fanoutListener2(Message message, Channel channel){
        try{
            String msg = MessageHelper.msgToObj(message, String.class);
            long tag = message.getMessageProperties().getDeliveryTag();
            log.info("监听队列：{},接收到消息：msg:{}, tag:{}", Const.FANOUT_QUEUE_TWO, msg, tag);
            channel.basicAck(tag, true);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 监听死信队列
     * @param message
     * @param channel
     */
    @RabbitListener(queuesToDeclare = @Queue(Const.DLX_QUEUE))
    public void dlxListener(Message message, Channel channel) {
        String msg = MessageHelper.msgToObj(message, String.class);
        log.info("监听死信队列：{}, msg:{}", Const.DLX_QUEUE, msg);
    }
}
