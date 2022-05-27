package com.jspcourse.cake_shop.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet Filter implementation class FilterEncoding
 */
@WebFilter(filterName="FilterEncoding",
			urlPatterns="/*",
			initParams= {@WebInitParam(name="encoding",value="utf-8")})
public class FilterEncoding implements Filter {
	private String encoding;
	

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest httpRequest=(HttpServletRequest) request;
		HttpServletResponse httpResponse=(HttpServletResponse) response;
		
		httpRequest.setCharacterEncoding(this.encoding);
		httpResponse.setCharacterEncoding(this.encoding);
		
		//System.out.println(DateUtil.show()+">>设置encoding"+this.encoding);
		
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) {
		this.encoding =fConfig.getInitParameter("encoding");
	}

	@Override
	public void destroy() {
		
	}

}
