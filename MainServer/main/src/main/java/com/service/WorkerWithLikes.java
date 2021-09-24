package com.service;

import com.entity.Posts;
import com.entity.Users;

public class WorkerWithLikes
{
    private WorkerWithLikes() {}

    public static void make_rate(Posts post, Users user, boolean likeordislike)
    {
        post.setCountLike(likeordislike ? (post.getCountLike() + 1) : (post.getCountLike() - 1));
        user.getListlike().put(post, likeordislike);
    }

    public static void remove_rate(Posts post,Users user,boolean likeordislike)
    {
        post.setCountLike(likeordislike?(post.getCountLike() - 1):(post.getCountLike()+1));
        user.getListlike().remove(post);
    }
    public static void remove_lastrate(Posts post,Users user,boolean likeordislike)
    {
        post.setCountLike(likeordislike?(post.getCountLike() + 1):(post.getCountLike()-1));
        user.getListlike().remove(post);
    }
}
