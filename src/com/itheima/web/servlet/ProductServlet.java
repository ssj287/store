package com.itheima.web.servlet;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alipay.api.domain.CommentReplyOpenModel;
import com.itheima.domain.Comment;
import com.itheima.domain.PageBean;
import com.itheima.domain.Product;
import com.itheima.service.CommentService;
import com.itheima.service.ProductService;
import com.itheima.service.impl.CommentServiceImpl;
import com.itheima.service.impl.ProductServiceImpl;
import com.itheima.utils.BeanFactory;
import com.itheima.utils.CookUtils;

/**
 * 商品servlet
 */
public class ProductServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * 通过id查询单个商品详情
	 * @throws Exception 
	 */
	public String  getById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//1.获取商品的id
		String pid=request.getParameter("pid");
		//2.调用service
		//ProductService ps=new ProductServiceImpl();
		ProductService ps=(ProductService) BeanFactory.getBean("ProductService");
		Product p=ps.getByPid(pid);
		/*String id = p.getPimage();
		//1.获取指定的cookie ids
				Cookie c = CookUtils.getCookieByName("ids", request.getCookies());
				
				String ids="";
				//2.判断cookie是否为空
				if(c==null){
					//若cookie为空  需要将当前商品id放入ids中
					ids=id;
				}else{
					//若cookie不为空 继续判断ids中是否已经该id // ids=2-11-21
					//获取值
					ids=c.getValue();
					String[] arr = ids.split("-");
					//将数组转成集合  此list长度不可变
					List<String> asList = Arrays.asList(arr);
					//将aslist放入一个新list中
					LinkedList<String> list = new LinkedList<>(asList);
					
					if(list.contains(id)){
						//若ids中包含id  将id移除 放到最前面
						list.remove(id);
						list.addFirst(id);
					}else{
						//若ids中不包含id  继续判断长度是否大于2
						if(list.size()>2){
							//长度>=3 移除最后一个 将当前的放入最前面
							list.removeLast();
							list.addFirst(id);
						}else{
							//长度<3 将当前放入最前面
							list.addFirst(id);
						}
					}
					
					ids="";
					//将list转成字符串
					for (String s : list) {
						ids+=(s+"-");
					}
					ids=ids.substring(0, ids.length()-1);
				}
				
				//将ids写回去
				c=new  Cookie("ids",ids);
				//设置访问路径
				c.setPath(request.getContextPath()+"/");
				//设置存活时间
				c.setMaxAge(3600);
				
				//写会浏览器
				response.addCookie(c);*/
		//3.将结果放入request中 请求转发
		CommentService cs = new CommentServiceImpl();
		List<Comment> cm = cs.getByPid(pid);
		request.setAttribute("bean", p);
		request.setAttribute("cm", cm);
		return "/jsp/product_info.jsp";
	}
	
	/**
	 * 分页查询数据
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String  findByPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//1.获取类别 当前页  设置一个pagesize
		String cid=request.getParameter("cid");
		int currPage=Integer.parseInt(request.getParameter("currPage"));
		int pageSize=12;
		
		
		//2.调用service 返回值pagebean
		ProductService ps=(ProductService) BeanFactory.getBean("ProductService");
		PageBean<Product> bean=ps.findByPage(currPage,pageSize,cid);
		
		//3.将结果放入request中 请求转发
		request.setAttribute("pb", bean);
		return "/jsp/product_list.jsp";
	}

}
