package com.itheima.web.filter;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itheima.domain.User;
import com.itheima.service.impl.UserServiceImpl;
import com.itheima.utils.CookUtils;

public class AutoLoginFilter implements Filter{
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		//1.强转
		HttpServletRequest request =(HttpServletRequest) req;
		HttpServletResponse response =(HttpServletResponse) resp;
		
		//2.完成自动登录
		//2.1 判断session中有无登录登录用户 没有的话继续自动登录
		User user=(User) request.getSession().getAttribute("user");
		if(user==null){
			//没有用户  需要自动登录
			//2.2 判断访问的资源是否和登录注册相关,若相关则不需要自动登录
			String path = request.getRequestURI();// /day1601/xxx
			if(!path.contains("/login")){
				
				//2.3获取指定的cookie
				Cookie c = CookUtils.getCookieByName("autologin", request.getCookies());
				//判断cookie是否为空
				//若不为空 获取值(username和passowrd) 调用serivce完成登录  判断user是否为空 不为空 放入session
				if(c!=null){
					String username=c.getValue().split("-")[0];
					String password=c.getValue().split("-")[1];
					
					//调用serivce完成登录
					//user=null;
					try {
						user = new UserServiceImpl().login(username, password);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if(user!=null){
						//将user放入session中
						request.getSession().setAttribute("user", user);
					}
				}
			}
			
		}
		
		
		
		//3.放行
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void destroy() {
		
	}

}
