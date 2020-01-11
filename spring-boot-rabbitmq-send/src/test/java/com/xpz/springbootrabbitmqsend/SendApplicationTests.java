package com.xpz.springbootrabbitmqsend;

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
    public void sendTopicMsg(){
        /*for(int i=0;i<10;i++) {
            String str = "Hello,This is Topic message.";
            sendMsg.sendMsg(str);
        }*/
        /*String str = "{\"away\":3,\"event\":1,\"home\":1,\"id\":1215352,\"time\":\"42\",\"type\":1}";
        sendMsg.sendMsg(str);*/
    }

    @Test
    public void sendTopicUser(){
        User user = new User(1, "admin", new Date());
        sendMsg.sendMsg(user);
    }

}
