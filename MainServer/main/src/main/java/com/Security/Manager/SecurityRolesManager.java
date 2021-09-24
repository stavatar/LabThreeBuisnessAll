package com.Security.Manager;

import org.springframework.security.authentication.jaas.JaasGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;
//Хранилище списка ролей и их полномочий
public class SecurityRolesManager
{
    private   static  HashMap<RoleType, Set<ActionType>> roleAction;
    static
    {
        roleAction=new HashMap<>();

        roleAction.put(RoleType.ROLE_USER_BASED,new HashSet<ActionType>(){});

        roleAction.put(RoleType.ROLE_USER_VIP,new HashSet<ActionType>(){});
            roleAction.get(RoleType.ROLE_USER_VIP).add(ActionType.WRITE_COMMENTS);
            roleAction.get(RoleType.ROLE_USER_VIP).add(ActionType.WRITE_POSTS);
            roleAction.get(RoleType.ROLE_USER_VIP).add(ActionType.DELETE_YOUR_COMMENTS);
            roleAction.get(RoleType.ROLE_USER_VIP).add(ActionType.DELETE_YOUR_POST);
            roleAction.get(RoleType.ROLE_USER_VIP).add(ActionType.UPDATE_YOUR_COMMENTS);
            roleAction.get(RoleType.ROLE_USER_VIP).add(ActionType.UPDATE_YOUR_POST);
            roleAction.get(RoleType.ROLE_USER_VIP).add(ActionType.DELETE_YOUR_USER);
            roleAction.get(RoleType.ROLE_USER_VIP).add(ActionType.UPDATE_YOUR_USER);
        roleAction.put(RoleType.ROLE_MODERATOR,new HashSet<ActionType>(){});
            roleAction.get(RoleType.ROLE_MODERATOR).addAll(roleAction.get(RoleType.ROLE_USER_VIP));
            roleAction.get(RoleType.ROLE_MODERATOR).add(ActionType.DELETE_ALIEN_COMMENTS);
            roleAction.get(RoleType.ROLE_MODERATOR).add(ActionType.DELETE_ALIEN_POST);
            roleAction.get(RoleType.ROLE_MODERATOR).add(ActionType.UPDATE_ALIEN_COMMENTS);
            roleAction.get(RoleType.ROLE_MODERATOR).add(ActionType.UPDATE_ALIEN_POST);

        roleAction.put(RoleType.ROLE_ADMIN,new HashSet<ActionType>(){});
            roleAction.get(RoleType.ROLE_MODERATOR).addAll(roleAction.get(RoleType.ROLE_MODERATOR));
            roleAction.get(RoleType.ROLE_ADMIN).add(ActionType.DELETE_ALIEN_USER);
            roleAction.get(RoleType.ROLE_ADMIN).add(ActionType.UPDATE_ALIEN_USER);

    }
    public static boolean  checkPermission(ActionType action)
    {
        JaasGrantedAuthority jaasGrantedAuthority= (JaasGrantedAuthority) SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray()[0];
        RoleType CurrentRole=RoleType.valueOf(jaasGrantedAuthority.getAuthority());
       if (roleAction.get(CurrentRole).contains(action)) return true;
        else return false;
    }
    public static String getNameCurrentUser()
    {
        JaasGrantedAuthority jaasGrantedAuthority= (JaasGrantedAuthority) SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray()[0];
        return  jaasGrantedAuthority.getPrincipal().getName();
    }

}
