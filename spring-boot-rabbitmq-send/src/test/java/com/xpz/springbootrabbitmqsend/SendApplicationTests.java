package com.xpz.springbootrabbitmqsend;

import com.xpz.common.Const;
import com.xpz.entity.User;
import com.xpz.springbootrabbitmqsend.send.SendMsg;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SendApplicationTests {

    @Autowired
    private SendMsg sendMsg;

    @Test
    public void contextLoads() {
    }

    @Test
    public void sendDirectMsg(){
        for(int i=0;i<10;i++) {
            String str = "Hello,This is Topic message." + i;
            sendMsg.sendMsg(str);
        }
    }

    @Test
    public void sendTopicUser(){
        User user = new User(1, "admin", new Date());
        sendMsg.sendMsg(user);
    }

    @Test
    public void sendTopicMsg(){
        String routeKey = "topic.route.key.one.abc";
        for(int i=0;i<10;i++) {
            String str = "Hello,This is Topic message." + i;
            sendMsg.sendMsg(str, Const.TOPIC_CHANGE, routeKey);
        }
    }

    @Test
    public void sendFanoutMsg(){
        for(int i=0;i<10;i++) {
            String str = "Hello,This is Fanout message." + i;
            sendMsg.sendMsg(str, Const.FANOUT_CHANGE, "");
        }
    }

}
