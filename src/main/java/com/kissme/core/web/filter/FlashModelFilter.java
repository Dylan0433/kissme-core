package com.kissme.core.web.filter;


import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.filter.OncePerRequestFilter;


/**
 * 
 * @author loudyn
 * 
 */
public final class FlashModelFilter extends OncePerRequestFilter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.filter.OncePerRequestFilter#doFilterInternal(javax.servlet.http. HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// how to share data between two request?
		// the flashModelFilter try to do this
		// we put first request'data in to the session
		// in the second request,we fetch these data out from the session
		// and set to the current request,remerber clear the request'data in session
		HttpSession session = request.getSession(false);
		if (null != session) {
			Map<String, ?> flash = (Map<String, ?>) session.getAttribute(FlashModel.FLASH_MODEL_ATTRIBUTE);
			if (null != flash) {
				for (Map.Entry<String, ?> entry : flash.entrySet()) {
					Object currentValue = request.getAttribute(entry.getKey());
					if (null == currentValue) {
						request.setAttribute(entry.getKey(), entry.getValue());
					}
				}
				session.removeAttribute(FlashModel.FLASH_MODEL_ATTRIBUTE);
			}
		}
		filterChain.doFilter(request, response);
	}

}
