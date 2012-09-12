package com.kissme.core.domain;


/**
 * 
 * @author loudyn
 *
 * @param <T>
 */
public interface Duplicable<T> {
	/**
	 * 
	 * @return
	 */
	T duplicate();
}
