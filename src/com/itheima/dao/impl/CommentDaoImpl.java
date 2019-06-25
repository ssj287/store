package com.itheima.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.itheima.dao.CommentDao;
import com.itheima.domain.Comment;
import com.itheima.utils.DataSourceUtils;

public class CommentDaoImpl implements CommentDao{

	@Override
	public List<Comment> getByPid(String pid) throws Exception {
		// TODO Auto-generated method stub
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql ="select * from comment where pid = ?" ;
		return qr.query(sql, new BeanListHandler<>(Comment.class),pid);
	}
}
