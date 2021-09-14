package com.repository;

import com.entity.Comments;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentsRepository  extends CrudRepository<Comments, Long> {

}