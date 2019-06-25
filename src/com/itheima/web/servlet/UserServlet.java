package com.itheima.web.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;

import com.itheima.constant.Constant;
import com.itheima.domain.User;
import com.itheima.myconventer.MyConventer;
import com.itheima.service.UserService;
import com.itheima.service.impl.UserServiceImpl;
import com.itheima.utils.BeanFactory;
import com.itheima.utils.MD5Utils;
import com.itheima.utils.UUIDUtils;

/**
 * 和用户相关的servlet
 */
public class UserServlet extends BaseServlet {

	/**
	 * 跳转到 注册页面
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String registUI(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		return "/jsp/register.jsp";
	}
	
	/**
	 * 用户注册
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	public String regist(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//1.封装数据
		User user = new User();
		
		//注册自定义转化器
		ConvertUtils.register(new MyConventer(), Date.class);
		BeanUtils.populate(user, request.getParameterMap());
		
		//1.1 设置用户id
		user.setUid(UUIDUtils.getId());
		
		//1.2 设置激活码
		user.setCode(UUIDUtils.getCode());
		
		//1.3加密密码
		user.setPassword(MD5Utils.md5(user.getPassword()));
		
		//2.调用service完成注册
		UserService s=(UserService) BeanFactory.getBean("UserService");
		s.regist(user);
		
		//3.页面请求转发
		request.setAttribute("msg", "用户注册已成功,请去邮箱激活~~");
		
		return "/jsp/msg.jsp";
	}
	
	/**
	 * 用户激活
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	public String active(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//1.获取激活码
		String code = request.getParameter("code");
		
		//2.调用service完成激活
		UserService s=(UserService) BeanFactory.getBean("UserService");
		User user=s.active(code);
		
		if(user==null){
			//通过激活码没有找到用户
			request.setAttribute("msg", "请重新激活");
		}else{
			//添加信息
			request.setAttribute("msg", "激活成功");
		}
		//3.请求转发到msg.jsp
		
		return "/jsp/msg.jsp";
	}
	
	/**
	 * 跳转到登录页面
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String loginUI(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		return "/jsp/login.jsp";
	}
	
	/**
	 * 登录
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	public String login(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 1.获取用户名和密码
				String username = request.getParameter("username");
				String password = request.getParameter("password");
				password = MD5Utils.md5(password);
				String rCode = request.getParameter("checkCode");
				String sCode = (String) request.getSession().getAttribute("sessionCode");
				request.getSession().removeAttribute("sessionCode");
				System.out.println(rCode+"=="+sCode);
				// 判断两个验证码是否一致.
				if (rCode == null || rCode.trim().length() == 0 || sCode == null) {
					// 验证码有问题 提示信息 页面跳转到login.jsp
					request.setAttribute("msg", "验证码不能为空");
					return "/jsp/login.jsp";
				}
				if (!rCode.equalsIgnoreCase(sCode)) {
					// 验证码输入不一致 提示信息 页面跳转到login.jsp
					request.setAttribute("msg", "验证码输入错误");
					return "/jsp/login.jsp";
				}
				// 2.调用serive完成登录操作 返回user
				UserService s = (UserService) BeanFactory.getBean("UserService");
				User user = s.login(username, password);
				// 3.判断用户
				if (user == null) {
					// 用户名密码不匹配
					request.setAttribute("msg", "用户名密码不匹配");
					return "/jsp/login.jsp";
				} else {
					// 若不为空 跳转到success.jsp
					request.getSession().setAttribute("user", user);

					// 判断是否勾选了自动登录 若勾选了需要将用户名和密码放入cookie中, 写回浏览器
					if (Constant.IS_AUTO_LOGIN.equals(request.getParameter("autoLogin"))) {
						// 创建cookie 注意中文
						Cookie c = new Cookie("autologin", username + "-" + password);
						c.setMaxAge(3600);
						c.setPath(request.getContextPath() + "/");

						response.addCookie(c);
					}

					// 判断是否勾选了记住用户名 若勾选了需要将用户名放入cookie中 写回浏览器
					if (Constant.IS_SAVE_NAME.equals(request.getParameter("saveName"))) {
						// 创建cookie
						Cookie c = new Cookie("savename", URLEncoder.encode(username, "utf-8"));
						c.setMaxAge(3600);
						c.setPath(request.getContextPath() + "/");

						response.addCookie(c);
					}

					// 页面重定向
					response.sendRedirect(request.getContextPath() + "/");
				}

				return null;
	}

	
	public String logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//干掉session
		request.getSession().invalidate();
		Cookie c=new Cookie("autologin", "");
		c.setPath(request.getContextPath()+"/");//   /day1101/
		
		//设置时间
		c.setMaxAge(0);
		
		//写会浏览器
		response.addCookie(c);
		//重定向
		response.sendRedirect(request.getContextPath());
		
		//处理自动登录
		
		return null;
	}
}
