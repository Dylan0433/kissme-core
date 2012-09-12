package com.kissme.core.web.filter;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.kissme.core.annotation.Immutable;

/**
 * 
 * @author loudyn
 * 
 */
public class FlashModel {
	static final String FLASH_MODEL_ATTRIBUTE = FlashModel.class.getName();

	/**
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getCurrent(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Map<String, Object> flash = (Map<String, Object>) session.getAttribute(FLASH_MODEL_ATTRIBUTE);
		if (flash == null) {
			flash = new HashMap<String, Object>();
			session.setAttribute(FLASH_MODEL_ATTRIBUTE, flash);
		}
		return flash;
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public static void put(String key, Object value) {
		getCurrent(getRequest(RequestContextHolder.currentRequestAttributes())).put(key, value);
	}

	/**
	 * 
	 * @param requestAttributes
	 * @return
	 */
	private static HttpServletRequest getRequest(RequestAttributes requestAttributes) {
		return ((ServletRequestAttributes) requestAttributes).getRequest();
	}

	/**
	 * 
	 * @param info
	 */
	public static void setInfoMessage(String info) {
		put(MESSAGE_KEY, new Message(MessageType.info, info));
	}

	/**
	 * 
	 * @param warning
	 */
	public static void setWarningMessage(String warning) {
		put(MESSAGE_KEY, new Message(MessageType.warning, warning));
	}

	/**
	 * 
	 * @param error
	 */
	public static void setErrorMessage(String error) {
		put(MESSAGE_KEY, new Message(MessageType.error, error));
	}

	/**
	 * 
	 * @param success
	 */
	public static void setSuccessMessage(String success) {
		put(MESSAGE_KEY, new Message(MessageType.success, success));
	}

	private static final String MESSAGE_KEY = "message";

	@Immutable
	public static final class Message {

		private final MessageType type;

		private final String text;

		public Message(MessageType type, String text) {
			this.type = type;
			this.text = text;
		}

		public MessageType getType() {
			return type;
		}

		public String getText() {
			return text;
		}

		public String toString() {
			return type + ": " + text;
		}

	}

	public static enum MessageType {
		info, success, warning, error
	}

	private FlashModel() {
	}
}
