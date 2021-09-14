package com.Security.Jaas;

import java.io.Serializable;
import java.security.Principal;

public class JaasPrincipal implements Principal, Serializable
{
    private String username;
    public JaasPrincipal(String user) {
        this.username = user;
    }
    @Override
    public String getName() {
        return this.username;
    }


}
