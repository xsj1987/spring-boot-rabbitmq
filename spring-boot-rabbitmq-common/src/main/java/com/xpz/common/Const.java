package com.xpz.common;

public class Const {

    // 主题队列
    public static final String TOPIC_QUEUE_ONE = "topic.queue.one";
    public static final String TOPIC_QUEUE_TWO = "topic.queue.two";
    public static final String TOPIC_QUEUE_THREE = "topic.queue.three";
    public static final String TOPIC_QUEUE_FOUR = "topic.queue.four";
    // 主题交换机
    public static final String TOPIC_CHANGE = "topic.exchange";
    // 主题路由键
    public static final String TOPIC_ROUTE_KEY_ONE = "topic.route.key.one.*";
    public static final String TOPIC_ROUTE_KEY_TWO = "topic.route.key.two.#";

    // 扇形队列
    public static final String FANOUT_QUEUE_ONE = "fanout.queue.one";
    public static final String FANOUT_QUEUE_TWO = "fanout.queue.two";
    // 扇形交换机
    public static final String FANOUT_CHANGE = "fanout.exchange";

    // 直连队列
    public static final String DIRECT_QUEUE_ONE = "direct.queue.one";
    public static final String DIRECT_QUEUE_TWO = "direct.queue.two";
    // 直连交换机
    public static final String DIRECT_CHANGE = "direct.exchange";
    // 直连路由键
    public static final String DIRECT_ROUTE_KEY_ONE = "direct.route.key.one";
    public static final String DIRECT_ROUTE_KEY_TWO = "direct.route.key.two";

    // 死信交换机
    public static final String DLX_EXCHANGE = "dlx.exchange";
    // 死信队列
    public static final String DLX_QUEUE = "dlx.queue";
    // 死信路由
    public static final String DLX_ROUTE_KEY = "dlx.route.key";
}
