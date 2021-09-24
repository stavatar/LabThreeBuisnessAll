package com.service;




import lombok.extern.java.Log;
import com.Security.RoleType;
import com.entity.Comments;
import com.entity.Position;
import com.entity.Posts;
import com.entity.Users;
import com.repository.PositionRepository;
import com.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Service
@Log
public class UserService
{
    @Autowired
    private UsersRepository usersRepository;


    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;

    @Autowired
    private TransactionTemplate transactionTemplate;



    public Users findByLogin(String login) {
        return usersRepository.findByLogin(login);
    }
    public List<Users> getAll()
    {
        return usersRepository.findAll();
    }

    public boolean delete(int  id)
    {
        return (boolean) transactionTemplate.execute((TransactionCallback) status -> {
            if (usersRepository.existsById((long) id)) {
                Users user = usersRepository.findById((long) id).get();
                user.getListPost().forEach(posts -> {
                    posts.setOwner(null);
                    postService.save(posts);
                });
                user.getListComment().forEach(comments -> {
                    comments.setOwner(null);
                    commentService.save(comments);
                });
                usersRepository.delete(user);
                return true;
            } else return false;
        });
    }


}
