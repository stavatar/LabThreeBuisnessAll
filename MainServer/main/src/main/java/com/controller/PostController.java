package com.controller;



import com.Security.Manager.ActionType;
import com.Security.Manager.SecurityRolesManager;
import com.entity.Posts;



import com.service.PostService;
import com.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

import javax.jms.TextMessage;
import javax.servlet.ServletException;
import java.util.List;
@Tag(name = "PostController", description = "Содержит методы для работы с постами")

@RestController
@Log
public class PostController
{
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;
    @Autowired
    private JmsTemplate jmsTemplate;
    @GetMapping(value = "/user/posts/")
    @Operation(summary = "Получение всех постов")

    public ResponseEntity<List<Posts>> read()
    {
        final List<Posts> posts = postService.getAll();

        return posts != null &&  !posts.isEmpty()
                ? new ResponseEntity<>(posts, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/user/create_post/",consumes = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Создание поста")

    public ResponseEntity<?> create(@RequestBody Posts posts) throws ServletException {

        if (SecurityRolesManager.checkPermission(ActionType.WRITE_POSTS))
        {
            postService.create(posts, SecurityRolesManager.getNameCurrentUser());
            return new ResponseEntity<>(HttpStatus.CREATED);
        }else return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    @GetMapping(value = "/user/post{id}/read/")
    @Operation(summary = "Получение конкретного поста")

    public ResponseEntity<Posts> read(@PathVariable(name = "id") int id) {
        final Posts client = postService.get(id);

        return client != null
                ? new ResponseEntity<>(client, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping(value = "/user/post{id}/like/")
    @Operation(summary = "Поставить лайк к посту")
    public  ResponseEntity<?> like(@PathVariable(name = "id") int post_id)
    {
       return postService.add_like(post_id,SecurityRolesManager.getNameCurrentUser(),true);
    }
    @GetMapping(value = "/user/post{id}/dislike/")
    @Operation(summary = "Поставить дизлайк к посту")
    public  ResponseEntity<?> dislike(@PathVariable(name = "id") int post_id)
    {
        return postService.add_like(post_id,SecurityRolesManager.getNameCurrentUser(),false);

    }
    @DeleteMapping(value = "/user/post{id}/delete/")
    @Operation(summary = "Удалить пост")
    public  ResponseEntity<?>  delete(@PathVariable(name = "id") int id)
    {

        boolean deleted;
        boolean checkPermission;
         if (userService.containPost(SecurityRolesManager.getNameCurrentUser(),id))
              checkPermission = SecurityRolesManager.checkPermission(ActionType.DELETE_YOUR_POST);
         else checkPermission =  SecurityRolesManager.checkPermission(ActionType.DELETE_ALIEN_POST);

        if(checkPermission)
        {
            jmsTemplate.send("deleteObject.topic", session -> {
                TextMessage message1 = session.createTextMessage();
                
                message1.setText("send");
                message1.setStringProperty("loginUser",SecurityRolesManager.getNameCurrentUser());
                message1.setStringProperty("nameObject","Post");
                message1.setIntProperty("id",id);
                return message1;
            });
            return  new ResponseEntity<>(HttpStatus.OK);

        } else   return   new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    @PutMapping(value = "/user/post{id}/update/")
    @Operation(summary = "Изменить пост")
    public  ResponseEntity<?>  update(@RequestBody Posts new_posts,@PathVariable(name = "id") int id)
    {
        boolean deleted;
        boolean checkPermission;
        if (userService.containPost(SecurityRolesManager.getNameCurrentUser(),id))
            checkPermission = SecurityRolesManager.checkPermission(ActionType.UPDATE_YOUR_POST);
        else checkPermission =  SecurityRolesManager.checkPermission(ActionType.UPDATE_ALIEN_POST);
        if (checkPermission)
        {
            deleted =  postService.update(new_posts, id);

               return deleted
                        ? new ResponseEntity<>(HttpStatus.OK)
                        : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        } else   return   new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
