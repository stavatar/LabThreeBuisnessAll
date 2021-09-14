package com.controller;



import com.ActiveMQ.RequestMes;
import com.Security.Manager.ActionType;
import com.Security.Manager.SecurityRolesManager;
import com.entity.Users;



import com.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

import javax.jms.TextMessage;
import java.util.List;
@Tag(name = "UserController", description = "Содержит методы для взаимодействия с пользователями")
@RestController
@Log
public class UserController
{
    @Autowired
    private UserService userService;
    @Autowired
    private JmsTemplate jmsTemplate;
    @GetMapping(value = "/user/users/all/")
    @Operation(summary = "Получение всех пользователей")
    public ResponseEntity<List<Users>> read()
    {
        final List<Users> users = userService.getAll();

        return users != null &&  !users.isEmpty()
                ? new ResponseEntity<>(users, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping(value = "/user/users/{id}/")
    @Operation(summary = "Получение  конкретного пользователя")
    public ResponseEntity<Users> read(@PathVariable(name = "id") int id)
    {
        final Users client = userService.get(id);

        return client != null
                ? new ResponseEntity<>(client, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/user/users/delete/{id}/")
    @Operation(summary = "Удаление конкретного пользователя")
    public ResponseEntity<?> delete(@PathVariable(name = "id") int delete_user_id)
    {
        boolean checkPermission;
        if (userService.findByLogin(SecurityRolesManager.getNameCurrentUser()).getId()==delete_user_id)
            checkPermission = SecurityRolesManager.checkPermission(ActionType.DELETE_YOUR_USER);
        else checkPermission =  SecurityRolesManager.checkPermission(ActionType.DELETE_ALIEN_USER);
        if (checkPermission)
        {
            jmsTemplate.send("deleteObject.topic", session -> {
                TextMessage message1 = session.createTextMessage();
                message1.setText("send");
                message1.setStringProperty("loginUser",SecurityRolesManager.getNameCurrentUser());
                message1.setStringProperty("nameObject","User");
                message1.setIntProperty("id",delete_user_id);
                return message1;
            });

            return new ResponseEntity<>(HttpStatus.OK);

        } else   return   new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }


    @PutMapping(value = "/user/users/update{id}/")
    @Operation(summary = "Изменение пользователя")
    public ResponseEntity<?> update(@RequestBody @Parameter(description = "Новый пользователь") Users new_user, @PathVariable(name = "id") @Parameter(description = "Старый пользователь") int update_user_id)
    {
        boolean checkPermission;
        if (userService.findByLogin(SecurityRolesManager.getNameCurrentUser()).getId()==update_user_id)
            checkPermission = SecurityRolesManager.checkPermission(ActionType.UPDATE_YOUR_USER);
        else checkPermission =  SecurityRolesManager.checkPermission(ActionType.UPDATE_ALIEN_USER);
        if (checkPermission)
        {
            return userService.update(new_user,update_user_id)
                    ? new ResponseEntity<>(HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        } else   return   new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

}
