package com.itheima.web.servlet;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.config.AlipayConfig;
import com.itheima.domain.Cart;
import com.itheima.domain.CartItem;
import com.itheima.domain.Order;
import com.itheima.domain.OrderItem;
import com.itheima.domain.PageBean;
import com.itheima.domain.User;
import com.itheima.service.OrderService;
import com.itheima.utils.BeanFactory;
import com.itheima.utils.UUIDUtils;

/**
 * 订单模块
 */
public class OrderServlet extends BaseServlet {

	/**
	 * 生成订单
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	public String add(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//0.判断用户是否登录
		User user=(User) request.getSession().getAttribute("user");
		if(user == null){
			request.setAttribute("msg", "请先登录~~");
			return "/jsp/msg.jsp";
		}
		
		//1.封装数据
		Order order=new Order();
		//1.1 订单id
		order.setOid(UUIDUtils.getId());
		
		//1.2 订单时间
		order.setOrdertime(new Date());
		
		//1.3 总金额
		//获取session中cart
		Cart cart=(Cart) request.getSession().getAttribute("cart");
		
		order.setTotal(cart.getTotal());
		
		//1.4 订单的所有订单项
		/*
		 * 先获取cart中itmes
		 * 遍历itmes 组装成orderItem
		 * 将orderItem添加到list(items)中
		 */
		for (CartItem cartItem : cart.getItmes()) {
			OrderItem oi = new OrderItem();
			
			//设置id
			oi.setItemid(UUIDUtils.getId());
			
			//设置购买数量
			oi.setCount(cartItem.getCount());
			
			//设置小计
			oi.setSubtotal(cartItem.getSubtotal());
			
			//设置product
			oi.setProduct(cartItem.getProduct());
			
			//设置order
			oi.setOrder(order);
			
			//添加到list中
			order.getItems().add(oi);
		}
		
		//1.5 设置用户
		order.setUser(user);
		
		//2.调用service 添加订单
		OrderService os=(OrderService) BeanFactory.getBean("OrderService");
		os.add(order);
		
		//3.将order放入request域中,请求转发
		request.setAttribute("bean", order);
		
		//4.清空购物车
		request.getSession().removeAttribute("cart");
		return "/jsp/order_info.jsp";
	}

	/**
	 * 分页查询我的订单
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String findAllByPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//1.获取当前页
		int currPage=Integer.parseInt(request.getParameter("currPage"));
		int pageSize=3;
		
		//2.获取用户
		User user=(User) request.getSession().getAttribute("user");
		if(user == null){
			request.setAttribute("msg", "你还没有登录,请登录!");
			return "/jsp/msg.jsp";
		}
		
		//3.调用service 分页查询  参数:currpage pagesize user  返回值:PageBean
		OrderService os=(OrderService) BeanFactory.getBean("OrderService");
		PageBean<Order> bean=os.findAllByPage(currPage,pageSize,user);
		
		//4.将PageBean放入request域中
		request.setAttribute("pb", bean);
		
		return "/jsp/order_list.jsp";
	}
	
	public String getById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//1.获取oid
		String oid = request.getParameter("oid");
		
		//2.调用service 通过oid 返回值:order
		OrderService os=(OrderService) BeanFactory.getBean("OrderService");
		Order order=os.getById(oid);
		
		//3.将order放入request域中
		request.setAttribute("bean", order);
		
		return "/jsp/order_info.jsp";
	}
	
	public String pay(HttpServletRequest request,HttpServletResponse response) throws Exception{
		/*//接受参数
		String address=request.getParameter("address");
		String name=request.getParameter("name");
		String telephone=request.getParameter("telephone");
		String oid=request.getParameter("oid");
		
		
		//通过id获取order
		OrderService s=(OrderService) BeanFactory.getBean("OrderService");
		Order order = s.getById(oid);
		
		order.setAddress(address);
		order.setName(name);
		order.setTelephone(telephone);
		
		//更新order
		s.update(order);
		

		// 组织发送支付公司需要哪些数据
		String pd_FrpId = request.getParameter("pd_FrpId");
		String p0_Cmd = "Buy";
		String p1_MerId = ResourceBundle.getBundle("merchantInfo").getString("p1_MerId");
		String p2_Order = oid;
		String p3_Amt = "0.01";
		String p4_Cur = "CNY";
		String p5_Pid = "";
		String p6_Pcat = "";
		String p7_Pdesc = "";
		// 支付成功回调地址 ---- 第三方支付公司会访问、用户访问
		// 第三方支付可以访问网址
		String p8_Url = ResourceBundle.getBundle("merchantInfo").getString("responseURL");
		String p9_SAF = "";
		String pa_MP = "";
		String pr_NeedResponse = "1";
		// 加密hmac 需要密钥
		String keyValue = ResourceBundle.getBundle("merchantInfo").getString("keyValue");
		String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt,
				p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP,
				pd_FrpId, pr_NeedResponse, keyValue);
	
		
		//发送给第三方
		StringBuffer sb = new StringBuffer("https://www.yeepay.com/app-merchant-proxy/node?");
		sb.append("p0_Cmd=").append(p0_Cmd).append("&");
		sb.append("p1_MerId=").append(p1_MerId).append("&");
		sb.append("p2_Order=").append(p2_Order).append("&");
		sb.append("p3_Amt=").append(p3_Amt).append("&");
		sb.append("p4_Cur=").append(p4_Cur).append("&");
		sb.append("p5_Pid=").append(p5_Pid).append("&");
		sb.append("p6_Pcat=").append(p6_Pcat).append("&");
		sb.append("p7_Pdesc=").append(p7_Pdesc).append("&");
		sb.append("p8_Url=").append(p8_Url).append("&");
		sb.append("p9_SAF=").append(p9_SAF).append("&");
		sb.append("pa_MP=").append(pa_MP).append("&");
		sb.append("pd_FrpId=").append(pd_FrpId).append("&");
		sb.append("pr_NeedResponse=").append(pr_NeedResponse).append("&");
		sb.append("hmac=").append(hmac);
		
		respone.sendRedirect(sb.toString());*/
		String address=request.getParameter("address");
		String name=request.getParameter("name");
		String telephone=request.getParameter("telephone");
		String oid=request.getParameter("oid");
		
		
		//通过id获取order
		OrderService s=(OrderService) BeanFactory.getBean("OrderService");
		Order order = s.getById(oid);
		
		order.setAddress(address);
		order.setName(name);
		order.setTelephone(telephone);
		
		//更新order
		s.update(order);
		//获得初始化的AlipayClient
		AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
		
		//设置请求参数
		AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
		alipayRequest.setReturnUrl(AlipayConfig.return_url);
		alipayRequest.setNotifyUrl(AlipayConfig.notify_url);
		
		//商户订单号，商户网站订单系统中唯一订单号，必填
		String out_trade_no = oid;
		//付款金额，必填
		String total_amount = "0.01";
		//订单名称，必填
		String subject ="test";
		//商品描述，可空
		String body = "test";
		
		alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\"," 
				+ "\"total_amount\":\""+ total_amount +"\"," 
				+ "\"subject\":\""+ subject +"\"," 
				+ "\"body\":\""+ body +"\"," 
				+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
		String result = alipayClient.pageExecute(alipayRequest).getBody();
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().println("<html><body>");
		response.getWriter().println(result);
		response.getWriter().println("</body></html>");
		return null;
	}
	/**
	 * 支付成功之后的回调
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String callback(HttpServletRequest request,HttpServletResponse response) throws Exception{
		/*String p1_MerId = request.getParameter("p1_MerId");
		String r0_Cmd = request.getParameter("r0_Cmd");
		String r1_Code = request.getParameter("r1_Code");
		String r2_TrxId = request.getParameter("r2_TrxId");
		String r3_Amt = request.getParameter("r3_Amt");
		String r4_Cur = request.getParameter("r4_Cur");
		String r5_Pid = request.getParameter("r5_Pid");
		String r6_Order = request.getParameter("r6_Order");
		String r7_Uid = request.getParameter("r7_Uid");
		String r8_MP = request.getParameter("r8_MP");
		String r9_BType = request.getParameter("r9_BType");
		String rb_BankId = request.getParameter("rb_BankId");
		String ro_BankOrderId = request.getParameter("ro_BankOrderId");
		String rp_PayDate = request.getParameter("rp_PayDate");
		String rq_CardNo = request.getParameter("rq_CardNo");
		String ru_Trxtime = request.getParameter("ru_Trxtime");
		// 身份校验 --- 判断是不是支付公司通知你
		String hmac = request.getParameter("hmac");
		String keyValue = ResourceBundle.getBundle("merchantInfo").getString(
				"keyValue");

		// 自己对上面数据进行加密 --- 比较支付公司发过来hamc
		boolean isValid = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd,
				r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid,
				r8_MP, r9_BType, keyValue);
		if (isValid) {
			// 响应数据有效
			if (r9_BType.equals("1")) {
				// 浏览器重定向
				System.out.println("111");
				request.setAttribute("msg", "您的订单号为:"+r6_Order+",金额为:"+r3_Amt+"已经支付成功,等待发货~~");
				
			} else if (r9_BType.equals("2")) {
				// 服务器点对点 --- 支付公司通知你
				System.out.println("付款成功！222");
				// 修改订单状态 为已付款
				// 回复支付公司
				response.getWriter().print("success");
			}
			
			//修改订单状态
			OrderService s=(OrderService) BeanFactory.getBean("OrderService");
			Order order = s.getById(r6_Order);
			
			//修改订单状态为 已支付
			order.setState(1);
			
			s.update(order);
			
		} else {
			// 数据无效
			System.out.println("数据被篡改！");
		}*/
		//获取支付宝GET过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map<String,String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用
			valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		
		boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名

		//——请在这里编写您的程序（以下代码仅作参考）——
		if(signVerified) {
			//商户订单号
			String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
		
			//支付宝交易号
			//String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
		
			//付款金额
			String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"),"UTF-8");
			//获取支付是否成功
			String msg = new String(request.getParameter("msg").getBytes("ISO-8859-1"),"UTF-8");
			if(msg.equals("Success")) {
				request.setAttribute("msg", "您的订单号为:"+out_trade_no+",金额为:"+total_amount+"已经支付成功,等待发货~~");
			}
			//修改订单状态
			OrderService s=(OrderService) BeanFactory.getBean("OrderService");
			Order order = s.getById(out_trade_no);
			
			//修改订单状态为 已支付
			order.setState(1);
			
			s.update(order);
			
		}else {
			request.setAttribute("msg", "系统错误，请联系管理员处理");
		}
		return "/jsp/msg.jsp";
		
	}
	
	
	/*
	 * 确认收获
	 */
	public String updateState(HttpServletRequest request,HttpServletResponse response) throws Exception{
		//1.获取 oid
		String oid = request.getParameter("oid");
		
		//2.调用service 修改订单状态
		OrderService os=(OrderService) BeanFactory.getBean("OrderService");
		Order order = os.getById(oid);
		order.setState(3);
		os.update(order);
		
		//3.重定向
		response.sendRedirect(request.getContextPath()+"/order?method=findAllByPage&currPage=1");
		return null;
	}
}
