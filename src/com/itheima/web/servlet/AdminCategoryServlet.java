package com.itheima.web.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itheima.domain.Category;
import com.itheima.service.CategoryService;
import com.itheima.utils.BeanFactory;
import com.itheima.utils.UUIDUtils;

/**
 * 后台分类管理
 */
public class AdminCategoryServlet extends BaseServlet {

	/**
	 * 展示所有分类
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String findAll(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//1.调用categoryservice 查询所有的分类信息 返回值 list
		CategoryService cs=(CategoryService) BeanFactory.getBean("CategoryService");
		List<Category> list = cs.findAll();
		
		//2.将list放入request域中 请求转发即可
		request.setAttribute("list", list);
		return "/admin/category/list.jsp";
	}
	
	/**
	 * 跳转到添加页面上
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String addUI(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return "/admin/category/add.jsp";
	}
	
	public String add(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//1.接受cname
		String cname = request.getParameter("cname");
		
		//2.封装category
		Category c = new Category();
		c.setCid(UUIDUtils.getId());
		c.setCname(cname);
		
		//3.调用service完成 添加操作
		CategoryService cs=(CategoryService) BeanFactory.getBean("CategoryService");
		cs.add(c);
		
		//4.重定向 查询所有分类
		response.sendRedirect(request.getContextPath()+"/adminCategory?method=findAll");
		
		
		return null;
	}
	
	/**
	 * 通过id获取分类信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//1.接受cid
		String cid = request.getParameter("cid");
		
		//2 调用service完成 查询操作 返回值:category
		CategoryService cs=(CategoryService) BeanFactory.getBean("CategoryService");
		Category c=cs.getById(cid);
		
		//3.将category放入request域中, 请求转发  /admin/category/edit.jsp
		request.setAttribute("bean", c);
		return "/admin/category/edit.jsp";
	}
	
	/**
	 * 更新分类信息方法
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String update(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//1.获取cid cname
		//2.封装参数
		Category c=new Category();
		c.setCid(request.getParameter("cid"));
		c.setCname(request.getParameter("cname"));
		
		//3.调用service 完成更新操作
		CategoryService cs=(CategoryService) BeanFactory.getBean("CategoryService");
		cs.update(c);
		
		//4.重定向 查询所有
		response.sendRedirect(request.getContextPath()+"/adminCategory?method=findAll");
		
		return null;
	}
	
	/**
	 * 删除分类
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//1.获取cid
		String cid = request.getParameter("cid");
		
		//2.调用service 完成删除
		CategoryService cs=(CategoryService) BeanFactory.getBean("CategoryService");
		cs.delete(cid);
		
		//3.重定向
		response.sendRedirect(request.getContextPath()+"/adminCategory?method=findAll");
		return null;
	}

}
