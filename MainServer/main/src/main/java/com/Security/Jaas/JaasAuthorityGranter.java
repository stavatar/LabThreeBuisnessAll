package com.Security.Jaas;

import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.jaas.AuthorityGranter;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Collections;
import java.util.Set;
@Component
public class JaasAuthorityGranter implements AuthorityGranter
{
    @Autowired
    private UserService userService;
    @Override
    public Set<String> grant(Principal principal)
    {
        String nameRole=userService.findByLogin(principal.getName()).getPosition().getName().toString();
        return  Collections.singleton(nameRole);
    }
}
