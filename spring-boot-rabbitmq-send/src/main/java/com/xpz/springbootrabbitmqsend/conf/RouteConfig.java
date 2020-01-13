package com.xpz.springbootrabbitmqsend.conf;

import com.xpz.common.Const;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

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
    public Queue topicQueueThree(){
        return new Queue(Const.TOPIC_QUEUE_THREE);
    }
    @Bean
    public Queue topicQueueFour(){
        return new Queue(Const.TOPIC_QUEUE_FOUR);
    }
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(Const.TOPIC_CHANGE);
    }
    @Bean
    public Binding topicBindOne(){
        return BindingBuilder.bind(topicQueueOne()).to(topicExchange()).with(Const.TOPIC_ROUTE_KEY_ONE);
    }
    @Bean
    public Binding topicBindTwo(){
        return BindingBuilder.bind(topicQueueTwo()).to(topicExchange()).with(Const.TOPIC_ROUTE_KEY_TWO);
    }
    @Bean
    public Binding topicBindThree(){
        return BindingBuilder.bind(topicQueueThree()).to(topicExchange()).with(Const.TOPIC_ROUTE_KEY_ONE);
    }
    @Bean
    public Binding topicBindFour(){
        return BindingBuilder.bind(topicQueueFour()).to(topicExchange()).with(Const.TOPIC_ROUTE_KEY_TWO);
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
        return BindingBuilder.bind(directQueueOne()).to(directExchange()).with(Const.DIRECT_ROUTE_KEY_ONE);
    }
    @Bean
    public Binding directBindTwo(){
        return BindingBuilder.bind(directQueueTwo()).to(directExchange()).with(Const.DIRECT_ROUTE_KEY_TWO);
    }
}
