package com.kissme.core.helper;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import com.kissme.lang.Lang;
import com.kissme.lang.Preconditions;

/**
 * 
 * @author loudyn
 * 
 */
public class ConvertHelper {

	/**
	 * 
	 * @param <T>
	 * @param beans
	 * @param keyPropertyName
	 * @param valuePropertyName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T, PK, PV> void propertyToMap(Collection<T> beans,
														String keyPropertyName,
														String valuePropertyName,
														Map<PK, PV> target) {

		Preconditions.notNull(beans, "beans must not null");
		Preconditions.hasLength(keyPropertyName, "keyPropertyName must not blank");
		Preconditions.hasLength(valuePropertyName, "valuePropertyName must not blank");
		Preconditions.notNull(target, "target must not null");

		try {

			for (T bean : beans) {
				PK pk = (PK) BeanUtils.getProperty(bean, keyPropertyName);
				PV pv = (PV) BeanUtils.getProperty(bean, valuePropertyName);
				target.put(pk, pv);
			}

		} catch (Exception e) {
			throw Lang.uncheck(e);
		}

	}

	/**
	 * 
	 * @param <T>
	 * @param beans
	 * @param propertyName
	 * @param separator
	 * @return
	 */
	public static <T> String propertyToString(Collection<T> beans,
														String propertyName,
														String separator) {
		Preconditions.notNull(beans, "beans must not null");
		Preconditions.hasLength(propertyName, "propertyName must not blank");

		List<String> target = new ArrayList<String>();
		propertyToList(beans, propertyName, target);
		return StringUtils.join(target, separator);
	}

	/**
	 * 
	 * @param <T>
	 * @param beans
	 * @param propertyName
	 * @param separator
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T, PV> void propertyToList(Collection<T> beans,
													String propertyName,
													List<PV> target) {

		Preconditions.notNull(beans, "beans must not null");
		Preconditions.hasLength(propertyName, "propertyName must not blank");
		Preconditions.notNull(target, "target must not null");

		try {

			for (T bean : beans) {
				target.add((PV) BeanUtils.getProperty(bean, propertyName));
			}

		} catch (Exception e) {
			throw Lang.uncheck(e);
		}
	}

	private ConvertHelper(){}
}
