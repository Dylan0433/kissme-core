package com.kissme.core.domain.event;

/**
 * 
 * @author loudyn
 * 
 */
public interface LifecycleAware<T> {

	/**
	 * 
	 * @return
	 */
	public T create();

	/**
	 * 
	 * @return
	 */
	public T modify();

	/**
	 * 
	 * @return
	 */
	public T delete();
}
