package com.kissme.core.web;

import static org.apache.commons.lang.StringUtils.equalsIgnoreCase;
import static org.apache.commons.lang.StringUtils.isBlank;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.kissme.core.web.exception.MaliciousRequestException;
import com.kissme.lang.Lang;

/**
 * 
 * @author loudyn
 * 
 */
public class Webs {

	private final static String USE_PROXY = "x-forwarded-for";
	private final static String UNKNOW_HOST = "unknow";

	private final static Pattern IP_V4_PATTERN = Pattern.compile("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}");

	/**
	 * 
	 * @param requestedWith
	 * @return
	 */
	public static boolean isAjax(String requestedWith) {
		return StringUtils.isNotBlank(requestedWith) ? "XMLHttpRequest".equals(requestedWith) : false;
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	public static String requestIP(HttpServletRequest request) {
		// the client didn't use proxy
		if (null == request.getHeader(USE_PROXY)) {
			return request.getRemoteAddr();
		}

		// the client use proxy
		String ip = request.getHeader(USE_PROXY);

		if (isBlank(ip) || equalsIgnoreCase(UNKNOW_HOST, ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (isBlank(ip) || equalsIgnoreCase(UNKNOW_HOST, ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (isBlank(ip) || equalsIgnoreCase(UNKNOW_HOST, ip)) {
			ip = request.getRemoteAddr();
		}

		ip = StringUtils.substringBefore(ip, ",");
		if (!IP_V4_PATTERN.matcher(ip).matches()) {
			throw new MaliciousRequestException("Bad ip header!");
		}
		return ip;
	}

	/**
	 * 
	 * @param response
	 * @param fileName
	 * @param contentType
	 */
	public static void prepareDownload(HttpServletResponse response, String fileName, String contentType) {
		try {
			String encodedFileName = new String(fileName.getBytes("utf-8"), "ISO8859-1");

			response.reset();
			response.setCharacterEncoding("utf-8");
			response.setHeader("Content-disposition", String.format("attachment; filename=%s", encodedFileName));
			response.setContentType(contentType);
		} catch (Exception e) {
			throw Lang.uncheck(e);
		}

	}

	/**
	 * 
	 * @param response
	 * @param fileName
	 * @param contentType
	 */
	public static void prepareDownload(HttpServletResponse response, String fileName, ContentType contentType) {
		prepareDownload(response, fileName, contentType.asMeta());
	}

	/**
	 * 
	 * @author loudyn
	 * 
	 */
	public static enum ContentType {

		PPT("application/vnd.ms-powerpoint"),
		WORD("application/msword"),
		EXCEL("application/msexcel"),

		PDF("application/pdf"),

		OCTET("application/octet-strem"),

		ZIP("application/zip"),
		TXT("text/plain"),
		JS("application/x-javascript"),

		JPG("image/jpeg"),
		PNG("image/png"),
		BMP("application/x-bmp"),
		GIF("image/gif");

		private final String meta;

		private ContentType(final String meta) {
			this.meta = meta;
		}

		public String asMeta() {
			return meta;
		}
	}

	private Webs() {}
}
