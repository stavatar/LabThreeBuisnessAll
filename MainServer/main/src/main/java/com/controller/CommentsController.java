package com.controller;

import com.Security.Manager.ActionType;
import com.Security.Manager.SecurityRolesManager;
import com.entity.Comments;


import com.service.CommentService;
import com.service.PostService;
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
import java.util.Optional;
@Tag(name = "CommentsController", description = "Содержит методы для работы с комментариями")

@RestController
@Log
public class CommentsController
{
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private JmsTemplate jmsTemplate;
    @GetMapping(value = "/user/comments_all/")
    @Operation(summary = "Вывод всех комментариев")
    public ResponseEntity<List<Comments>> readAll()
    {
        final List<Comments> posts = commentService.getAll();

        return posts != null &&  !posts.isEmpty()
                ? new ResponseEntity<>(posts, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
       @PostMapping(value = "/user/post{id}/{parent_id}add_comments/")
       @Operation(summary = "Создание комментария")
       public ResponseEntity<?> create(@RequestBody Comments new_comments, @PathVariable(name = "parent_id") @Parameter(description = "id комментария-родителя") Optional<Long> parent_comments, @PathVariable(name = "id") int posts_id)
    {

        if (SecurityRolesManager.checkPermission(ActionType.WRITE_COMMENTS))
        {
            if (parent_comments.isPresent())
                commentService.create(new_comments, parent_comments.get(), SecurityRolesManager.getNameCurrentUser(), posts_id);
            else commentService.create(new_comments, null, SecurityRolesManager.getNameCurrentUser(), posts_id);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    @GetMapping(value = "/user/comment{id}/read/")
    @Operation(summary = "Создание комментария")
    public ResponseEntity<Comments> read(@PathVariable(name = "id") int id)
    {

        final Comments client = commentService.get(id);

        return client != null
                ? new ResponseEntity<>(client, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/user/comment{id}/getChild/")
    @Operation(summary = "Вывод дочерних комментариев")
    public ResponseEntity<List<Comments>> getChild(@PathVariable(name = "id") @Parameter(description = "ID комменатрия-родителя") int parent_id)
    {

        final Comments parent_comment = commentService.get(parent_id);

        return parent_comment != null
                ? new ResponseEntity<>(parent_comment.getChildComment(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @DeleteMapping(value = "/user/comment{id}/delete/")
    @Operation(summary = "Удалить комментарий")
    public  ResponseEntity<?>  delete(@PathVariable(name = "id") int id)
    {
        boolean checkPermission;
        if (userService.containComment(SecurityRolesManager.getNameCurrentUser(),id))
            checkPermission = SecurityRolesManager.checkPermission(ActionType.DELETE_YOUR_COMMENTS);
        else checkPermission =  SecurityRolesManager.checkPermission(ActionType.DELETE_ALIEN_COMMENTS);

       if (checkPermission)
        {
            jmsTemplate.send("deleteObject.topic", session -> {
                TextMessage message1 = session.createTextMessage();
                message1.setText("send");
                message1.setStringProperty("loginUser",SecurityRolesManager.getNameCurrentUser());
                message1.setStringProperty("nameObject","Comment");
                message1.setIntProperty("id",id);
                return message1;
            });
                return new ResponseEntity<>(HttpStatus.OK);
        } else   return   new ResponseEntity<>(HttpStatus.FORBIDDEN);

    }
    @PutMapping(value = "/user/comment/update/{id}/")
    @Operation(summary = "Изменить комментарий")
    public  ResponseEntity<?>  update(@RequestBody @Parameter(description = "Изменяемый коммент") Comments comments, @PathVariable(name = "id") @Parameter(description = "Новый коммент") int id)
    {
        boolean deleted;
        boolean checkPermission;
        if (userService.containComment(SecurityRolesManager.getNameCurrentUser(),id))
            checkPermission = SecurityRolesManager.checkPermission(ActionType.UPDATE_YOUR_COMMENTS);
        else checkPermission =  SecurityRolesManager.checkPermission(ActionType.UPDATE_ALIEN_COMMENTS);
        if (checkPermission)
        {
            deleted = commentService.update(comments, id);
            return deleted
                    ? new ResponseEntity<>(HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        } else   return   new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
