package com.ActiveMQ;

import com.google.gson.Gson;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.TextMessage;
import java.util.Map;

@Component
@Log
public class Producer {

    @Autowired
    JmsTemplate jmsTemplate;

    public void sendMessage(final String queueName, final String message) {
        Map map = new Gson().fromJson(message, Map.class);
        final String textMessage = (String) map.get("name");
        log.info("[1] Sending message " + textMessage + "to queue - " + queueName);
        jmsTemplate.send(queueName, session -> {
            TextMessage message1 = session.createTextMessage();
            return message1;
        });
    }

}