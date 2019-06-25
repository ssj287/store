package com.itheima.dao.impl;

import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.itheima.dao.ProductDao;
import com.itheima.domain.Product;
import com.itheima.utils.DataSourceUtils;

public class ProductDaoImpl implements ProductDao{

	/**
	 * 查询最新
	 */
	@Override
	public List<Product> findNew() throws Exception {
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql="select * from product order by pdate desc limit 9";
		return qr.query(sql, new BeanListHandler<>(Product.class));
	}

	/**
	 * 查询热门
	 */
	@Override
	public List<Product> findHot() throws Exception {
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql="select * from product where is_hot = 1 order by pdate desc limit 9";
		return qr.query(sql, new BeanListHandler<>(Product.class));
	}

	/**
	 * 通过商品id 获取商品详情
	 */
	@Override
	public Product getByPid(String pid) throws Exception {
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		
		String sql="select * from product where pid = ? limit 1";
		return qr.query(sql, new BeanHandler<>(Product.class), pid);
	}

	/**
	 * 查询当前也需要展示的数据
	 */
	@Override
	public List<Product> findByPage(int currPage, int pageSize, String cid) throws Exception {
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql="select * from product where cid = ? limit ?,?";
		return qr.query(sql, new BeanListHandler<>(Product.class), cid,(currPage-1)*pageSize,pageSize);
	}

	/**
	 * 查询当前类别的总条数
	 */
	@Override
	public int getTotalCount(String cid) throws Exception {
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql="select count(*) from product where cid = ?";
		return ((Long)qr.query(sql, new ScalarHandler(), cid)).intValue();
	}

	/**
	 * 更新商品的cid 为删除分类的时候准备
	 */
	@Override
	public void updateCid(String cid) throws Exception {
		QueryRunner qr = new QueryRunner();
		String sql="update product set cid = null where cid = ?";
		qr.update(DataSourceUtils.getConnection(), sql, cid);
	}

	@Override
	public List<Product> findAll() throws Exception {
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql="select * from product";
		return qr.query(sql, new BeanListHandler<>(Product.class));
	}

	/**
	 * 添加商品
	 */
	@Override
	public void add(Product p) throws Exception {
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		/**
		 * `pid` VARCHAR(32) NOT NULL,
		  `pname` VARCHAR(50) DEFAULT NULL,
		  `market_price` DOUBLE DEFAULT NULL,
		  
		  `shop_price` DOUBLE DEFAULT NULL,
		  `pimage` VARCHAR(200) DEFAULT NULL,
		  `pdate` DATE DEFAULT NULL,
		  
		  `is_hot` INT(11) DEFAULT NULL,
		  `pdesc` VARCHAR(255) DEFAULT NULL,
		  `pflag` INT(11) DEFAULT NULL,
		  `cid` VARCHAR(32) DEFAULT NULL,
		 */
		String sql="insert into product values(?,?,?,?,?,?,?,?,?,?);";
		qr.update(sql, p.getPid(),p.getPname(),p.getMarket_price(),
				p.getShop_price(),p.getPimage(),p.getPdate(),
				p.getIs_hot(),p.getPdesc(),p.getPflag(),p.getCategory().getCid());
	}

}
