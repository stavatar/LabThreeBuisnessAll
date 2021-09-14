package com.Security.Jaas;


import com.Security.jwt.JwtProvider;
import com.entity.Users;

import com.service.UserService;;
import lombok.extern.java.Log;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.authentication.jaas.DefaultJaasAuthenticationProvider;
import org.springframework.stereotype.Component;

import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import java.time.LocalDate;
import java.util.Map;

@Component
@Log
public class JaasLoginModule implements LoginModule, ApplicationContextAware
{
	 static ApplicationContext context;
    private CallbackHandler handler;
    private Subject subject;
    private  String login;
    private Users user;
    //private List<String> userGroups;

    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserService userService;
    @Autowired
    private DefaultJaasAuthenticationProvider defaultJaasAuthenticationProvider;

    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options)
        {
            handler = callbackHandler;
            this.subject = subject;
            jwtProvider= (JwtProvider) context.getBean("jwtProvider");
            userService=(UserService) context.getBean("userService");

        }



    @Override
    public boolean login() throws LoginException {
        Callback[] callbacks = new Callback[2];
        callbacks[0] = new NameCallback("username");
        callbacks[1] = new PasswordCallback("password", true);

        try {

            handler.handle(callbacks);
            String name = ((NameCallback) callbacks[0]).getName();
            String password = String.valueOf(((PasswordCallback) callbacks[1]).getPassword());
            String userLogin = jwtProvider.getLoginFromToken(password);
            Users userEntity =userService.findByLogin(userLogin);
            if (userEntity!=null) {
                user=userEntity;

                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public boolean commit() throws LoginException
    {
        subject.getPrincipals().add(new JaasPrincipal(user.getLogin()));

        return true;
    }

    @Override
    public boolean abort() throws LoginException {
        return false;
    }

    @Override
    public boolean logout() throws LoginException {
        subject.getPrincipals().clear();
        return true;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context=applicationContext;
    }
}
