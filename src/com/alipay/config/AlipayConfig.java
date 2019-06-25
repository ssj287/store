package com.alipay.config;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {
	
//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
	public static String app_id = "2016101100656887";
	
	// 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDCTbZ8m2ZBmT77V7hVRMW50xS+Cl0smk9DykUbKfhCwFb9OlZfjHDyR2wX4RPk3IRI/G3xWCBgeOhagClRyXFEhfpD2hObASUarIyzyeHs8nYzCGQio5pv85rhfdb+Yyu4aHBgInradJ/4sGNjKl+aDZ/5RzHcKV8iYMemRq98grRfkV9G05ZkpjqoiCijBuksBsMFZuDXrzJywuXr3NJNaZehlELJmt0sUSaka3y+vTERcMD+mSv95xhhZk63L6KMDY9CSqAF/X18NXUhwvmg+lwCSF83y4kqMFf9RnPMqjtncxY3HoI+hQi3zK6sn5JdMxMqK/28YhjbiYRhBPMhAgMBAAECggEBAJG166L4eq+494VPXDn9r/XRuJ1nckYvlj+1ISvkxntweAvAwuM2k8DtFL+3l86qHD4Lp3s05lrScRTlRZdC5MFJY8ucb9Vu/19IhN+TBu1FSqSsp0New0MvQTfWwfLVdgT9tUAyu0S17v11caL9NOJ3Uz+CR37eO6yxPSeRnKpWnpMUXqL32LKMzRJFUEb7yC47O5uFaoPWnqsdjaJ+O5FkEsESlm6b0kO4dNCScbMuJcDqjeUOI1z7u7aJLEbxFG9qPR4/HdlcMK04v0WXOdiFSVljqHEQFvu15lEbQeC4CKcXAMwiml6J7XFEZdGDh3BQodk5Gt1m0qaDZ5YtEG0CgYEA859xK/05g57Oq6wBVWSicLF50J2VoiboSF1AkY6/ymuFrLGyb8HFMX2TDd+n3TBHxA1Gop8VyakSnbQC+AC6Lojk70BURuGKrZU+gta+kArjB4RcWen+b/kasRpqaLgvuA2ghqfOQgyUFqoxCBWRYDiwDiDhcygQVMQ5hO83hQcCgYEAzCzTGv3aH42lbi+TpC4aolZKHjaemVLMSYxZIPUL1K+5waVHbQDqfGTJ4FyXO18EZNPdyKMibz+rzLB/kl0xUbxF4WRZ7kZc+fOCtUZPoEvajb/YZQSqfDqYuO385p+PlhIlJzG+nRArq5uDjy6QWEMLuGDSN1QkgtfU8BtLpJcCgYA4fgeDONQGZmPsQRWKFKzqTu09Z91T3YvNg/o+BokIykgUFJyY96jfYgwgknVdgB2UI7P2b8dgnNkZQq3RjnGd6taTeCpSyWgb+lilhGBqDGw6yQGVRAJhOrVqzgJQyC0gk+BeeTkF7d7FJq/5/O5aDqsci5lb17V5worH/aS1LwKBgDRHogJ+T7TWoy50ljxq5bz9lVrpJ3EhB1gpzMSomk/qcwkHliNuIZyafiqp6rt0F/7tVRPD2dRcnFP8OZ4zqKZCLC6LcI+XisUhui1KhKcJ5Tt4jSNIr5LBuktX/PLclnqZyNOhZCp5coUte+FUzfcbq8uZVxp9vB+YvliA1LyfAoGAB7bvG2PXesNEYLb+gHEkIZyCeCBahiXE2TKtDF5tRbSGGbQY33jKpbP60q3oFQu4JDV2ONkXOHz9VgQ89Nh8pRkDLLa0vCmXrTr2tnU5aaYWEHfbqh18xCUm0+++1xsJVEVCVjf2iZH2f2XDaDr45FqJFmG5k5tzLoVMkraTLno=";
	
	// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAh9YEAMKtqcvd4a4PkqWRCk4/bMCmU1u50GOL6eAuWZhYWdOz7geI7Mnwc+StsWR0bKbG++hKYP0KoH1zRcDiT9JxlX0dDsY9qT/zPoInU34uqNpT406I8E6bNmDxVwRVljNbjzu4OHLRTZ0d1nkATAG1B07X83MiMbJJSBA9UZeO+fLxsU/D2O01QtkbEFD4qQWhv7W3MrTo6qeg+6McXZEYQx/X57PJwzKbTPDeB6J3DciJboPKjV7xY3P5JvmBuUWjt5RVlhZfkydQTt+v79O7b35VknbF2hwi65tI8PDNMh8oLT32p/9a/63HhEXIsoup5Rp2XQkuJ0USXxZ1eQIDAQAB";

	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "http://localhost:8080/store/jsp/notify_url.jsp";

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String return_url = "http://store.mqy.xyz/store/payresult";

	// 签名方式
	public static String sign_type = "RSA2";
	
	// 字符编码格式
	public static String charset = "utf-8";
	
	// 支付宝网关
	public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";
	
	// 支付宝网关
	public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /** 
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

