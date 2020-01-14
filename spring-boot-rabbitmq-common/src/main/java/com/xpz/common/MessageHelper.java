package com.xpz.common;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;

public class MessageHelper {

    /**
     * 序列化消息
     * @param obj 序列化对象
     * @param expiration 延迟时间
     * @return
     */
    public static Message objToMsg(Object obj, String expiration){
        if (obj == null){
            return null;
        }
        Message message = MessageBuilder.withBody(JsonUtil.objToStr(obj).getBytes()).build();
        message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
        // 设置消息内容格式
        message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);
        message.getMessageProperties().setExpiration(expiration);
        return message;
    }

    /**
     * 反序列化消息
     * @param message
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T msgToObj(Message message, Class<T> clazz){
        if (message == null || clazz == null){
            return null;
        }

        String str = new String(message.getBody());
        T obj = JsonUtil.strToObj(str, clazz);
        return obj;
    }
}
