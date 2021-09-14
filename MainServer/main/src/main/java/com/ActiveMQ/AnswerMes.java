package com.ActiveMQ;

import org.springframework.http.ResponseEntity;

import java.io.Serializable;

public class AnswerMes implements Serializable
{
    RequestMes mes;
    ResponseEntity<?> result;
}
