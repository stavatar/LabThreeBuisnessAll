package com.service;



import lombok.extern.java.Log;
import com.entity.Posts;
import com.entity.Users;
import com.repository.PostsRepository;
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
    private TransactionTemplate transactionTemplate;


 
    public void save(Posts post)
    {
        transactionTemplate.execute(status ->
        {
            postsRepository.save(post);
            return null;
        });
    }


    
    public boolean delete(Posts post)
    {
        return (boolean) transactionTemplate.execute(new TransactionCallback() {
            public Object doInTransaction(TransactionStatus status) {
                if (postsRepository.existsById(post.getId())) {
                    post.setOwner(null);
                    postsRepository.delete(post);
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
