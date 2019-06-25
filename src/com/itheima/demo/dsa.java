package com.itheima.demo;

import org.junit.Test;

import net.sf.json.JSONObject;

public class dsa {
	@Test
	public void f1() {
		final String JSON_A="{'alipay_trade_query_response':{'code':'10000','msg':'Success','buyer_logon_id':'qju***@sandbox.com','buyer_pay_amount':'0.00','buyer_user_id':'2088102178207000','buyer_user_type':'PRIVATE','invoice_amount':'0.00','out_trade_no':'2019621145042576','point_amount':'0.00','receipt_amount':'0.00','send_pay_date':'2019-06-21 14:51:25','total_amount':'0.01','trade_no':'2019062122001407001000044487','trade_status':'TRADE_SUCCESS'},'sign':'VYkFOZCHGUC9n8HawlTZFIvynZ2XZySYRuNImKIDCpCUEaatXRWkFNDW+dXYrpSYBX5VPParkk93rSpcL5JSTGLLgOktnyfsrcHX+pk2JplYxsqIb9pUJU9Ui87oDVF3fUKyUX8yedznBxePVcw4aasqAUoZlJYylpWUM1/5at/G24Xrs5t3/aLj87lZdUbBFHt2jDRdj/Moak7ifG3A5Ra2vYGXpcw9qWXgrDaMgJzo3dsFQIAwx/W18ayjeHW38bUGP/GyIw5K+F/omvMaZ1mFvxxoylHZOTYv1zA/luLVbzQFdBl30phA+6yC0x+oWl80H5Vb/rlae2vELxaM8Q=='}";
		JSONObject obj = JSONObject.fromObject(JSON_A);
		JSONObject msgobj = obj.getJSONObject("alipay_trade_query_response");
		System.out.println("name is : " + obj.get("alipay_trade_query_response"));
	    System.out.println("score is : " + msgobj.get("msg"));
	}
}
