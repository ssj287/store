package com.itheima.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itheima.domain.Category;
import com.itheima.domain.Product;
import com.itheima.service.CategoryService;
import com.itheima.service.ProductService;
import com.itheima.utils.BeanFactory;

/**
 *后台商品管理
 */
public class AdminProductServlet extends BaseServlet {
	
	/**
	 * 查询所有商品
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String findAll(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//1.调用service 查询所有 返回一个list
		ProductService ps=(ProductService) BeanFactory.getBean("ProductService");
		List<Product> list=ps.findAll();
		
		//2.将list放入request域中 请求转发
		request.setAttribute("list", list);
		
		return "/admin/product/list.jsp";
	}
	
	/**
	 * 跳转到添加商品的页面
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String addUI(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//查询所有的分类 返回list
		CategoryService cs=(CategoryService) BeanFactory.getBean("CategoryService");
		List<Category> clist = cs.findAll();
		
		//将list放入request
		request.setAttribute("clist", clist);
		return "/admin/product/add.jsp";
	}

}
