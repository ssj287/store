package com.itheima.service.impl;

import java.util.List;

import com.itheima.dao.CommentDao;
import com.itheima.dao.impl.CommentDaoImpl;
import com.itheima.domain.Comment;
import com.itheima.service.CommentService;

public class CommentServiceImpl implements CommentService {

	@Override
	public List<Comment> getByPid(String pid) throws Exception {
		// TODO Auto-generated method stub
		CommentDao cd = new CommentDaoImpl();
		return cd.getByPid(pid);
	}
}
