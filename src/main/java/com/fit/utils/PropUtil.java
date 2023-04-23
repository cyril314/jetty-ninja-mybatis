package com.fit.utils;

import com.fit.common.utils.ConverterUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @AUTO 去读properties文件工具
 * @FILE JDBCContentUtil.java
 * @DATE 2017-8-4 下午2:45:38
 * @Author AIM
 */
public class PropUtil {

	// 定时器时间
	public static String quartzTime;
	// 通信使用key
	public static String key = "53549970c720bbc9970eb03dccfc2c8d";
	//
	public static int e_type = 0;
	// 邮件服务器主机名
	public static String e_ip_domain = "";
	// 发送邮件协议名称
	public static String protocol = "";
	// 邮件服务器端口
	public static String e_port = "";
	// 发件人邮箱账户，注：@之前的内容
	public static String email = "";
	// 发件人邮箱密码
	public static String epass = "";
	// 发件人邮箱完整地址
	public static String e_email = "";
	// 项目的访问路径
	public static String item_url = "";

	static {
		InputStream in = null;
		try {
			in = PropUtil.class.getClassLoader().getResourceAsStream("mail.properties");
			Properties p = new Properties();
			p.load(in);
			quartzTime = p.getProperty("quartzTime");
			// 邮件设置信息
			e_type = ConverterUtils.toInt(p.getProperty("mail.types"));
			e_ip_domain = p.getProperty("mail.host");
			protocol = p.getProperty("mail.protocol");
			e_port = p.getProperty("mail.port");
			email = p.getProperty("mail.username");
			epass = p.getProperty("mail.password");
			e_email = p.getProperty("mail.default.from");
			item_url = p.getProperty("mail.itemurl");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 获取配置文件对象
	 * 
	 * @param fileName
	 *            配置文件名称
	 */
	public static Properties getPropertie(String filePath) {
		try {
			InputStream in = PropUtil.class.getClassLoader().getResourceAsStream(filePath);
			Properties p = new Properties();
			p.load(in);
			return p;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
