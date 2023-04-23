package com.fit.common.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @AUTO 编码设置过滤器
 * @FILE CharsetEncodingFilter.java
 * @DATE 2017-8-27 下午1:25:08
 * @Author AIM
 */
public class CharsetEncodingFilter implements Filter {

	private static Logger logger = LoggerFactory.getLogger(CharsetEncodingFilter.class);

	private String encoding = "UTF-8";

	// 自写一个request换掉原来的request,重写里面的getParemeter方法，可以设置编码
	class MyRequest extends HttpServletRequestWrapper {

		@Override
		public String getParameter(String param) {
			String value = null;
			try {
				// post
				super.setCharacterEncoding(encoding);// 把编码转换为encoding
				value = super.getParameter(param);
				if (super.getMethod().equalsIgnoreCase("GET")) {
					if (value != null) {
						value = new String(value.getBytes("iso-"), encoding);
					}
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return value;
		}

		public MyRequest(HttpServletRequest request) {
			super(request);
		}

	}

	public void destroy() { // 销毁
		// TODO Auto-generated method stub
		this.encoding = "UTF-8";
		logger.info("--------destroy---------");
	}

	// 对编码问题进行转换
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=" + encoding);
		// 过滤未登录用户
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		String path = req.getServletPath();
		String param = req.getQueryString();
		if (path != null) {
			path = path + "?" + param;// 全请求路径
		}
		if (path.indexOf("Order") != -1) {
			HttpSession session = req.getSession();
			String userName = (String) session.getAttribute("username");
			if (userName == null) {
				session.setAttribute("url", path.replaceFirst("/", ""));
				System.out.println(session.getAttribute("url"));
				resp.sendRedirect("user.do?op=loginAction");
				return;
			}
		}
		// 把过滤器给下一个过滤器或资源处理器
		chain.doFilter(new MyRequest((HttpServletRequest) request), response);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		logger.info("--------init---------");
		logger.info("编码类型：" + encoding);
		this.encoding = filterConfig.getInitParameter("encoding");// encoding在web.xml中指定
	}

}