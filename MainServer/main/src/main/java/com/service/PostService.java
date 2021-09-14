package com.service;


import com.entity.Posts;
import com.entity.Users;
import com.repository.PostsRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Service
@Log
public class PostService
{
    @Autowired
    private PostsRepository postsRepository;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;
    @Autowired
    private TransactionTemplate transactionTemplate;

    public List<Posts> getAll()
{
    return (List<Posts>) postsRepository.findAll();
}
    public void create(Posts post,String nameUser)
    {
          transactionTemplate.execute((TransactionCallback<Void>) status -> {
              Users user = userService.findByLogin(nameUser);
              post.setOwner(user);
              postsRepository.save(post);
              return null;
          });
    }
    public void save(Posts post)
    {
        transactionTemplate.execute(status ->
        {
            postsRepository.save(post);
            return null;
        });;
    }


     public void make_rate(Posts post,Users user,boolean likeordislike)
     {
         post.setCountLike(likeordislike ? (post.getCountLike() + 1) : (post.getCountLike() - 1));
         user.getListlike().put(post, likeordislike);


     }

     public void remove_rate(Posts post,Users user,boolean likeordislike)
     {
          post.setCountLike(likeordislike?(post.getCountLike() - 1):(post.getCountLike()+1));
          user.getListlike().remove(post);

     }
    public void remove_lastrate(Posts post,Users user,boolean likeordislike)
    {
        post.setCountLike(likeordislike?(post.getCountLike() + 1):(post.getCountLike()-1));
        user.getListlike().remove(post);

    }
     public ResponseEntity<?> add_like(int id_post, String login, boolean likeordislike)
     {
         return (ResponseEntity<?>) transactionTemplate.execute(new TransactionCallback() {
             public Object doInTransaction(TransactionStatus status) {
                 Users user = userService.findByLogin(login);
                 Posts post = postsRepository.findById((long) id_post).get();
                 StringBuilder answer = new StringBuilder();
                 if (likeordislike) answer.append("Лайк ");
                 else answer.append("Дизлайк ");

                 if (!user.getListlike().containsKey(post)) {
                     make_rate(post, user, likeordislike);
                     answer.append(" поставлен");
                     userService.save(user);
                     postsRepository.save(post);
                 } else {
                     if (user.getListlike().get(post) == likeordislike) {
                         remove_rate(post, user, likeordislike);
                         answer.append(" убран");
                     } else {
                         remove_lastrate(post, user, likeordislike);
                         make_rate(post, user, likeordislike);
                         answer.append(" поставлен");
                     }
                     ;
                     userService.save(user);
                     postsRepository.save(post);

                 }
                 return new ResponseEntity<>(HttpStatus.OK);
             }});

     }





    public boolean update(Posts post, int id)
    {
        return (boolean) transactionTemplate.execute(new TransactionCallback() {
            public Object doInTransaction(TransactionStatus status) {
                if (postsRepository.existsById((long) id)) {
                    Posts post_old = postsRepository.findById((long) id).get();
                    if (post.getOwner() == null)
                        post.setOwner(post_old.getOwner());
                    if (post.getListComments() == null)
                        post.setListComments(post_old.getListComments());
                    if (post.getCountLike() == null)
                        post.setCountLike(post_old.getCountLike());
                    if (post.getTitle() == null)
                        post.setTitle(post_old.getTitle());
                    if (post.getDateCreate() == null)
                        post.setDateCreate(post_old.getDateCreate());
                    if (post.getContent() == null)
                        post.setContent(post_old.getContent());
                    if (post.getListusersliked() == null)
                        post.setListusersliked(post_old.getListusersliked());
                    post.setId((long) id);
                    postsRepository.save(post);
                    return true;
                }

                return false;
            }});
    }

    public  Posts get(int id)
    {
        return  postsRepository.findById((long) id).get();
    }
}
