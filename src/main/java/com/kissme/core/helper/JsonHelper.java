package com.kissme.core.helper;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import com.kissme.lang.Lang;

/**
 * @author loudyn
 */
public class JsonHelper {
	private final static ObjectMapper MAPPER = new ObjectMapper();

	static {
		MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		MAPPER.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		MAPPER.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		MAPPER.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
	}

	/**
	 * @param bean
	 * @return
	 */
	public static String toJsonString(Object bean) {
		try {

			return MAPPER.writeValueAsString(bean);
		} catch (Exception e) {
			throw Lang.uncheck(e);
		}
	}

	/**
	 * 
	 * @param <T>
	 * @param jsonString
	 * @param clazz
	 * @return
	 */
	public static <T> T fromJsonString(String jsonString, Class<T> clazz) {
		try {

			return MAPPER.readValue(jsonString, clazz);
		} catch (Exception e) {
			throw Lang.uncheck(e);
		}
	}

	/**
	 * 
	 * @param bean
	 * @param clazz
	 * @return
	 */
	public static <T> T newfor(Object bean, Class<T> clazz) {
		try {

			return MAPPER.convertValue(bean, clazz);
		} catch (Exception e) {
			throw Lang.uncheck(e);
		}
	}

	private JsonHelper() {}
}
