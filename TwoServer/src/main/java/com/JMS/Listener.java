package com.JMS;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

@Component
@Log
@EnableJms
public class Listener {
    @Autowired
    private DeleteObject deleteObject;
    @JmsListener(destination = "deleteObject.topic")
    @SendTo("answer")
    public String receiveMessageFromTopic(final Message jsonMessage) throws JMSException
    {
        log.info("[2] Received message " + jsonMessage);
        if(jsonMessage instanceof TextMessage) {
            TextMessage textMessage = (TextMessage)jsonMessage;
            Integer id=textMessage.getIntProperty("id");
            String nameObject=textMessage.getStringProperty("nameObject");
            String loginUser=textMessage.getStringProperty("loginUser");
            solver(id,loginUser,nameObject);
        }
        return "{\"name\":\"request1\"}";
    }
   public void solver(int id,String login,String nameObject)
   {
       switch (nameObject)
       {
          case  "User":
              deleteObject.deleteUser(id);
             break;
          case  "Comment":
              deleteObject.deleteComment(id);
               break;
          case  "Post":
              deleteObject.deletePost(id);
               break;
       }
   }
}