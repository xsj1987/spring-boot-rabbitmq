package com.xpz.common;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;

public class MessageHelper {

    public static Message objToMsg(Object obj){
        if (obj == null){
            return null;
        }
        Message message = MessageBuilder.withBody()
    }
}
