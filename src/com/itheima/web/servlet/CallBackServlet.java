package com.itheima.web.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.config.AlipayConfig;
import com.itheima.domain.Order;
import com.itheima.service.OrderService;
import com.itheima.utils.BeanFactory;

import net.sf.json.JSONObject;

/**
 * Servlet implementation class CallBackServlet
 */
public class CallBackServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// 获取支付宝GET过来反馈信息
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			// 乱码解决，这段代码在出现乱码时使用
			valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}

		boolean signVerified;
		try {
			signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset,
					AlipayConfig.sign_type);
			// 调用SDK验证签名

			// ——请在这里编写您的程序（以下代码仅作参考）——
			if (signVerified) {
				//获得初始化的AlipayClient
				AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id,
						AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key,
						AlipayConfig.sign_type);

				//设置请求参数
				AlipayTradeQueryRequest alipayRequest = new AlipayTradeQueryRequest();

				//商户订单号，商户网站订单系统中唯一订单号
				String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
				//支付宝交易号
				String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
				//付款金额
				String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"),"UTF-8");
				alipayRequest.setBizContent(
						"{\"out_trade_no\":\"" + out_trade_no + "\"," + "\"trade_no\":\"" + trade_no + "\"}");

				//请求
				String result = alipayClient.execute(alipayRequest).getBody();
				
				JSONObject obj = JSONObject.fromObject(result);
				JSONObject msgobj = obj.getJSONObject("alipay_trade_query_response");
				String msg = msgobj.get("msg").toString();
				if (msg.equals("Success")) {
					request.setAttribute("msg", "您的订单号为:" + out_trade_no + ",金额为:" + total_amount + "已经支付成功,等待发货~~");
				}
				// 修改订单状态
				OrderService s = (OrderService) BeanFactory.getBean("OrderService");
				Order order = s.getById(out_trade_no);

				// 修改订单状态为 已支付
				order.setState(1);

				s.update(order);

			} else {
				request.setAttribute("msg", "系统错误，请联系管理员处理");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.getRequestDispatcher("/jsp/msg.jsp").forward(request, response);
	}

}
