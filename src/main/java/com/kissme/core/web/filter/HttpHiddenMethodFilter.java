package com.kissme.core.web.filter;


import java.io.IOException;
import java.util.Locale;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * avoid using hidden method,we can call with ajax request but some browser,it didnot support put method
 * 
 * @author loudyn
 */
public class HttpHiddenMethodFilter extends OncePerRequestFilter {

	public static final String DEFAULT_METHOD_PARAM = "_method";
	private String methodParam = DEFAULT_METHOD_PARAM;

	/**
	 * @param methodParam
	 */
	public void setMethodParam(String methodParam) {
		this.methodParam = methodParam;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.web.filter.OncePerRequestFilter#doFilterInternal( javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		filterChain.doFilter(new HttpHiddenMethodRequestWrapper(request, request.getParameter(methodParam)), response);
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.web.filter.OncePerRequestFilter#shouldNotFilter(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

		String methodValue = request.getParameter(methodParam);
		return !("POST".equals(request.getMethod()) && StringUtils.isNotBlank(methodValue));
	}

	/**
	 * @author loudyn
	 */
	public class HttpHiddenMethodRequestWrapper extends HttpServletRequestWrapper {
		private final String method;

		/**
		 * @param request
		 * @param method
		 */
		public HttpHiddenMethodRequestWrapper(HttpServletRequest request, final String method) {
			super(request);
			this.method = method;
		}

		@Override
		public String getMethod() {
			return this.method.toUpperCase(Locale.ENGLISH);
		}
	}
}
