package com.JMS;



import com.entity.Comments;
import com.entity.Position;
import com.entity.Posts;
import com.entity.Users;
import com.repository.UsersRepository;

import com.repository.CommentsRepository;
import com.repository.PositionRepository;
import com.repository.PostsRepository;
import com.service.CommentService;
import com.service.PostService;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteObject
{
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private PostsRepository postsRepository;
    @Autowired
    private CommentsRepository commentsRepository;

    public boolean deleteComment(int id)
    {
        Comments comment=commentsRepository.findById((long) id).get();
        return  commentService.delete(comment);
    }
    public boolean deletePost(int id)
    {
        Posts post = postsRepository.findById((long) id).get();
        return  postService.delete(post);
    }
    public boolean deleteUser(int  id)
    {
        return  userService.delete(id);
    }
}
