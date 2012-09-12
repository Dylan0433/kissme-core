package com.kissme.core.helper;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringEscapeUtils;

import com.kissme.lang.Lang;
import com.kissme.lang.Preconditions;

/**
 * 
 * @author loudyn
 * 
 */
public abstract class CodeHelper {

	private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private static final String DEFAULT_URL_ENCODING = "UTF-8";

	/**
	 * 
	 * @param input
	 * @return
	 */
	public static String encodeHex(byte[] input) {
		return Hex.encodeHexString(input);
	}

	/**
	 * 
	 * @param input
	 * @return
	 */
	public static byte[] decodeHex(String input) {
		try {
			return Hex.decodeHex(input.toCharArray());
		} catch (DecoderException e) {
			throw new IllegalStateException("Hex Decoder exception", e);
		}
	}

	/**
	 * 
	 * @param input
	 * @return
	 */
	public static String encodeBase64(byte[] input) {
		return Base64.encodeBase64String(input);
	}

	/**
	 * 
	 * @param input
	 * @return
	 */
	public static String encodeUrlSafeBase64(byte[] input) {
		return Base64.encodeBase64URLSafeString(input);
	}

	/**
	 * 
	 * @param input
	 * @return
	 */
	public static byte[] decodeBase64(String input) {
		return Base64.decodeBase64(input);
	}

	/**
	 * 
	 * @param num
	 * @return
	 */
	public static String encodeBase62(long num) {
		return alphabetEncode(num, 62);
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static long decodeBase62(String str) {
		return alphabetDecode(str, 62);
	}

	private static String alphabetEncode(long num, int base) {
		num = Math.abs(num);
		StringBuilder sb = new StringBuilder();
		for (; num > 0; num /= base) {
			sb.append(ALPHABET.charAt((int) (num % base)));
		}

		return sb.toString();
	}

	private static long alphabetDecode(String str, int base) {
		Preconditions.hasText(str);

		long result = 0;
		for (int i = 0; i < str.length(); i++) {
			result += ALPHABET.indexOf(str.charAt(i)) * Math.pow(base, i);
		}

		return result;
	}

	/**
	 * 
	 * @param part
	 * @param encoding
	 * @return
	 */
	public static String urlEncode(String part, String encoding) {
		try {
			return URLEncoder.encode(part, encoding);
		} catch (UnsupportedEncodingException e) {
			throw Lang.uncheck(e);
		}
	}

	/**
	 * 
	 * @param part
	 * @return
	 */
	public static String urlEncode(String part) {
		return urlEncode(part, DEFAULT_URL_ENCODING);
	}

	/**
	 * 
	 * @param part
	 * @param encoding
	 * @return
	 */
	public static String urlDecode(String part, String encoding) {

		try {

			return URLDecoder.decode(part, encoding);
		} catch (UnsupportedEncodingException e) {
			throw Lang.uncheck(e);
		}
	}

	/**
	 * 
	 * @param part
	 * @return
	 */
	public static String urlDecode(String part) {
		return urlDecode(part, DEFAULT_URL_ENCODING);
	}

	/**
	 * 
	 * @param html
	 * @return
	 */
	public static String htmlEscape(String html) {
		return StringEscapeUtils.escapeHtml(html);
	}

	/**
	 * Html ����.
	 */
	public static String htmlUnescape(String htmlEscaped) {
		return StringEscapeUtils.unescapeHtml(htmlEscaped);
	}

	/**
	 * 
	 * @param xml
	 * @return
	 */
	public static String xmlEscape(String xml) {
		return StringEscapeUtils.escapeXml(xml);
	}

	/**
	 * 
	 * @param xmlEscaped
	 * @return
	 */
	public static String xmlUnescape(String xmlEscaped) {
		return StringEscapeUtils.unescapeXml(xmlEscaped);
	}

	private CodeHelper() {}
}
