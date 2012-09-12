package com.kissme.core.domain;

/**
 * 
 * @author loudyn
 * 
 * @param <T>
 */
public interface ValueObject<T> {
	/**
	 * 
	 * @param other
	 * @return
	 */
	boolean sameValueAs(T other);
}
