package com.kissme.core.helper;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author loudyn
 * 
 */
public final class PatternHelper {

	private static final List<Character> QUOTES = Arrays.asList(new Character[] {
																					'+', '*', '{', '}',
																					'[', ']', '(', ')',
																					'|', '\\', '$', '^',
																					'?'
																	}
															);

	/**
	 * 
	 * @param string
	 * @return
	 */
	public static boolean hasQuotes(String string) {
		if (StringUtils.isBlank(string)) {
			return false;
		}

		int length = string.length();
		for (int i = 0; i < length; i++) {
			if (QUOTES.contains(string.charAt(i))) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 
	 * @param string
	 * @return
	 */
	public static String quoteReplace(String string) {
		if (StringUtils.isBlank(string)) {
			return string;
		}

		StringBuilder result = new StringBuilder();
		char[] chars = string.toCharArray();
		int length = chars.length;

		for (int i = 0; i < length; i++) {
			char c = chars[i];
			if (QUOTES.contains(c)) {
				result.append("\\");
			}

			result.append(c);
		}

		return result.toString();
	}

	/**
	 * 
	 * @param string
	 * @return
	 */
	public static String contains(String string) {
		return String.format(".*?%s.*?", quoteReplace(string));
	}

	/**
	 * 
	 * @param string
	 * @return
	 */
	public static String startWith(String string) {
		return String.format("^%s", quoteReplace(string));
	}

	/**
	 * 
	 * @param string
	 * @return
	 */
	public static String endWith(String string) {
		return String.format("%s$", quoteReplace(string));
	}

	/**
	 * 
	 * @return
	 */
	public static String digits() {
		return "[0-9]+";
	}

	/**
	 * 
	 * @return
	 */
	public static String letters() {
		return "[a-zA-Z]+";
	}

	private PatternHelper() {}
}
