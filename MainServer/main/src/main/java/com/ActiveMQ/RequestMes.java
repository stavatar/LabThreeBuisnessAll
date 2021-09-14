package com.ActiveMQ;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestMes implements Serializable
{
    String loginUser;
    String nameObject;
    Integer id;


}
