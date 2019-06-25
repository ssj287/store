package com.itheima.service;

import java.util.List;

import com.itheima.domain.Comment;

public interface CommentService {

	List<Comment> getByPid(String pid) throws Exception;

	

}
