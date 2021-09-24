package com.service;



import com.entity.Comments;
import com.entity.Posts;
import com.entity.Users;
import com.repository.CommentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Service
public class CommentService
{
    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;
     public void save(Comments comment)
     {
         commentsRepository.save(comment);
     }
    public List<Comments> getAll()
    {
        return (List<Comments>) commentsRepository.findAll();
    }


    public boolean delete(Comments comment)
    {
        return (boolean) transactionTemplate.execute(new TransactionCallback() {
            public Object doInTransaction(TransactionStatus status) {
                if (commentsRepository.existsById(comment.getId())) {
                    comment.setOwner(null);
                    comment.setPost(null);
                    comment.setParentComment(null);
                    commentsRepository.delete(comment);
                    return true;
                }
                return false;
            }
        });
    }
   
}
