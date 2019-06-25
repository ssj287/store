package com.itheima.dao;

import java.util.List;

import com.itheima.domain.Comment;

public interface CommentDao {

	List<Comment> getByPid(String pid) throws Exception;


}
